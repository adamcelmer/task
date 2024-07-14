package com.magnoliacms.security;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHashCalculator {

    public static final String ERROR_MSG = "Could not calcaulate password hash";

    public static PasswordHash calculateHash(String password) {
        String salt = BCrypt.gensalt();
        return calculateHash(password, salt);
    };

    public static PasswordHash calculateHash(String password, String salt) {
        String hash = BCrypt.hashpw(password, salt);
        return new PasswordHash(hash, salt);
    };
}

