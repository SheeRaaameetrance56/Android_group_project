package com.ousl.application_event_management.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ousl.application_event_management.controllers.AuthenticationManager;
import com.ousl.application_event_management.controllers.DataBaseManager;
import com.ousl.application_event_management.databinding.ActivityProfileViewBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileViewActivity extends AppCompatActivity {

    private TextView profile_name, profile_email, profile_phone, profile_joined, profile_address;
    private Button profileEditBtn, profileLogoutBtn, listedEventsBtn;
    private ActivityProfileViewBinding binding;
    private DatabaseReference reference;
    private DatabaseReference referenceOrg;
    private AuthenticationManager authManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        profile_name = binding.profileName;
        profile_email = binding.profileEmail;
        profile_phone = binding.profilePhone;
        profile_address = binding.profileAddress;
        profile_joined = binding.profileJoined;
        profileEditBtn = binding.profileEditBtn;
        profileLogoutBtn = binding.profileLogoutBtn;
        listedEventsBtn = binding.profileListedEventButton;

        FirebaseAuth authProfile = FirebaseAuth.getInstance();
        FirebaseUser currentUser = authProfile.getCurrentUser();

        DataBaseManager dataBaseManager = DataBaseManager.getInstance();

        reference = dataBaseManager.getReferenceUser().child(currentUser.getUid());
        referenceOrg = dataBaseManager.getReferenceOrgUser().child(currentUser.getUid());

        userProfile();
        userOrgProfile();

        long creationTimestamp = currentUser.getMetadata().getCreationTimestamp();
        Date creationDate = new Date(creationTimestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(creationDate);
        profile_joined.setText(formattedDate);



        profileEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileViewActivity.this, EditProfileActivity.class));
            }
        });

        profileLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ProfileViewActivity.this, LoginActivity.class);
                startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
            }
        });

        listedEventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileViewActivity.this, ListedEventsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }

    public void userProfile(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    profile_name.setText(snapshot.child("name").getValue(String.class));
                    profile_email.setText(snapshot.child("email").getValue(String.class));
                    profile_address.setText("");
                    binding.textView23.setText("");
                    profile_phone.setText(snapshot.child("phoneNo").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void userOrgProfile(){
        referenceOrg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    profile_name.setText(snapshot.child("nameOrg").getValue(String.class));
                    profile_email.setText(snapshot.child("emailOrg").getValue(String.class));
                    profile_address.setText(snapshot.child("addressOrg").getValue(String.class));
                    profile_phone.setText(snapshot.child("phoneNumberOrg").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ProfileViewActivity.this, DashboardActivity.class));
        finish();
        super.onBackPressed();
    }
}