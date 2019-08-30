package com.lucianpiros.accountmanagement.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.lucianpiros.accountmanagement.aidl.IUserAccountService;
import com.lucianpiros.accountmanagement.aidl.Login;
import com.lucianpiros.accountmanagement.aidl.Me;
import com.lucianpiros.accountmanagement.aidl.Signup;

import androidx.annotation.Nullable;

/**
 * IPC Service
 */
public class UserAccountService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private final IUserAccountService.Stub binder = new IUserAccountService.Stub() {
        @Override
        public boolean login(Login loginInfo) throws RemoteException {
            return false;
        }

        @Override
        public boolean signUp(Signup signUpInfo) throws RemoteException {
            return false;
        }

        @Override
        public Me getUserInfo() throws RemoteException {
            return null;
        }

        @Override
        public boolean updateUserInfo(Me me) throws RemoteException {
            return false;
        }

        @Override
        public String getErrorMessage() throws RemoteException {
            return null;
        }
    };
}
