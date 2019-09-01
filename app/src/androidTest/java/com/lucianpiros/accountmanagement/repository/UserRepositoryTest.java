package com.lucianpiros.accountmanagement.repository;

import android.content.Context;

import com.lucianpiros.accountmanagement.aidl.Login;
import com.lucianpiros.accountmanagement.aidl.Me;
import com.lucianpiros.accountmanagement.aidl.Signup;
import com.lucianpiros.accountmanagement.util.Utility;

import org.junit.Before;
import org.junit.Test;

import androidx.test.core.app.ApplicationProvider;

import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserRepositoryTest {
    private UserRepository userRepository;

    private static String EMAIL = "@email.com";
    private static String PASSWORD = "password";

    private static String UPDATE_NAME = "updatename";
    private static String LOCATION = "location";
    private static String BIRTHDATE = "2000-01-01";

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        userRepository = UserRepository.getUserRepository(context);
    }

    @Test
    public void testSignUp() {
        String name = Utility.randomName();

        Signup signup = new Signup(name, name + EMAIL, PASSWORD, PASSWORD);
        try {
            assertThat(userRepository.signUp(signup), equalTo(true));
        } catch(Exception e) {
            fail();
        }
    }

    @Test
    public void testLogin() {
        String name = Utility.randomName();

        Signup signup = new Signup(name, name + EMAIL, PASSWORD, PASSWORD);
        try {
            assertThat(userRepository.signUp(signup), equalTo(true));
        }catch(Exception e) {
            fail();
        }

        Login login = new Login(name + EMAIL, PASSWORD);
        try {
            assertThat(userRepository.login(login), equalTo(true));
        } catch(Exception e) {
            fail();
        }
    }

    @Test
    public void testInvalidLogin() {
        String name = Utility.randomName();

        Login login = new Login(name + EMAIL, PASSWORD);
        try {
            assertThat(userRepository.login(login), equalTo(false));
        } catch(Exception e) {
            fail();
        }
    }

    @Test
    public void testGetUserInfo() {
        String name = Utility.randomName();

        Signup signup = new Signup(name, name + EMAIL, PASSWORD, PASSWORD);
        try {
            assertThat(userRepository.signUp(signup), equalTo(true));
        } catch(Exception e) {
            fail();
        }

        Login login = new Login(name + EMAIL, PASSWORD);
        try {
            assertThat(userRepository.login(login), equalTo(true));
        } catch(Exception e) {
            fail();
        }

        try {
            Me me = userRepository.getUserInfo();
            assertThat(me.getName(), equalTo(name));
            assertThat(me.getLocation(), equalTo(null));
            assertThat(me.getBirthdate(), equalTo(null));
        } catch(Exception e) {
            fail();
        }
    }

    @Test
    public void testInvalidGetUserInfo() {
        String name = Utility.randomName();

        Login login = new Login(name + EMAIL, PASSWORD);
        try {
            assertThat(userRepository.login(login), equalTo(false));
        } catch(Exception e) {
            fail();
        }

        try {
            Me me = userRepository.getUserInfo();
            assertThat(me, equalTo(null));
        } catch(Exception e) {
            fail();
        }
    }

    @Test
    public void testUpdateUserInfo() {
        String name = Utility.randomName();

        Signup signup = new Signup(name, name + EMAIL, PASSWORD, PASSWORD);
        try {
            assertThat(userRepository.signUp(signup), equalTo(true));
        } catch(Exception e) {
            fail();
        }

        Login login = new Login(name + EMAIL, PASSWORD);
        try {
            assertThat(userRepository.login(login), equalTo(true));
        } catch(Exception e) {
            fail();
        }

        try {
            Me me = userRepository.getUserInfo();
            assertThat(me.getName(), equalTo(name));
            assertThat(me.getLocation(), equalTo(null));
            assertThat(me.getBirthdate(), equalTo(null));

            Me updatedMe = new Me(UPDATE_NAME, LOCATION, BIRTHDATE);
            userRepository.updateUserInfo(updatedMe);

            Me readUpdateMe = userRepository.getUserInfo();
            assertThat(readUpdateMe.getName(), equalTo(UPDATE_NAME));
            assertThat(readUpdateMe.getLocation(), equalTo(LOCATION));
            assertThat(readUpdateMe.getBirthdate(), equalTo(BIRTHDATE));
        } catch(Exception e) {
            fail();
        }
    }
}