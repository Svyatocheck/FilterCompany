package com.example.filtercompanylist.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private final String userId;
    private final String displayName;
    private final String mail;

    public LoggedInUser(String userId, String displayName, String mail) {
        this.userId = userId;
        this.displayName = displayName;
        this.mail = mail;
    }


    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getMail() {return mail;}
}