package com.chisom.accountservice.utils;

import java.security.SecureRandom;

/**
 * @author Chisom.Iwowo
 */
public final class AccountServiceUtils {

    private AccountServiceUtils() {
    }

    /**
     * generates random 10-digits account numbers
     *
     * @return Integer
     */
    public static Integer generateAccountNumber() {
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.nextInt(999999) + 1000000000;
    }
}
