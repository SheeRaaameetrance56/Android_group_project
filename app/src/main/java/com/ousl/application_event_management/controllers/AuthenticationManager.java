package com.ousl.application_event_management.controllers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ousl.application_event_management.models.Users;

public class AuthenticationManager {
    private static AuthenticationManager instance;
    private FirebaseAuth auth;
    private FirebaseUser user;

    private AuthenticationManager(){
        auth = FirebaseAuth.getInstance();
    }

    public static synchronized AuthenticationManager getInstance(){
        if (instance == null) {
            instance = new AuthenticationManager();
        }
        return instance;
    }

    public FirebaseUser getCurrentUser(){
        user = auth.getCurrentUser();
        return user;
    }

    public void createUserWithEmailAndPassword(String email, String password, String name, String phoneNo, AuthenticationListener listener){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = auth.getCurrentUser();
                        if (currentUser != null) {
                            String uid = currentUser.getUid();
                            Users user = new Users(name, email, phoneNo);

                            DataBaseManager dataBaseManager = DataBaseManager.getInstance();
                            dataBaseManager.getReferenceUser().child(uid).setValue(user)
                                    .addOnCompleteListener(innerTask -> {
                                        if (innerTask.isSuccessful()) {
                                            currentUser.sendEmailVerification()
                                                    .addOnCompleteListener(emailVerificationTask -> {
                                                        if (emailVerificationTask.isSuccessful()) {
                                                            listener.onAccountCreated();
                                                        } else {
                                                            listener.onEmailVerificationFailed();
                                                        }
                                                    });
                                        } else {
                                            listener.onAccountCreationFailed();
                                        }
                                    });
                        }
                    } else {
                        listener.onAuthenticationFailed();
                    }
                });
    }

    public interface AuthenticationListener {
        void onAccountCreated();

        void onAccountCreationFailed();

        void onAuthenticationFailed();

        void onEmailVerificationFailed();
    }
}
