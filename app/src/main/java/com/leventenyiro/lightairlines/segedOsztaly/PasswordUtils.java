package com.leventenyiro.lightairlines.segedOsztaly;

import android.util.Base64;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtils {
    private static final int iterations = 10000, keyLength = 256;
    private static final Random rnd = new SecureRandom();
    private static final String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String getSalt(int length) {
        StringBuilder returnValue = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            returnValue.append(alphabet.charAt(rnd.nextInt(alphabet.length())));
        }
        return new String(returnValue);
    }

    public static byte[] hash(char[] password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return secretKeyFactory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Hiba a jelszótitkosítás közben!");
        } finally {
            spec.clearPassword();
        }
    }

    public static String generateSecurePassword(String password, String salt) {
        byte[] securePassword = hash(password.toCharArray(), salt.getBytes());
        return android.util.Base64.encodeToString(securePassword, Base64.NO_WRAP);
    }

    public static boolean verifyUserPassword(String providedPassword, String securedPassword, String salt) {
        String newSecurePassword = generateSecurePassword(providedPassword, salt);
        boolean returnValue = newSecurePassword.equalsIgnoreCase(securedPassword);
        return returnValue;
    }
}
