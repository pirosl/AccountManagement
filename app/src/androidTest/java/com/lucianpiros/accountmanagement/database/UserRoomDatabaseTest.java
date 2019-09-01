package com.lucianpiros.accountmanagement.database;

import android.content.Context;

import com.lucianpiros.accountmanagement.util.Utility;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class UserRoomDatabaseTest {
    private static final String EMAIL = "email@email.com";
    private static final String EMAILTAG = "@email.com";
    private static final String NAME = "name";
    private static final String UPDATENAME = "updatename";
    private static final String LOCATION = "location";
    private static final String BIRTHDATE = "2000-01-01";

    private UserRoomDatabase db;
    private UserDao userDao;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, UserRoomDatabase.class).build();
        userDao = db.userDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void testWriteUserAndReadAfter() throws Exception {
        UserEntity userEntity = new UserEntity(EMAIL, NAME, LOCATION, BIRTHDATE, System.currentTimeMillis());
        userDao.insert(userEntity);
        UserEntity readUserEntity = userDao.getUser(EMAIL);
        assertThat(readUserEntity.getName(), equalTo(userEntity.getName()));
        assertThat(readUserEntity.getLocation(), equalTo(userEntity.getLocation()));
        assertThat(readUserEntity.getBirthdate(), equalTo(userEntity.getBirthdate()));
    }

    @Test
    public void testNoUser() throws Exception {
        UserEntity readUserEntity = userDao.getUser(Utility.randomName() + EMAILTAG);
        assertThat(readUserEntity, equalTo(null));
    }

    @Test
    public void testDeleteUserAndReadAfter() throws Exception {

        String name = Utility.randomName();
        UserEntity readUserEntity = userDao.getUser(name + EMAILTAG);
        assertThat(readUserEntity, equalTo(null));

        UserEntity userEntity = new UserEntity(name + EMAILTAG, name, LOCATION, BIRTHDATE, System.currentTimeMillis());
        userDao.insert(userEntity);
        readUserEntity = userDao.getUser(name + EMAILTAG);
        assertThat(readUserEntity.getName(), equalTo(userEntity.getName()));
        assertThat(readUserEntity.getLocation(), equalTo(userEntity.getLocation()));
        assertThat(readUserEntity.getBirthdate(), equalTo(userEntity.getBirthdate()));

        userDao.deleteUser(name + EMAILTAG);
        readUserEntity = userDao.getUser(name + EMAILTAG);
        assertThat(readUserEntity, equalTo(null));
    }

    @Test
    public void testUpdateUserAndReadAfter() throws Exception {
        UserEntity userEntity = new UserEntity(EMAIL, NAME, LOCATION, BIRTHDATE, System.currentTimeMillis());
        userDao.insert(userEntity);

        userDao.updateUserDetails(EMAIL, UPDATENAME, LOCATION, BIRTHDATE, System.currentTimeMillis());

        UserEntity readUserEntity = userDao.getUser(EMAIL);
        assertThat(readUserEntity.getName(), equalTo(UPDATENAME));
        assertThat(readUserEntity.getLocation(), equalTo(userEntity.getLocation()));
        assertThat(readUserEntity.getBirthdate(), equalTo(userEntity.getBirthdate()));
    }
}