package com.lucianpiros.accountmanagement.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.lucianpiros.accountmanagement.aidl.IUserAccountService;
import com.lucianpiros.accountmanagement.aidl.Login;
import com.lucianpiros.accountmanagement.aidl.Me;
import com.lucianpiros.accountmanagement.aidl.Signup;
import com.lucianpiros.accountmanagement.repository.UserRepository;

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
            try {
                return UserRepository.getUserRepository(getApplicationContext()).login(loginInfo);
            } catch(Exception e) {
                throw new RemoteException(e.getMessage());
            }
        }

        @Override
        public boolean signUp(Signup signUpInfo) throws RemoteException {
            try {
                return UserRepository.getUserRepository(getApplicationContext()).signUp(signUpInfo);
            } catch(Exception e) {
                throw new RemoteException(e.getMessage());
            }
        }

        @Override
        public Me getUserInfo() throws RemoteException {
            try {
                return UserRepository.getUserRepository(getApplicationContext()).getUserInfo();
            } catch(Exception e) {
                throw new RemoteException(e.getMessage());
            }
        }

        @Override
        public boolean updateUserInfo(Me me) throws RemoteException {
            try {
                return UserRepository.getUserRepository(getApplicationContext()).updateUserInfo(me);
            } catch(Exception e) {
                throw new RemoteException(e.getMessage());
            }
        }

        @Override
        public String getErrorMessage() throws RemoteException {
            return UserRepository.getUserRepository(getApplicationContext()).getErrorMessage();
        }
    };
}
