package com.chisom.accountservice.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author Chisom.Iwowo
 */
@Getter
@Setter
@Builder
public class TransactionRequest {

    @NotBlank(message = "please enter the narration")
    private String narration;

    @NotNull(message = "please enter the amount")
    private BigDecimal amount;

    @NotNull(message = "please enter the account balance")
    private BigDecimal accountBalance;

    @NotNull(message = "please enter the user id")
    private Long userId;

    @NotBlank(message = "please enter your email as username")
    private String username;
}
