package com.jspcafe.user.model;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.UUID;

public record User(String id, String email, String nickname, String password) {
    private static final byte[] SALT = "HELLO_WEB".getBytes();
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    public static User create(String email, String nickname, String password) {
        validatePassword(password);
        return new User(UUID.randomUUID().toString(), email, nickname, hashPassword(password));
    }

    public User {
        validateEmail(email);
        validateNickname(nickname);
        validatePassword(password);
    }

    private static void validateEmail(String email) {
        if (email.isEmpty() || email.isBlank()) {
            throw new IllegalArgumentException("Invalid email: " + email);
        }
    }

    private static void validateNickname(String nickname) {
        if (nickname.isEmpty() || nickname.isBlank()) {
            throw new IllegalArgumentException("Invalid nickname: " + nickname);
        }
    }

    private static void validatePassword(String password) {
        if (password.isEmpty() || password.isBlank()) {
            throw new IllegalArgumentException("Invalid password: " + password);
        }
    }

    private static String hashPassword(String password) {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), SALT, ITERATIONS, KEY_LENGTH);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verifyPassword(String password) {
        String hashedPassword = hashPassword(password);
        return this.password.equals(hashedPassword);
    }
}
