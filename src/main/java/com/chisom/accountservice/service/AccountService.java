package com.chisom.accountservice.service;

import com.chisom.accountservice.dto.request.AccountRegistrationRequest;
import com.chisom.accountservice.dto.request.LoginRequest;
import com.chisom.accountservice.dto.request.UpdateAccountRequest;
import com.chisom.accountservice.dto.response.AccountRegisterResponse;
import com.chisom.accountservice.dto.response.LoginResponse;
import com.chisom.accountservice.dto.response.TransactionResponse;
import com.chisom.accountservice.dto.response.UpdateAccountResponse;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * @author Chisom.Iwowo
 */
public interface AccountService {

    /**
     * register for a new account.
     *
     * @param accountRegistrationRequest account registration request
     * @return Object
     */
    AccountRegisterResponse registerNewAccount(
            AccountRegistrationRequest accountRegistrationRequest);

    /**
     * update account details
     *
     * @param updateAccountRequest request
     * @param principal            logged in user
     */
    UpdateAccountResponse updateAccountDetails(
            UpdateAccountRequest updateAccountRequest, Principal principal);

    /**
     * login by calling the authorization server which sends back an access token
     *
     * @param loginRequest login request
     * @return LoginResponse
     */
    LoginResponse login(LoginRequest loginRequest);

    /**
     * deposit money to account.
     *
     * @param amount    amount
     * @param principal LoggedIn user
     */
    TransactionResponse deposit(
            double amount, Principal principal, HttpServletRequest request);

    /**
     * withdraw money from account.
     *
     * @param amount    amount
     * @param principal user
     */
    TransactionResponse withdraw(
            double amount, Principal principal, HttpServletRequest request);

}
