package com.chisom.accountservice.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Chisom.Iwowo
 */
@Getter
@Setter
@ToString
public class AccountRegistrationPayload {
    private Long userId;
    private String username;
    private String fullName;
}