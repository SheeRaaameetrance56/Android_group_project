package com.ousl.application_event_management.models;

import android.graphics.Bitmap;
import android.media.Image;

import java.sql.Time;
import java.util.Date;

public class PublicEvent {
    private String eventID, title, description, venue, limitations, date, time;
    private String banner;

    public PublicEvent() {
    }

    public PublicEvent(String title, String description, String venue, String limitations, String date, String time, String banner) {
        this.title = title;
        this.description = description;
        this.venue = venue;
        this.limitations = limitations;
        this.date = date;
        this.time = time;
        this.banner = banner;
    }

    public PublicEvent(String title, String description, String venue, String limitations, String date, String time) {
        this.title = title;
        this.description = description;
        this.venue = venue;
        this.date = date;
        this.time = time;
        this.limitations = limitations;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
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

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }
}
