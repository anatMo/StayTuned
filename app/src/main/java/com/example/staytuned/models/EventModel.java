package com.example.staytuned.models;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;
import java.util.HashMap;

public class EventModel {
    private String eventID;
    private String eventName;
    private String eventDescription;
    private String eventLocationName;
    private Date eventTimeCreated;
    private Date eventTimeStarting;
    private String eventCreatorUID;
    private String eventCreatorName;
    private HashMap<String, String> usersAttending;

    public EventModel(String eventID, String eventCreatorUID, String eventName, String eventDescription, String eventLocationName, Date eventTimeCreated, Date eventTimeStarting, GeoPoint eventLocation, String eventCreatorName) {
        this.eventID = eventID;
        this.eventCreatorUID = eventCreatorUID;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventLocationName = eventLocationName;
        this.eventTimeCreated = eventTimeCreated;
        this.eventTimeStarting = eventTimeStarting;
        this.eventCreatorName = eventCreatorName;
    }

    public EventModel() {

    }

//    public ArrayList<UserModel> getEventInvitedUsers() {
//        return eventInvitedUsers;
//    }
//
//    public void setEventInvitedUsers(ArrayList<UserModel> eventInvitedUsers) {
//        this.eventInvitedUsers = eventInvitedUsers;
//    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventCreatorUID() {
        return eventCreatorUID;
    }

    public void setEventCreatorUID(String eventCreatorUID) {
        this.eventCreatorUID = eventCreatorUID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventLocationName() {
        return eventLocationName;
    }

    public void setEventLocationName(String eventLocationName) {
        this.eventLocationName = eventLocationName;
    }

    public Date getEventTimeCreated() {
        return eventTimeCreated;
    }

    public void setEventTimeCreated(Date eventTimeCreated) {
        this.eventTimeCreated = eventTimeCreated;
    }

    public Date getEventTimeStarting() {
        return eventTimeStarting;
    }

    public void setEventTimeStarting(Date eventTimeStarting) {
        this.eventTimeStarting = eventTimeStarting;
    }

    public String getEventCreatorName() {
        return eventCreatorName;
    }

    public void setEventCreatorName(String eventCreatorName) {
        this.eventCreatorName = eventCreatorName;
    }

    public HashMap<String, String> getUsersAttending() {
        return usersAttending;
    }

    public EventModel setUsersAttending(HashMap<String, String> usersAttending) {
        this.usersAttending = usersAttending;
        return this;
    }
}
