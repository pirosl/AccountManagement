package com.lucianpiros.accountmanagement.restapi;

import android.util.Log;

import com.lucianpiros.accountmanagement.restapi.pojo.SerializableLogin;
import com.lucianpiros.accountmanagement.restapi.pojo.SerializableSignup;
import com.lucianpiros.accountmanagement.util.Utility;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class RESTClientTest {

    private final static String TAG = RESTClient.class.getCanonicalName();
    private static String EMAIL = "@email.com";
    private static String PASSWORD = "password";
    private static String INVALID_PASSWORD = "p_______";

    private class RestTest implements RESTClient.Callback {
        protected CountDownLatch latch;

        public RestTest(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void onResponse(String message) {
            // default implementation
            fail();
            latch.countDown();
        }

        @Override
        public void onFailure(int code, String error) {
            // default implementation
            fail();
            latch.countDown();
        }

        public void run() {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class PositiveTest extends RestTest {
        private String expectedMessage;

        public PositiveTest(CountDownLatch latch, String expectedMessage) {
            super(latch);
            this.expectedMessage = expectedMessage;
        }

        @Override
        public void onResponse(String message) {
            // Override positive run
            latch.countDown();
            assertThat(message, is(expectedMessage));
        }
    }

    public class NegativeTest extends RestTest {
        private String expectedMessage;

        public NegativeTest(CountDownLatch latch, String expectedMessage) {
            super(latch);
            this.expectedMessage = expectedMessage;
        }

        @Override
        public void onFailure(int code, String error) {
            // override failure path
            Log.d(TAG, error);
            assertThat(error, is(expectedMessage));
            latch.countDown();
        }
    }

    @Test
    public void testRandomSignUp() {
        // TODO: this test might fail as the random generated user might be already registered
        //       test could be fixed if an API for removing the user would exit

        String name = Utility.randomName();
        SerializableSignup serializableSignup = new SerializableSignup(name, name + EMAIL, PASSWORD, PASSWORD);

        PositiveTest pt = new PositiveTest(new CountDownLatch(1), "ok");
        RESTClient.getInstance().signUp(serializableSignup, pt);
        pt.run();
    }

    @Test
    public void testInvalidInputSignUp() {
        SerializableSignup serializableSignup = new SerializableSignup("", EMAIL, PASSWORD, PASSWORD);

        NegativeTest nt = new NegativeTest(new CountDownLatch(1), "Invalid input");
        RESTClient.getInstance().signUp(serializableSignup, nt);
        nt.run();
    }

    @Test
    public void testDuplicateSignUp() {
        // TODO: this test might fail as the random generated user might be already registered
        //       test could be fixed if an API for removing the user would exit
        String name = Utility.randomName();
        final SerializableSignup serializableSignup = new SerializableSignup(name, name + EMAIL, PASSWORD, PASSWORD);

        PositiveTest pt = new PositiveTest(new CountDownLatch(1), "ok");
        RESTClient.getInstance().signUp(serializableSignup, pt);
        pt.run();

        NegativeTest nt = new NegativeTest(new CountDownLatch(1), "User with that email already exists");
        RESTClient.getInstance().signUp(serializableSignup, nt);
        nt.run();
    }

    @Test
    public void testValidLogin() {
        // TODO: this test might fail as the random generated user might be already registered
        //       test could be fixed if an API for removing the user would exit

        String name = Utility.randomName();
        SerializableSignup serializableSignup = new SerializableSignup(name, name + EMAIL, PASSWORD, PASSWORD);

        PositiveTest pts = new PositiveTest(new CountDownLatch(1), "ok");
        RESTClient.getInstance().signUp(serializableSignup, pts);
        pts.run();

        SerializableLogin serializableLogin = new SerializableLogin(name + EMAIL, PASSWORD);
        PositiveTest ptl = new PositiveTest(new CountDownLatch(1), "ok");
        RESTClient.getInstance().login(serializableLogin, ptl);
        ptl.run();
    }

    @Test
    public void testLoginInvaliUserPassword() {
        // TODO: this test might fail as the random generated user might be already registered
        //       test could be fixed if an API for removing the user would exit

        String name = Utility.randomName();
        SerializableSignup serializableSignup = new SerializableSignup(name, name + EMAIL, PASSWORD, PASSWORD);

        PositiveTest pts = new PositiveTest(new CountDownLatch(1), "ok");
        RESTClient.getInstance().signUp(serializableSignup, pts);
        pts.run();

        SerializableLogin serializableLogin = new SerializableLogin(name + EMAIL, INVALID_PASSWORD);
        NegativeTest ntl = new NegativeTest(new CountDownLatch(1), "Invalid User Password");
        RESTClient.getInstance().login(serializableLogin, ntl);
        ntl.run();
    }

    @Test
    public void testLoginUserNotFound() {
        // TODO: this test might fail as the random generated user might be already registered

        String name = Utility.randomName();
        SerializableLogin serializableLogin = new SerializableLogin(name + EMAIL, INVALID_PASSWORD);
        NegativeTest ntl = new NegativeTest(new CountDownLatch(1), "User not found with that email");
        RESTClient.getInstance().login(serializableLogin, ntl);
        ntl.run();
    }
}