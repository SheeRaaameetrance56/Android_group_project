package com.ousl.application_event_management.models;

import android.graphics.Bitmap;
import android.media.Image;

import java.sql.Time;
import java.util.Date;

public class PublicEvent {
    private String eventID, title, description, venue, limitations;
    private Date date;
    private Time time;
    private Bitmap banner;

    public PublicEvent() {
    }

    public PublicEvent(String title, String description, String venue, String limitations, Date date, Time time, Bitmap banner) {
        this.title = title;
        this.description = description;
        this.venue = venue;
        this.limitations = limitations;
        this.date = date;
        this.time = time;
        this.banner = banner;
    }

    public PublicEvent(String title, String description, String venue, Date date, Time time) {
        this.title = title;
        this.description = description;
        this.venue = venue;
        this.date = date;
        this.time = time;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Bitmap getBanner() {
        return banner;
    }

    public void setBanner(Bitmap banner) {
        this.banner = banner;
    }
}
