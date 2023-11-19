package com.ousl.application_event_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ousl.application_event_management.databinding.ActivityProfileViewBinding;

public class ProfileViewActivity extends AppCompatActivity {

    TextView profile_name, profile_email, profile_phone;
    Button profileEditBtn, profileLogoutBtn;
    ActivityProfileViewBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        profile_name = binding.profileName;
        profile_email = binding.profileEmail;
        profile_phone = binding.profilePhone;
        profileEditBtn = binding.profileEditBtn;
        profileLogoutBtn = binding.profileLogoutBtn;

        FirebaseAuth authProfile = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(authProfile.getCurrentUser().getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    profile_name.setText(snapshot.child("name").getValue(String.class));
                    profile_email.setText(snapshot.child("email").getValue(String.class));
                    profile_phone.setText(snapshot.child("phoneNo").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileViewActivity.this, "Something went wrong.!", Toast.LENGTH_SHORT).show();
            }
        });

        profileEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileViewActivity.this, EditProfileActivity.class));
            }
        });

        profileLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authProfile.signOut();
                startActivity(new Intent(ProfileViewActivity.this, LoginActivity.class));
                finish();
            }
        });

    }
}