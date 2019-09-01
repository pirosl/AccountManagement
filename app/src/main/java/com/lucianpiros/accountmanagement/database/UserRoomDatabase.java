package com.lucianpiros.accountmanagement.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Room Database
 */
@Database(entities = {UserEntity.class}, version = 1)
public abstract class UserRoomDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    private static volatile UserRoomDatabase userRoomDatabaseInstance;

    static UserRoomDatabase getDatabase(final Context context) {
        if (userRoomDatabaseInstance == null) {
            synchronized (UserRoomDatabase.class) {
                if (userRoomDatabaseInstance == null) {
                    userRoomDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                            UserRoomDatabase.class, "user_database")
                            .build();
                }
            }
        }
        return userRoomDatabaseInstance;
    }
}
