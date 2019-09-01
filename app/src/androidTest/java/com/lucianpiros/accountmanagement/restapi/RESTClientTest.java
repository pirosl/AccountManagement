package com.lucianpiros.accountmanagement.restapi;

import android.util.Log;

import com.lucianpiros.accountmanagement.restapi.pojo.SerializableSignup;

import org.junit.Test;

import java.security.SecureRandom;
import java.util.concurrent.CountDownLatch;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class RESTClientTest {

    private final static String TAG = RESTClient.class.getCanonicalName();
    private static String EMAIL = "@email.com";
    private static String PASSWORD = "password";

    private static SecureRandom random = new SecureRandom();

    /**
     * Generates and return a random name
     * @return - return generated name
     */
    private String randomName() {
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

    /**
     * Positive test - will be called in test functions
     *
     * @param serializableSignup - signup parameters
     * @param expectedMessage - expected message
     */
    private void positiveTest(SerializableSignup serializableSignup, final String expectedMessage) {
        final CountDownLatch latch = new CountDownLatch(1);

        RESTClient.getInstance().signUp(serializableSignup, new RESTClient.Callback() {
            @Override
            public void onResponse(String message) {
                latch.countDown();
                assertThat(message, is(expectedMessage));
            }

            @Override
            public void onFailure(int code, String error) {
                // this should never happen
                Log.d(TAG, error);
                fail();
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Negative test - will be called in test functions
     *
     * @param serializableSignup - signup parameters
     * @param expectedMessage - expected message
     */
    private void negativeTest(SerializableSignup serializableSignup, final String expectedMessage) {
        final CountDownLatch latch = new CountDownLatch(1);

        RESTClient.getInstance().signUp(serializableSignup, new RESTClient.Callback() {
            @Override
            public void onResponse(String message) {
                // this should never happen
                latch.countDown();
                fail();
            }

            @Override
            public void onFailure(int code, String error) {
                // this is the expected path
                Log.d(TAG, error);
                assertThat(error, is(expectedMessage));
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRandomSignUp() {
        // TODO: this test might fail as the random generated user might be already registered
        //       test could be fixed if an API for removing the user would exit

        String name = randomName();
        SerializableSignup serializableSignup = new SerializableSignup(name, name + EMAIL, PASSWORD, PASSWORD);

        positiveTest(serializableSignup, "ok");
    }

    @Test
    public void testInvalidInputSignUp() {
        SerializableSignup serializableSignup = new SerializableSignup("", EMAIL, PASSWORD, PASSWORD);
        negativeTest(serializableSignup, "Invalid input");
    }

    @Test
    public void testDuplicateSignUp() {
        // TODO: this test might fail as the random generated user might be already registered
        //       test could be fixed if an API for removing the user would exit
        String name = randomName();
        final SerializableSignup serializableSignup = new SerializableSignup(name, name + EMAIL, PASSWORD, PASSWORD);
        positiveTest(serializableSignup, "ok");
        negativeTest(serializableSignup, "User with that email already exists");
    }
}