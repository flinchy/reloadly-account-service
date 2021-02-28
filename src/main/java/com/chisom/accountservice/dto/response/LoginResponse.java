package com.chisom.accountservice.dto.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Chisom.Iwowo
 */
@Getter
@Setter
public class LoginResponse {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private String scope;
    private String jti;
}

