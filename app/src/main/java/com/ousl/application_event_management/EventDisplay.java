package com.ousl.application_event_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ousl.application_event_management.R;
import com.ousl.application_event_management.databinding.ActivityEventDisplayBinding;
import com.ousl.application_event_management.databinding.ActivityPublicEventEntryBinding;
import com.ousl.application_event_management.models.PublicEvent;
import com.squareup.picasso.Picasso;

public class EventDisplay extends AppCompatActivity {

    ActivityEventDisplayBinding binding;
    TextView title, description, venue, date, time, limitations;
    ImageView imageView;
    DatabaseReference eventRef = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventDisplayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String eventID = getIntent().getStringExtra("EVENT_ID");
        if (eventID != null) {
             eventRef= FirebaseDatabase.getInstance().getReference().child("public_events").child(eventID);
            eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        PublicEvent event = dataSnapshot.getValue(PublicEvent.class);
                        if (event != null) {
                            // Populate views with the retrieved data
                            title.setText(event.getTitle());
                            description.setText(event.getDescription());
                            venue.setText(event.getVenue());
                            date.setText(event.getDate());
                            time.setText(event.getTime());
                            limitations.setText(event.getLimitations());

                            // For the image load on Picasso
                            Picasso.get().load(event.getImageUrl()).into(imageView);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            Toast.makeText(this, "The event ID is Null", Toast.LENGTH_SHORT).show();
        }



    }
}