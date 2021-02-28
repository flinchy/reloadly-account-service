package com.chisom.accountservice.controller;

import com.chisom.accountservice.dto.request.AccountRegistrationRequest;
import com.chisom.accountservice.dto.request.LoginRequest;
import com.chisom.accountservice.dto.request.UpdateAccountRequest;
import com.chisom.accountservice.dto.response.AccountRegisterResponse;
import com.chisom.accountservice.dto.response.LoginResponse;
import com.chisom.accountservice.dto.response.TransactionResponse;
import com.chisom.accountservice.dto.response.UpdateAccountResponse;
import com.chisom.accountservice.service.AccountService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.concurrent.CompletableFuture;

/**
 * @author Chisom.Iwowo
 */
@Service
public class AccountControllerFacade implements AccountController {

    private final AccountService accountService;

    public AccountControllerFacade(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * @param accountRegistrationRequest registration request
     * @return LoginResponse
     */
    @Override
    public CompletableFuture<AccountRegisterResponse> register(
            @Valid AccountRegistrationRequest accountRegistrationRequest
    ) {

        return CompletableFuture.supplyAsync(() -> accountService.registerNewAccount(accountRegistrationRequest)).thenApply(x -> x);
    }

    /**
     * @param loginRequest login request
     * @return LoginResponse
     */
    @Override
    public LoginResponse login(@Valid LoginRequest loginRequest) {
        return accountService.login(loginRequest);
    }

    /**
     * @param amount    amount
     * @param principal logged in user
     * @return String
     */
    @Override
    public TransactionResponse deposit(
            double amount, Principal principal, HttpServletRequest request
    ) {
        return accountService.deposit(amount, principal, request);
    }

    /**
     * endpoint to withdraw money.
     *
     * @param amount    amount
     * @param principal logged in user
     * @return String
     */
    @Override
    public TransactionResponse withdraw(
            double amount, Principal principal, HttpServletRequest request
    ) {
        return accountService.withdraw(amount, principal, request);
    }

    /**
     * update account details
     *
     * @param updateAccountRequest update request
     * @param principal            logged in user
     * @return Object
     */
    @Override
    public UpdateAccountResponse updateSavingsAccount(
            UpdateAccountRequest updateAccountRequest, Principal principal
    ) {
        return accountService.updateAccountDetails(updateAccountRequest, principal);
    }
}
