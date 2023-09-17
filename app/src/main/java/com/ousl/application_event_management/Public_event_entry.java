package com.ousl.application_event_management;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ousl.application_event_management.databinding.ActivityPublicEventEntryBinding;

public class Public_event_entry extends AppCompatActivity {

    ActivityPublicEventEntryBinding binding;
    FirebaseDatabase database;
    DatabaseReference reference;
    String title, description, venue, date, time, limitations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPublicEventEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // database instantiate
        database = FirebaseDatabase.getInstance();

        title = binding.pubEventTitle.getText().toString();
        description = binding.pubEventDescription.getText().toString();
        venue = binding.pubEventVenue.getText().toString();
        limitations = binding.pubEventLimitations.getText().toString();
        binding.pubEventPublishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}