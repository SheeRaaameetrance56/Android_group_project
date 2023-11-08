package com.ousl.application_event_management;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ousl.application_event_management.databinding.ActivityPublicEventEntryBinding;
import com.ousl.application_event_management.databinding.ActivityPublicEventShowBinding;

public class PublicEventShowActivity extends AppCompatActivity {
    ActivityPublicEventShowBinding binding;

    TextView title, description, venue, date, time, limitations;

    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPublicEventShowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        title = binding.publicEventNameView;
        description = binding.publicEventDescriptionView;
        venue = binding.publicEventVenueView;
        date = binding.publicEventDateView;
        time = binding.publicEventTimeView;
        limitations = binding.publicEventLimitationView;

        database.getInstance();
        DatabaseReference reference = database.getReference("https://database-splendy-default-rtdb.firebaseio.com/public_events");


        binding.dashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PublicEventShowActivity.this, dashboard.class);
                startActivity(intent);
                finish();
            }
        });
    }
}