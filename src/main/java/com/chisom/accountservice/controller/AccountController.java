package com.chisom.accountservice.controller;

import com.chisom.accountservice.dto.request.AccountRegistrationRequest;
import com.chisom.accountservice.dto.request.LoginRequest;
import com.chisom.accountservice.dto.request.UpdateAccountRequest;
import com.chisom.accountservice.dto.response.AccountRegisterResponse;
import com.chisom.accountservice.dto.response.LoginResponse;
import com.chisom.accountservice.dto.response.TransactionResponse;
import com.chisom.accountservice.dto.response.UpdateAccountResponse;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/deposit")
    @ResponseStatus(HttpStatus.OK)
    TransactionResponse deposit(
            @RequestParam final double amount,
            @ApiParam(hidden = true)  Principal principal, HttpServletRequest request);

    /**
     * endpoint to withdraw money.
     *
     * @param amount    amount
     * @param principal logged in user
     * @return String
     */
    @PostMapping("withdraw")
    @ResponseStatus(HttpStatus.OK)
    TransactionResponse withdraw(
            @RequestParam final double amount,
            @ApiParam(hidden = true)  Principal principal, HttpServletRequest request);

    /**
     * update account details
     *
     * @param updateAccountRequest update request
     * @return Object
     */
    @PostMapping("update")
    @ResponseStatus(HttpStatus.CREATED)
    UpdateAccountResponse updateSavingsAccount(
            @Valid @RequestBody final UpdateAccountRequest updateAccountRequest,
            @ApiParam(hidden = true)  Principal principal);
}

