package com.ousl.application_event_management.models;

public class UsersOrganization {

    private String nameOrg, emailOrg, phoneNumberOrg, addressOrg, passwordOrg, type;

    public UsersOrganization() {
    }

    public UsersOrganization(String nameOrg, String emailOrg, String phoneNumberOrg, String addressOrg, String type) {
        this.nameOrg = nameOrg;
        this.emailOrg = emailOrg;
        this.phoneNumberOrg = phoneNumberOrg;
        this.addressOrg = addressOrg;
        this.type = type;
    }

    public UsersOrganization(String emailOrg, String passwordOrg) {
        this.emailOrg = emailOrg;
        this.passwordOrg = passwordOrg;
    }

    public String getNameOrg() {
        return nameOrg;
    }

    public void setNameOrg(String nameOrg) {
        this.nameOrg = nameOrg;
    }

    public String getEmailOrg() {
        return emailOrg;
    }

    public void setEmailOrg(String emailOrg) {
        this.emailOrg = emailOrg;
    }

    public String getPhoneNumberOrg() {
        return phoneNumberOrg;
    }

    public void setPhoneNumberOrg(String phoneNumberOrg) {
        this.phoneNumberOrg = phoneNumberOrg;
    }

    public String getAddressOrg() {
        return addressOrg;
    }

    public void setAddressOrg(String addressOrg) {
        this.addressOrg = addressOrg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
