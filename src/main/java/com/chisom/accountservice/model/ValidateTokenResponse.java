package com.chisom.accountservice.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ValidateTokenResponse {
    private String user_name;
    private List<String> scope;
    private boolean active;
    private Long exp;
    private List<String> authorities;
    private String jti;
    private String client_id;
}
