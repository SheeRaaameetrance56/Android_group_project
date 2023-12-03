package com.ousl.application_event_management.controllers;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataBaseManager {
    private static DataBaseManager instance;
    private final FirebaseDatabase database;
    private final DatabaseReference reference;

    private DataBaseManager() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    public static synchronized DataBaseManager getInstance() {
        if (instance == null) {
            instance = new DataBaseManager();
        }
        return instance;
    }

    public DatabaseReference getReference() {
        return reference;
    }

    public DatabaseReference getReferenceUser(){
        return database.getReference("users");
    }

    public DatabaseReference getReferenceOrgUser(){
        return database.getReference("organization_users");
    }

    public DatabaseReference getReferencePublicEvent(){
        return database.getReference("public_events");
    }

    public DatabaseReference getReferencePrivateEvent(){
        return database.getReference("private_event");
    }
}
