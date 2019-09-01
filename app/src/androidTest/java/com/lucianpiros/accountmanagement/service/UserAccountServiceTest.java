package com.lucianpiros.accountmanagement.service;

import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.lucianpiros.accountmanagement.aidl.IUserAccountService;
import com.lucianpiros.accountmanagement.aidl.Login;
import com.lucianpiros.accountmanagement.aidl.Me;
import com.lucianpiros.accountmanagement.aidl.Signup;
import com.lucianpiros.accountmanagement.util.Utility;

import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeoutException;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.ServiceTestRule;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class UserAccountServiceTest {
    private static String EMAIL = "@email.com";
    private static String PASSWORD = "password";
    private static String UPDATE_NAME = "updatename";
    private static String LOCATION = "location";
    private static String BIRTHDATE = "2000-01-01";


    @Rule
    public final ServiceTestRule serviceRule = new ServiceTestRule();

    @Test
    public void testSignupWithService() throws TimeoutException, RemoteException {

        IBinder binder = serviceRule.bindService(new Intent(ApplicationProvider.getApplicationContext(), UserAccountService.class));
        IUserAccountService iUserAccountService = IUserAccountService.Stub.asInterface(binder);
        assertNotNull(iUserAccountService);

        String name = Utility.randomName();
        Signup signUp = new Signup(name, name + EMAIL, PASSWORD, PASSWORD);

        assertThat(iUserAccountService.signUp(signUp), equalTo(true));
    }

    @Test
    public void testLoginWithService() throws TimeoutException, RemoteException {

        IBinder binder = serviceRule.bindService(new Intent(ApplicationProvider.getApplicationContext(), UserAccountService.class));
        IUserAccountService iUserAccountService = IUserAccountService.Stub.asInterface(binder);
        assertNotNull(iUserAccountService);

        String name = Utility.randomName();
        Signup signUp = new Signup(name, name + EMAIL, PASSWORD, PASSWORD);

        assertThat(iUserAccountService.signUp(signUp), equalTo(true));

        Login login = new Login(name + EMAIL, PASSWORD);
        assertThat(iUserAccountService.login(login), equalTo(true));
    }

    @Test
    public void testInvalidLoginWithService() throws TimeoutException, RemoteException {

        IBinder binder = serviceRule.bindService(new Intent(ApplicationProvider.getApplicationContext(), UserAccountService.class));
        IUserAccountService iUserAccountService = IUserAccountService.Stub.asInterface(binder);
        assertNotNull(iUserAccountService);

        String name = Utility.randomName();

        Login login = new Login(name + EMAIL, PASSWORD);
        assertThat(iUserAccountService.login(login), equalTo(false));
    }

    @Test
    public void testGetUserWithService() throws TimeoutException, RemoteException {

        IBinder binder = serviceRule.bindService(new Intent(ApplicationProvider.getApplicationContext(), UserAccountService.class));
        IUserAccountService iUserAccountService = IUserAccountService.Stub.asInterface(binder);
        assertNotNull(iUserAccountService);

        String name = Utility.randomName();
        Signup signUp = new Signup(name, name + EMAIL, PASSWORD, PASSWORD);

        assertThat(iUserAccountService.signUp(signUp), equalTo(true));

        Login login = new Login(name + EMAIL, PASSWORD);
        assertThat(iUserAccountService.login(login), equalTo(true));

        Me me = iUserAccountService.getUserInfo();
        assertThat(me.getName(), equalTo(name));
        assertThat(me.getLocation(), equalTo(null));
        assertThat(me.getBirthdate(), equalTo(null));
    }

    @Test
    public void testInvalidGetUserWithService() throws TimeoutException, RemoteException {

        IBinder binder = serviceRule.bindService(new Intent(ApplicationProvider.getApplicationContext(), UserAccountService.class));
        IUserAccountService iUserAccountService = IUserAccountService.Stub.asInterface(binder);
        assertNotNull(iUserAccountService);

        String name = Utility.randomName();

        Login login = new Login(name + EMAIL, PASSWORD);
        assertThat(iUserAccountService.login(login), equalTo(false));

        Me me = iUserAccountService.getUserInfo();
        assertThat(me, equalTo(null));
    }

    @Test
    public void testUpdateUserWithService() throws TimeoutException, RemoteException {

        IBinder binder = serviceRule.bindService(new Intent(ApplicationProvider.getApplicationContext(), UserAccountService.class));
        IUserAccountService iUserAccountService = IUserAccountService.Stub.asInterface(binder);
        assertNotNull(iUserAccountService);

        String name = Utility.randomName();
        Signup signUp = new Signup(name, name + EMAIL, PASSWORD, PASSWORD);

        assertThat(iUserAccountService.signUp(signUp), equalTo(true));

        Login login = new Login(name + EMAIL, PASSWORD);
        assertThat(iUserAccountService.login(login), equalTo(true));

        Me me = iUserAccountService.getUserInfo();
        assertThat(me.getName(), equalTo(name));
        assertThat(me.getLocation(), equalTo(null));
        assertThat(me.getBirthdate(), equalTo(null));

        Me updatedMe = new Me(UPDATE_NAME, LOCATION, BIRTHDATE);
        iUserAccountService.updateUserInfo(updatedMe);

        Me readUpdateMe = iUserAccountService.getUserInfo();
        assertThat(readUpdateMe.getName(), equalTo(UPDATE_NAME));
        assertThat(readUpdateMe.getLocation(), equalTo(LOCATION));
        assertThat(readUpdateMe.getBirthdate(), equalTo(BIRTHDATE));
    }
}