package com.lucianpiros.accountmanagement.util;

import java.security.SecureRandom;

/**
 * Utility class - contains methods used accross test suite
 */
public class Utility {

    private static SecureRandom random = new SecureRandom();

    /**
     * Generates and return a random name
     * @return - return generated name
     */
    public static String randomName() {
        final int nameLength = 10;
        String NUMBER = "0123456789";

        StringBuilder sb = new StringBuilder();
        sb.append("n_");
        for (int i = 0; i < nameLength; i++) {

            int rndCharAt = random.nextInt(NUMBER.length());
            sb.append(NUMBER.charAt(rndCharAt));
        }

        return sb.toString();
    }
}
