package com.lucianpiros.accountmanagement.model;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.lucianpiros.accountmanagement.aidl.IUserAccountService;
import com.lucianpiros.accountmanagement.aidl.Login;
import com.lucianpiros.accountmanagement.aidl.Me;
import com.lucianpiros.accountmanagement.aidl.Signup;
import com.lucianpiros.accountmanagement.service.UserAccountService;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

public class UserAccountModel extends AndroidViewModel {
    private final MutableLiveData<Boolean> logedIn = new MutableLiveData<Boolean>();
    private final MutableLiveData<Boolean> updated = new MutableLiveData<Boolean>();
    private final MutableLiveData<Me> meInfo = new MutableLiveData<Me>();

    private static UserAccountModel userAccountModel = null;
    public static UserAccountModel getUserAccountModel(FragmentActivity activity) {
        if(userAccountModel == null) {
            userAccountModel = ViewModelProviders.of(activity).get(UserAccountModel.class);
        }
        return userAccountModel;
    }

    private IUserAccountService userAccountService = null;

    public UserAccountModel(@NonNull Application application) {
        super(application);
    }

    public void login(String email, String password) {
        Intent intent = new Intent(getApplication().getApplicationContext(), UserAccountService.class);
        intent.setAction(IUserAccountService.class.getName());
        getApplication().getApplicationContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);

        Login login = new Login(email, password);
        new loginAsyncTask(getApplication().getApplicationContext()).execute(login);
    }

    public void signup(String name, String email, String password, String password2) {
        Intent intent = new Intent(getApplication().getApplicationContext(), UserAccountService.class);
        intent.setAction(IUserAccountService.class.getName());
        getApplication().getApplicationContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);

        Signup signup = new Signup(name, email, password, password2);
        new signupAsyncTask(getApplication().getApplicationContext()).execute(signup);
    }

    public void update(String name, String location, String birthdate) {

        Me me = new Me(name, location, birthdate);
        new updateAsyncTask(getApplication().getApplicationContext()).execute(me);
    }

    public LiveData<Boolean> loggedIn() {
        return logedIn;
    }

    public LiveData<Boolean> updateResult() {
        return updated;
    }

    public LiveData<Me> userInfo() {
        return meInfo;
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
            new readInfoAsyncTask(context).execute();
        }

        @Override
        protected Void doInBackground(final Login... params) {
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
            new readInfoAsyncTask(context).execute();
        }

        @Override
        protected Void doInBackground(final Signup... params) {
            Signup signup = params[0];
            try {
                logedInWT = userAccountService.signUp(signup);
            } catch(Exception e) {
                logedInWT = false;
            }
            return null;
        }
    }

    private class readInfoAsyncTask extends android.os.AsyncTask<Void, Void, Void> {
        private Context context;
        private Me meWT;

        readInfoAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPostExecute(Void result) {
            meInfo.setValue(meWT);
        }

        @Override
        protected Void doInBackground(final Void... params) {
            try {
                meWT = userAccountService.getUserInfo();
            } catch(Exception e) {
                meWT = null;
            }
            return null;
        }
    }

    private class updateAsyncTask extends android.os.AsyncTask<Me, Void, Void> {
        private Context context;
        private boolean updatedWT;

        updateAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPostExecute(Void result) {
            updated.setValue(updatedWT);
            new readInfoAsyncTask(context).execute();
        }

        @Override
        protected Void doInBackground(final Me... params) {
            try {
                Me me = params[0];
                updatedWT = userAccountService.updateUserInfo(me);
            } catch(Exception e) {
                updatedWT = false;
            }
            return null;
        }
    }
}
