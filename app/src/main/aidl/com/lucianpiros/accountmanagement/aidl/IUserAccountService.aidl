// IUserAccountService.aidl
package com.lucianpiros.accountmanagement.aidl;

import com.lucianpiros.accountmanagement.aidl.Login;
import com.lucianpiros.accountmanagement.aidl.Signup;
import com.lucianpiros.accountmanagement.aidl.Me;

// Persistence service aidl - provide all the interface to data persistence service
interface IUserAccountService {
    // login into the system
    boolean login(in Login loginInfo);

    // sign up to system
    boolean signUp(in Signup signUpInfo);

    // retrieve information about currently logged in user
    Me getUserInfo();

    // update user information
    boolean updateUserInfo(in Me me);

    // return the last error message, if any
    String getErrorMessage();
}
