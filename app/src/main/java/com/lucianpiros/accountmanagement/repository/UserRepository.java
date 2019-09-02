package com.lucianpiros.accountmanagement.repository;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;

import com.lucianpiros.accountmanagement.aidl.Login;
import com.lucianpiros.accountmanagement.aidl.Me;
import com.lucianpiros.accountmanagement.aidl.Signup;
import com.lucianpiros.accountmanagement.database.PersistenceDataProvider;
import com.lucianpiros.accountmanagement.database.UserDao;
import com.lucianpiros.accountmanagement.database.UserEntity;
import com.lucianpiros.accountmanagement.database.UserRoomDatabase;
import com.lucianpiros.accountmanagement.restapi.RESTClient;
import com.lucianpiros.accountmanagement.restapi.pojo.SerializableLogin;
import com.lucianpiros.accountmanagement.restapi.pojo.SerializableMe;
import com.lucianpiros.accountmanagement.restapi.pojo.SerializableSignup;

import java.util.concurrent.CountDownLatch;

import androidx.room.Room;

/**
 * User repository class. Will be used from UserAccountService.
 * UserRepository will read data from Room database and remote REST API service (and persist data from remote calls)
 * Singleton class.
 */
public class UserRepository implements PersistenceDataProvider  {

    private static volatile UserRepository userRepositoryInstance;
    private Context context;

    // information about current state
    private boolean loggedIn;
    private String loggedInEmail;

    private String errorMessage;
    private int errorCode;
    private final int OK = 0;

    private UserRoomDatabase db;
    private UserDao userDao;

    private static final long HARDTTL = 1000;

    private UserRepository(final Context context) {
        this.context = context;
        db = Room.databaseBuilder(context, UserRoomDatabase.class, "userDatabase").build();
        userDao = db.userDao();
        RESTClient.getInstance().setPersistenceDataProvider(this);
    }

    public static UserRepository getUserRepository(final Context context) {
        if (userRepositoryInstance == null) {
            synchronized (UserRoomDatabase.class) {
                if (userRepositoryInstance == null) {
                    userRepositoryInstance = new UserRepository(context);
                }
            }
        }
        return userRepositoryInstance;
    }

    public boolean login(final Login loginInfo) throws Exception {
        // login to REST API endpoint - if connection login successful store the info locally

        loggedIn = false;
        loggedInEmail = null;

        final CountDownLatch latch = new CountDownLatch(1);
        SerializableLogin serializableLogin = new SerializableLogin(loginInfo.getEmail(), loginInfo.getPassword());
        RESTClient.getInstance().login(serializableLogin, new RESTClient.Callback() {
            @Override
            public void onResponse(String message) {
                errorMessage = message;
                loggedIn = true;
                loggedInEmail = loginInfo.getEmail();

                latch.countDown();
            }

            @Override
            public void onFailure(int code, String error) {
                errorMessage = error;

                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return loggedIn;
    }

    public boolean signUp(final Signup signUpInfo) throws Exception {
        loggedIn = false;
        loggedInEmail = null;

        final CountDownLatch latch = new CountDownLatch(1);
        SerializableSignup serializableSignup = new SerializableSignup(signUpInfo.getName(), signUpInfo.getEmail(), signUpInfo.getPassword(), signUpInfo.getPassword2());
        RESTClient.getInstance().signUp(serializableSignup, new RESTClient.Callback() {
            @Override
            public void onResponse(String message) {
                errorMessage = message;
                loggedIn = true;
                loggedInEmail = signUpInfo.getEmail();

                // store info in local database as well
                UserEntity userEntity = new UserEntity(signUpInfo.getEmail(), signUpInfo.getName(), null, null, System.currentTimeMillis());
                new insertAsyncTask(userDao).execute(userEntity);

                latch.countDown();
            }

            @Override
            public void onFailure(int code, String error) {
                errorMessage = error;

                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return loggedIn;
    }

    public Me getUserInfo() throws Exception {
        // if not logged in return null
        if(!loggedIn) {
            return null;
        }

        // first try to get info from local database
        UserEntity userEntity = userDao.getUser(loggedInEmail);
        Me me = null;
        errorCode = OK;

        // if userEntity is null or data too old - try to read using REST API endpoint
        if(userEntity == null || System.currentTimeMillis() - userEntity.getLastupdate_timestamp() >= HARDTTL) {
            final CountDownLatch latch = new CountDownLatch(1);
            RESTClient.getInstance().retrieveMeDetails(new RESTClient.Callback() {
                @Override
                public void onResponse(String message) {
                    errorMessage = message;
                    latch.countDown();
                }

                @Override
                public void onFailure(int code, String error) {
                    errorMessage = error;
                    errorCode = code;
                    latch.countDown();
                }
            });

            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(errorCode != OK) {
                return null;
            }

            userEntity = userDao.getUser(loggedInEmail);
        }

        // yey data is ok...we can present it
        me = new Me(userEntity.getName(), userEntity.getLocation(), userEntity.getBirthdate());


        return me;
    }

    public boolean updateUserInfo(final Me me) throws Exception {
        if(!loggedIn) {
            return false;
        }

        errorCode = OK;
        final CountDownLatch latch = new CountDownLatch(1);
        SerializableMe serializableMe = new SerializableMe(me.getName(), me.getLocation(), me.getBirthdate());
        RESTClient.getInstance().updateMeDetails(serializableMe, new RESTClient.Callback() {
            @Override
            public void onResponse(String message) {
                errorMessage = message;
                // update local database
                updateUserDetails(loggedInEmail, me.getName(), me.getLocation(), me.getBirthdate());
                latch.countDown();
            }

            @Override
            public void onFailure(int code, String error) {
                errorMessage = error;
                errorCode = code;
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return (errorCode == OK);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void saveUserDetails(String userEmail, String name, String location, String birthdate) {
        UserEntity userEntity = new UserEntity(userEmail, name, location, birthdate, System.currentTimeMillis());
        new insertAsyncTask(userDao).execute(userEntity);
    }

    @Override
    public void updateUserDetails(String userEmail, String name, String location, String birthdate) {
        UserEntity userEntity = new UserEntity(userEmail, name, location, birthdate, System.currentTimeMillis());
        new updateAsyncTask(userDao).execute(userEntity);
    }

    private static class insertAsyncTask extends android.os.AsyncTask<UserEntity, Void, Void> {

        private UserDao asyncTaskDao;

        insertAsyncTask(UserDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final UserEntity... params) {
            try {
                asyncTaskDao.insert(params[0]);
            }catch(SQLiteConstraintException ex) {
                // TODO: handle this error
            }
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<UserEntity, Void, Void> {

        private UserDao asyncTaskDao;

        updateAsyncTask(UserDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final UserEntity... params) {
            UserEntity userEntity = params[0];
            asyncTaskDao.updateUserDetails(userEntity.getEmail(), userEntity.getName(), userEntity.getLocation(), userEntity.getBirthdate(), userEntity.getLastupdate_timestamp());
            return null;
        }
    }
}
