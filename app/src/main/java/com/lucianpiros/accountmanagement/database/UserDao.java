package com.lucianpiros.accountmanagement.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

/**
 * Room User DAO
 */
@Dao
public interface UserDao {
    @Insert
    void insert(UserEntity userEntity);

    @Query("DELETE FROM user_table")
    void deleteAll();

    @Query("SELECT * from user_table")
    List<UserEntity> getAllUsers();

    @Query("SELECT * from user_table WHERE email LIKE :userEmail")
    UserEntity getUser(String userEmail);

    @Query("DELETE FROM user_table WHERE email LIKE :userEmail")
    void deleteUser(String userEmail);

    @Query("UPDATE user_table SET name = :name, location = :location, birthdate = :birthdate, lastupdate_timestamp = :lastupdate_timestamp WHERE email LIKE :userEmail")
    void updateUserDetails(String userEmail, String name, String location, String birthdate, Long lastupdate_timestamp);
}
