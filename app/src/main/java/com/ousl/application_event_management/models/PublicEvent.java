package com.ousl.application_event_management.models;

import android.graphics.Bitmap;
import android.media.Image;

import java.sql.Time;
import java.util.Date;

public class PublicEvent {
    private String userId, eventId, title, description, venue, limitations, date, time;
    private String imageUrl;
    private long timestamp;

    public PublicEvent() {
    }

    public PublicEvent(String userId, String eventId, String title, String description, String venue, String limitations, String date, String time, String imageUrl) {
        this.userId = userId;
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.venue = venue;
        this.date = date;
        this.time = time;
        this.limitations = limitations;
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getEventID() {
        return eventId;
    }

    public void setEventID(String eventID) {
        this.eventId = eventID;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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

    public String getLimitations() {
        return limitations;
    }

    public void setLimitations(String limitations) {
        this.limitations = limitations;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String banner) {
        this.imageUrl = banner;
    }

}
