package com.lucianpiros.accountmanagement.database;

/**
 * API providing persistence functions.
 * Will be implemented by data repository
 */
public interface PersistenceDataProvider {

    void saveUserDetails(String userEmail, String name, String location, String birthdate);
    void updateUserDetails(String userEmail, String name, String location, String birthdate);
}
