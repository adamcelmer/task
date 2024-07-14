package com.magnoliacms.security;

public record PasswordHash(String passwordHash, String salt) {
}
