package com.ousl.application_event_management.models;

public class Users {
    String name, profilePicture, email, phoneNo, password;

    public Users(){
    }

    public Users(String name, String profilePicture, String email, String phoneNo) {
        this.name = name;
        this.profilePicture = profilePicture;
        this.email = email;
        this.phoneNo = phoneNo;
    }

    public Users(String name ,String email, String phoneNo) {
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @Override
    public String toString() {
        return name;
    }
}


