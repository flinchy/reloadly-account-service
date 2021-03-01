package com.chisom.accountservice.controller;

import com.chisom.accountservice.dto.request.AccountRegistrationRequest;
import com.chisom.accountservice.dto.request.LoginRequest;
import com.chisom.accountservice.dto.request.UpdateAccountRequest;
import com.chisom.accountservice.dto.response.AccountRegisterResponse;
import com.chisom.accountservice.dto.response.LoginResponse;
import com.chisom.accountservice.dto.response.TransactionResponse;
import com.chisom.accountservice.dto.response.UpdateAccountResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.concurrent.CompletableFuture;

/**
 * @author Chisom.Iwowo
 */
@CrossOrigin
@RestController
@RequestMapping("/account")
public interface AccountController {

    /**
     * endpoint to register a new account
     *
     * @param accountRegistrationRequest registration request
     * @return AccountRegisterResponse
     */
    @ApiOperation(
            value = "${api.register.description}")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    CompletableFuture<AccountRegisterResponse> register(
            @Valid @RequestBody final AccountRegistrationRequest accountRegistrationRequest);

    /**
     * login endpoint
     *
     * @param loginRequest login request
     * @return LoginResponse
     */
    @ApiOperation(
            value = "${api.login.description}",
            notes = "${api.login.notes}")
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    LoginResponse login(@Valid @RequestBody LoginRequest loginRequest);

    /**
     * endpoint to deposit money into savings account
     *
     * @param amount    amount
     * @param principal logged in user
     * @return String
     */
    @ApiOperation(
            value = "${api.deposit.description}",
            notes = "${api.deposit.notes}")
    @PostMapping("/deposit")
    @ResponseStatus(HttpStatus.OK)
    TransactionResponse deposit(
            @RequestParam final double amount,
            @ApiIgnore Principal principal, HttpServletRequest request);

    /**
     * endpoint to withdraw money.
     *
     * @param amount    amount
     * @param principal logged in user
     * @return String
     */
    @ApiOperation(
            value = "${api.withdraw.description}",
            notes = "${api.withdraw.notes}")
    @PostMapping("withdraw")
    @ResponseStatus(HttpStatus.OK)
    TransactionResponse withdraw(
            @RequestParam final double amount,
            @ApiIgnore Principal principal, HttpServletRequest request);

    /**
     * update account details
     *
     * @param updateAccountRequest update request
     * @return Object
     */
    @ApiOperation(
            value = "${api.update-account.description}",
            notes = "${api.update-account.notes}")
    @PostMapping("update")
    @ResponseStatus(HttpStatus.CREATED)
    UpdateAccountResponse updateSavingsAccount(
            @Valid @RequestBody final UpdateAccountRequest updateAccountRequest,
            @ApiIgnore Principal principal);
}

