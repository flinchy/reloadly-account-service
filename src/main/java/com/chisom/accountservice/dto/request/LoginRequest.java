package com.chisom.accountservice.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author Chisom.Iwowo
 */
@Getter
@Setter
public class LoginRequest {

    @Email(message = "please enter a valid email")
    @NotBlank(message = "please enter your email")
    private String username;

    @NotBlank(message = "please enter your password")
    private String password;
}