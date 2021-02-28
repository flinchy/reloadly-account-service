package com.chisom.accountservice.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

/**
 * @author Chisom.Iwowo
 */
@Getter
@Setter
public class TransactionResponse {
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

}
