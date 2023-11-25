package com.ousl.application_event_management.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ousl.application_event_management.R;
import com.ousl.application_event_management.databinding.ActivityEventDisplayBinding;
import com.ousl.application_event_management.databinding.ActivityPublicEventEntryBinding;
import com.ousl.application_event_management.models.PublicEvent;
import com.squareup.picasso.Picasso;

public class EventDisplay extends AppCompatActivity {

    ActivityEventDisplayBinding binding;
    TextView title, description, venue, date, time, limitations;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventDisplayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        title = binding.eventDisplayTitle;
        description = binding.eventDisplayDescription;
        venue = binding.eventDisplayVenue;
        date = binding.eventDisplayDate;
        time = binding.eventDisplayTime;
        limitations = binding.eventDisplayLimitations;
        imageView = binding.eventDisplayImage;

        String eventID = getIntent().getStringExtra("EVENT_ID");
        String userId = getIntent().getStringExtra("USER_ID");
        if (eventID != null) {

            DatabaseReference eventRef= FirebaseDatabase.getInstance().getReference().child("public_events").child(userId).child(eventID);
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

                            String path = "/"+event.getUserId()+ "/" + event.getEventID() +"/"+event.getImageName();
                            Log.w("imagePath", path );

                            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(path);
                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Picasso.get()
                                            .load(uri)
                                            .into(imageView);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@androidx.annotation.NonNull Exception e) {
                                    // Handle any errors that may occur while getting the download URL
                                    Log.e("FirebaseStorage", "Error getting download URL: " + e.getMessage());
                                }
                            });

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