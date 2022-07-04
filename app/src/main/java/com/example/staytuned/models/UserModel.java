package com.example.staytuned.models;

import java.util.HashMap;

public class UserModel {
    String userID;
    String userName;
    String userEmail;
//    String userProfilePicURL;
//    GeoPoint userLastLocation;
//    ArrayList<String> userInvitedEventsIDs;
    HashMap<String,String> events;

    public UserModel() {
    }

    public UserModel(String userID, String userName, String userEmail, String userProfilePicURL) {
        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
//        this.userProfilePicURL = userProfilePicURL;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

//    public String getUserProfilePicURL() {
//        return userProfilePicURL;
//    }

//    public void setUserProfilePicURL(String userProfilePicURL) {
//        this.userProfilePicURL = userProfilePicURL;
//    }


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public HashMap<String, String> getEvents() {
        return events;
    }

    public UserModel setEvents(HashMap<String, String> events) {
        this.events = events;
        return this;
    }
}
