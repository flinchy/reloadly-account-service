package com.chisom.accountservice.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class AccountRegisterResponse {

    /**
     * timestamp.
     */
    private ZonedDateTime timestamp;

    /**
     * message.
     */
    private String message;

    /**
     * status.
     */
    private boolean status;

    /**
     * data.
     */
    private AccountRegistrationPayload data;
}
