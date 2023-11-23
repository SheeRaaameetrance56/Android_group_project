package com.ousl.application_event_management.models;

public class Users {
    String name, profilePicture, email, phoneNo, password;

    public Users(){
    }

    public Users(String name, String profilePicture, String email, String phoneNo, String password) {
        this.name = name;
        this.profilePicture = profilePicture;
        this.email = email;
        this.phoneNo = phoneNo;
        this.password = password;
    }

    public Users(String name ,String email, String password, String phoneNo) {
        this.name = name;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return name;
    }
}


