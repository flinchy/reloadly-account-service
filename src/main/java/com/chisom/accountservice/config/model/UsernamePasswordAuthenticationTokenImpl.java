package com.chisom.accountservice.config.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Objects;

@Getter
@Setter
public class UsernamePasswordAuthenticationTokenImpl extends UsernamePasswordAuthenticationToken {

    private String token;

    public UsernamePasswordAuthenticationTokenImpl(String token) {
        super(null, null);
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UsernamePasswordAuthenticationTokenImpl that = (UsernamePasswordAuthenticationTokenImpl) o;
        return Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), token);
    }
}
