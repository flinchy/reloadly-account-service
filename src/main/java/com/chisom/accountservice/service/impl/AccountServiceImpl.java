package com.chisom.accountservice.service.impl;

import com.chisom.accountservice.dto.request.AccountRegistrationRequest;
import com.chisom.accountservice.dto.request.LoginRequest;
import com.chisom.accountservice.dto.request.TransactionRequest;
import com.chisom.accountservice.dto.request.UpdateAccountRequest;
import com.chisom.accountservice.dto.response.*;
import com.chisom.accountservice.exception.CustomException;
import com.chisom.accountservice.model.SavingsAccount;
import com.chisom.accountservice.repository.SavingsAccountRepository;
import com.chisom.accountservice.service.AccountService;
import com.chisom.accountservice.utils.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.chisom.accountservice.constants.AccountServiceConstants.*;
import static com.chisom.accountservice.utils.AccountServiceUtils.generateAccountNumber;
import static com.chisom.accountservice.utils.AppConstants.*;

/**
 * @author Chisom.Iwowo
 */
@Service
public class AccountServiceImpl implements AccountService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final SavingsAccountRepository savingsAccountRepository;
    private final RestTemplate restTemplate;
    private final ConfigUtils configUtils;
    private final String accountServiceUrl;

    @Autowired
    public AccountServiceImpl(
            SavingsAccountRepository savingsAccountRepository,
            RestTemplate restTemplate,
            ConfigUtils configUtils,
            @Value("${account-server-health}") String accountServiceUrl
    ) {
        this.savingsAccountRepository = savingsAccountRepository;
        this.restTemplate = restTemplate;
        this.configUtils = configUtils;
        this.accountServiceUrl = accountServiceUrl;
    }

    /**
     * Login by calling the authorization server.
     * This returns back a jwt access token.
     *
     * @param loginRequest login request
     * @return LoginResponse.
     */
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        try {

            UriComponents builder = UriComponentsBuilder.fromHttpUrl(configUtils.getLoginUrl())
                    .queryParam("grant_type", "password")
                    .queryParam("username", loginRequest.getUsername())
                    .queryParam("password", loginRequest.getPassword()).build();

            HttpHeaders headers = basicAuthHeaders();

            HttpEntity<LoginRequest> entity = new HttpEntity<>(loginRequest, headers);

            return restTemplate.postForEntity(
                    builder.toUriString(),
                    entity, LoginResponse.class).getBody();

        } catch (Exception exception) {
            throw new CustomException("Invalid credentials", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * register for a new account.
     *
     * @param accountRegistrationRequest account registration request
     * @return Object
     */
    @Override
    public AccountRegisterResponse registerNewAccount(
            AccountRegistrationRequest accountRegistrationRequest
    ) {
        try {
            HttpHeaders headers = headers();
            HttpEntity<AccountRegistrationRequest> entity = new HttpEntity<>(
                    accountRegistrationRequest, headers);
            //sends request to be registered with the authorization server.
            AccountRegisterResponse apiResponse = restTemplate
                    .postForEntity(configUtils.getRegistrationUrl(), entity,
                            AccountRegisterResponse.class).getBody();
            //create a savings account for the user.
            return createSavingsAccount(apiResponse);

        } catch (Exception exception) {
            throw new CustomException(exception.getMessage(),
                    exception, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    private AccountRegisterResponse createSavingsAccount(
            AccountRegisterResponse apiResponse
    ) {
        if (apiResponse != null) {
            if (apiResponse.isStatus()) {
                //save savings account
                saveSavingsAccount(apiResponse.getData().getUserId(),
                        apiResponse.getData().getUsername());
                return apiResponse;
            }
            throw new CustomException(apiResponse.getMessage(),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
            throw new CustomException("could not process request",
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    private void saveSavingsAccount(Long userId, String username) {
        try {
            SavingsAccount savingsAccount = new SavingsAccount();
            savingsAccount.setAccountBalance(new BigDecimal(INITIAL_SAVINGS_ACCOUNT_BALANCE));
            savingsAccount.setAccountNumber(generateAccountNumber());
            savingsAccount.setUsername(username);
            savingsAccount.setUserId(userId);
            SavingsAccount savedAccount = savingsAccountRepository.save(savingsAccount);
            log.info("create a savings account for user....");
            //send email notification without token, because on account creation the token will
            //be unavailable until login
            sendNotification(
                    new EmailNotificationRequest(savedAccount.getUsername(), ACCOUNT_CREATION_SUBJECT,
                            ACCOUNT_CREATION_SUCCESS_MSG + savedAccount.getAccountNumber()));

        } catch (Exception exception) {
            throw new CustomException("caught an exception trying to process request",
                    exception, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * deposit money to savings account.
     *
     * @param amount    amount
     * @param principal loggedIn User
     */
    @Override
    public TransactionResponse deposit(
            double amount, Principal principal, HttpServletRequest request
    ) {

        try {
            //retrieve token.
            String token = request.getHeader("Authorization");
            //find the owner of the account
            Optional<SavingsAccount> savingsAccount =
                    savingsAccountRepository.findByUsername(principal.getName(), false);

            if (savingsAccount.isPresent()) {
                //update the account balance
                savingsAccount.get().setAccountBalance(savingsAccount.get()
                        .getAccountBalance().add(BigDecimal.valueOf(amount)));
                //save back to the database
                final SavingsAccount savedSavingsAccount =
                        savingsAccountRepository.save(savingsAccount.get());
                //save transaction in the transaction service..
                return saveTransaction(savedSavingsAccount, amount, principal, DEPOSIT_NARRATION,
                        DEPOSIT_NOTIFICATION_SUBJECT, token);
            }
            throw new CustomException("savings account does not exist",
                    HttpStatus.UNPROCESSABLE_ENTITY);

        } catch (Exception exception) {
            throw new CustomException(exception.getMessage(),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    /**
     * withdraw money from savings account.
     *
     * @param amount    amount
     * @param principal user
     */
    @Override
    public TransactionResponse withdraw(
            double amount, Principal principal, HttpServletRequest request
    ) {
        try {
            //retrieve token
            String token = request.getHeader("Authorization");
            //find the logged in user
            Optional<SavingsAccount> savingsAccount = savingsAccountRepository
                    .findByUsername(principal.getName(), false);

            if (savingsAccount.isPresent()) {
                //if the result of the amount you want to withdraw - available balance is < 0 return an error message insufficient fund.
                //update the account balance
                savingsAccount.get().setAccountBalance(savingsAccount.get()
                        .getAccountBalance().subtract(BigDecimal.valueOf(amount)));
                //save back to the database
                SavingsAccount savedAccount = savingsAccountRepository
                        .save(savingsAccount.get());
                //set transaction data
                return saveTransaction(savedAccount, amount, principal,
                        WITHDRAWAL_NARRATION, WITHDRAW_NOTIFICATION_SUBJECT, token);
            }
            throw new CustomException("savings account not found",
                    HttpStatus.UNPROCESSABLE_ENTITY);

        } catch (Exception exception) {
            throw new CustomException(exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * This method calls the transaction microservice, to save the data.
     *
     * @param savedSavingsAccount updated savings account
     * @param amount              amount
     * @param principal           logged in user
     * @param token               bearer token
     * @return String
     */
    private TransactionResponse saveTransaction(
            SavingsAccount savedSavingsAccount,
            double amount, Principal principal, String narration,
            String subject, String token
    ) {
        //set transaction data
        final TransactionRequest transactionRequest =
                setTransactionData(savedSavingsAccount, amount, principal.getName(),
                        narration);
        //set headers, adding basic auth.
        HttpHeaders headers = headerWithToken(token);

        HttpEntity<TransactionRequest> entity = new HttpEntity<>(transactionRequest, headers);
        //send transaction to transaction service.
        TransactionResponse response = restTemplate.postForEntity(configUtils.getTransactionUrl(),
                entity, TransactionResponse.class).getBody();

        if (Objects.requireNonNull(response).isStatus()) {
            //send notification
            sendNotification(
                    new EmailNotificationRequest(savedSavingsAccount.getUsername(), subject,
                            narration));
        } else {
            //send error notification
            new EmailNotificationRequest(savedSavingsAccount.getUsername(), NOTIFICATION_ERROR_SUBJECT,
                    NOTIFICATION_ERROR_MSG + ".");
        }
        return response;
    }

    private TransactionRequest setTransactionData(
            SavingsAccount savedSavingsAccount,
            double amount, String loggedInUser,
            final String narration
    ) {
        return TransactionRequest.builder()
                .accountBalance(savedSavingsAccount.getAccountBalance())
                .amount(BigDecimal.valueOf(amount))
                .narration(narration)
                .username(loggedInUser)
                .userId(savedSavingsAccount.getUserId())
                .build();
    }

    /**
     * update savings account details
     *
     * @param updateAccountRequest request
     * @param principal            logged in user.
     */
    @Override
    public UpdateAccountResponse updateAccountDetails(
            UpdateAccountRequest updateAccountRequest, Principal principal
    ) {
        try {
            Optional<SavingsAccount> savingsAccount =
                    savingsAccountRepository.findByUsername(principal.getName(), false);

            if (savingsAccount.isPresent()) {
                //set headers.
                HttpHeaders headers = headers();

                HttpEntity<UpdateAccountRequest> entity = new HttpEntity<>(
                        updateAccountRequest, headers);
                //set path variable
                Map<String, Long> pathVars = new HashMap<>();
                pathVars.put("userId", savingsAccount.get().getUserId());
                //build url string
                UriComponents builder = UriComponentsBuilder.fromHttpUrl(configUtils.getAccountUpdateUrl())
                        .path("{userId}")
                        .buildAndExpand(pathVars);
                //sends request to authorization server, for update.e
                UpdateAccountResponse apiResponse = restTemplate
                        .postForEntity(builder.toUriString(), entity, UpdateAccountResponse.class).getBody();

                log.info("response from auth server ::: {}", apiResponse);

                return processUpdateRequest(apiResponse, savingsAccount.get());
            } else {
                throw new CustomException("no savings account for this user",
                        HttpStatus.UNPROCESSABLE_ENTITY);
            }

        } catch (Exception exception) {
            throw new CustomException(exception.getMessage(), exception,
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    private UpdateAccountResponse processUpdateRequest(
            UpdateAccountResponse response, SavingsAccount savingsAccount
    ) {

        if (response != null) {
            if (response.isStatus()) {
                //save savings account
                saveProcessedUpdatedRequest(response, savingsAccount);
                return response;
            }
            throw new CustomException(response.getMessage(),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
            throw new CustomException("could not process request",
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    public void saveProcessedUpdatedRequest(
            UpdateAccountResponse updateAccountResponse,
            SavingsAccount savingsAccount
    ) {
        savingsAccount.setUsername(updateAccountResponse.getData().getUsername());

        savingsAccountRepository.save(savingsAccount);
    }

    /**
     * calls the notification service to send notification.
     *
     * @param emailNotificationRequest email request
     */
    private void sendNotification(
            EmailNotificationRequest emailNotificationRequest
    ) {
        //add token to header.
        HttpHeaders headers = headers();

        HttpEntity<EmailNotificationRequest> entity = new HttpEntity<>(
                emailNotificationRequest, headers);
        UriComponents builder = UriComponentsBuilder
                .fromHttpUrl(configUtils.getNotificationUrl()).build();

        restTemplate.postForEntity(builder.toUriString(), entity, TransactionResponse.class);
    }

    private HttpHeaders basicAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, configUtils.getBasicAuth());

        return headers;
    }

    private HttpHeaders headerWithToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, token);

        return headers;
    }

    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        return headers;
    }

    /**
     * ping url every 5min to keep alive
     */
    @Async
    @Scheduled(fixedRate = 5000)
    public void health() {
        try {
            CompletableFuture.runAsync(() ->
                    restTemplate.getForObject(accountServiceUrl, Object.class));
        } catch (Exception e) {
            log.error("caught an exception :::", e);
        }
    }
}
