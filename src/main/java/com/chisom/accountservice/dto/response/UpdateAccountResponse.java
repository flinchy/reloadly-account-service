package com.chisom.accountservice.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
public class UpdateAccountResponse {
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
