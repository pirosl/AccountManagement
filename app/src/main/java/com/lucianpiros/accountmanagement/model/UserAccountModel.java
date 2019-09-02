package com.lucianpiros.accountmanagement.model;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.lucianpiros.accountmanagement.aidl.IUserAccountService;
import com.lucianpiros.accountmanagement.aidl.Login;
import com.lucianpiros.accountmanagement.aidl.Signup;
import com.lucianpiros.accountmanagement.service.UserAccountService;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class UserAccountModel extends AndroidViewModel {
    private final MutableLiveData<Boolean> logedIn = new MutableLiveData<Boolean>();

    private IUserAccountService userAccountService = null;

    public UserAccountModel(@NonNull Application application) {
        super(application);
    }

    public void login(String email, String password) {
        Login login = new Login(email, password);
        new loginAsyncTask(getApplication().getApplicationContext()).execute(login);
    }

    public void signup(String name, String email, String password, String password2) {
        Signup signup = new Signup(name, email, password, password2);
        new signupAsyncTask(getApplication().getApplicationContext()).execute(signup);
    }

    public LiveData<Boolean> loggedIn() {
        return logedIn;
    }

    private ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            userAccountService = IUserAccountService.Stub.asInterface(service);
        }

        public void onServiceDisconnected(ComponentName className) {
            userAccountService = null;
        }
    };

    private class loginAsyncTask extends android.os.AsyncTask<Login, Void, Void> {
        private Context context;
        private boolean logedInWT;

        loginAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPostExecute(Void result) {
            logedIn.setValue(logedInWT);
        }

        @Override
        protected Void doInBackground(final Login... params) {

            Intent intent = new Intent(context, UserAccountService.class);
            intent.setAction(IUserAccountService.class.getName());
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE);

            Login login = params[0];
            try {
                logedInWT = userAccountService.login(login);
            } catch(Exception e) {
                logedInWT = false;
            }
            return null;
        }
    }

    private class signupAsyncTask extends android.os.AsyncTask<Signup, Void, Void> {
        private Context context;
        private boolean logedInWT;

        signupAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPostExecute(Void result) {
            logedIn.setValue(logedInWT);
        }

        @Override
        protected Void doInBackground(final Signup... params) {

            Intent intent = new Intent(context, UserAccountService.class);
            intent.setAction(IUserAccountService.class.getName());
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE);

            Signup signup = params[0];
            try {
                logedInWT = userAccountService.signUp(signup);
            } catch(Exception e) {
                logedInWT = false;
            }
            return null;
        }
    }
}
