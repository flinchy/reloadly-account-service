package com.chisom.accountservice.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdateAccountRequest {
    @Email(message = "please enter a valid email")
    @NotBlank(message = "please enter a valid email")
    private String username;

    @NotBlank(message = "please enter your fullName")
    private String fullName;

    private String mobile;
}
