package com.lucianpiros.accountmanagement.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Room User entity
 */
@Entity(tableName = "user_table")
public class UserEntity {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "location")
    private String location;

    @ColumnInfo(name = "birthdate")
    private String birthdate;

    @ColumnInfo(name = "lastupdate_timestamp")
    private Long lastupdate_timestamp;

    public UserEntity(@NonNull String email, String name, String location, String birthdate, Long lastupdate_timestamp) {
        this.email = email;
        this.name = name;
        this.location = location;
        this.birthdate = birthdate;
        this.lastupdate_timestamp = lastupdate_timestamp;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public Long getLastupdate_timestamp() {
        return lastupdate_timestamp;
    }

    public void setLastupdate_timestamp(Long lastupdate_timestamp) {
        this.lastupdate_timestamp = lastupdate_timestamp;
    }
}
