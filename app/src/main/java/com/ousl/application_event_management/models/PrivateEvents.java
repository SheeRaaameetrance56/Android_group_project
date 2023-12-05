package com.ousl.application_event_management.models;

import java.util.List;

public class PrivateEvents {
    private String title, description, venue, date, time, limitations, eventId, userId;
    private List<String> invitedUserEmails; // Add this line

    public PrivateEvents() {
        //empty constructor
    }

    public PrivateEvents(String title, String description, String venue, String date, String time, String limitations, String eventId, String userId) {
        this.title = title;
        this.description = description;
        this.venue = venue;
        this.date = date;
        this.time = time;
        this.limitations = limitations;
        this.eventId = eventId;
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLimitations() {
        return limitations;
    }

    public void setLimitations(String limitations) {
        this.limitations = limitations;
    }

    public List<String> getInvitedUserEmails() {
        return invitedUserEmails;
    }

    public void setInvitedUserEmails(List<String> invitedUserEmails) {
        this.invitedUserEmails = invitedUserEmails;
    }
}
