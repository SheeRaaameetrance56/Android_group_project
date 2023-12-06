package com.ousl.application_event_management.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ousl.application_event_management.controllers.DataBaseManager;
import com.ousl.application_event_management.databinding.ActivityEditProfileBinding;

import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private EditText name, email, phoneNo, currentPassword, newPassword;
    private Button editButton, cancelButton;
    private DatabaseReference reference;
    private DatabaseReference referenceOrg;
    private FirebaseAuth authProfile;
    boolean userIsUser;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        name = binding.editProfileName;
        email = binding.editProfileEmail;
        phoneNo = binding.editProfilePhone;
        currentPassword = binding.editProfileCuurentPassword;
        newPassword = binding.editProfileNewPassword;
        editButton = binding.editProfileEditBtn;
        cancelButton = binding.editProfileCancelBtn;
        userIsUser = false;

        authProfile = FirebaseAuth.getInstance();
        DataBaseManager dataBaseManager = DataBaseManager.getInstance();
        reference = dataBaseManager.getReferenceUser().child(authProfile.getCurrentUser().getUid());
        referenceOrg = dataBaseManager.getReferenceOrgUser().child(authProfile.getCurrentUser().getUid());

        userChecker(new UserCheckCallback() {
            @Override
            public void onUserTypeReceived(boolean isOrganizationUser) {
                if (isOrganizationUser) {
                    loadOrgUser();
                } else {
                    loadUser();
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    userChecker(new UserCheckCallback() {
                        @Override
                        public void onUserTypeReceived(boolean isOrganizationUser) {
                            if (isOrganizationUser) {
                                editOrgUserDatabaseDetails();
                            } else {
                                editUserDatabaseDetails();
                            }
                        }
                    });
                    editAuthenticationDetails();
                }catch (Exception e){
                    Log.e("Error exception", e.toString() );
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, ProfileViewActivity.class));
                finish();
            }
        });
    }

    public void loadUser(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(snapshot.child("name").getValue(String.class));
                email.setText(snapshot.child("email").getValue(String.class));
                phoneNo.setText(snapshot.child("phoneNo").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileActivity.this, "Something went wrong..!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadOrgUser(){
        referenceOrg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(snapshot.child("nameOrg").getValue(String.class));
                email.setText(snapshot.child("emailOrg").getValue(String.class));
                phoneNo.setText(snapshot.child("phoneNumberOrg").getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileActivity.this, "Something went wrong..!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void editUserDatabaseDetails(){
        String newName = binding.editProfileName.getText().toString().trim();
        String newEmail = binding.editProfileEmail.getText().toString().trim();
        String newPhone = binding.editProfilePhone.getText().toString().trim();

        reference.child("email").setValue(newEmail);
        reference.child("name").setValue(newName);
        reference.child("phoneNo").setValue(newPhone);

    }

    public void editAuthenticationDetails(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        user.updateEmail(email.getText().toString());
        AuthCredential credential = EmailAuthProvider.getCredential(Objects.requireNonNull(user.getEmail()), currentPassword.getText().toString());
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                user.updatePassword(newPassword.getText().toString());
            }
        });

    }

    public void editOrgUserDatabaseDetails(){
        String newName = binding.editProfileName.getText().toString().trim();
        String newEmail = binding.editProfileEmail.getText().toString().trim();
        String newPhone = binding.editProfilePhone.getText().toString().trim();

        referenceOrg.child("emailOrg").setValue(newEmail);
        referenceOrg.child("nameOrg").setValue(newName);
        referenceOrg.child("phoneNumberOrg").setValue(newPhone);

    }

    public void userChecker(UserCheckCallback callback){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userID = currentUser.getUid();

            DatabaseReference userRef = DataBaseManager.getInstance().getReferenceOrgUser().child(userID);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean isOrganizationUser = false;

                    if (dataSnapshot.child("type").exists()) {
                        isOrganizationUser = true;
                    }

                    callback.onUserTypeReceived(isOrganizationUser);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    callback.onUserTypeReceived(false);
                }
            });
        } else {
            callback.onUserTypeReceived(false);
        }
    }


    public interface UserCheckCallback {
        void onUserTypeReceived(boolean isOrganizationUser);
    }


}