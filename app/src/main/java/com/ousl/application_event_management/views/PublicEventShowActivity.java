package com.ousl.application_event_management.views;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ousl.application_event_management.databinding.ActivityPublicEventShowBinding;
import com.ousl.application_event_management.models.PublicEvent;
import com.squareup.picasso.Picasso;

public class PublicEventShowActivity extends AppCompatActivity {
    ActivityPublicEventShowBinding binding;

    TextView title, description, venue, date, time, limitations;
    String userId, eventId, imageName;
    ImageView imageView;
    FirebaseDatabase database;
    FirebaseAuth auth;

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
        imageView = binding.publicEventBannerView;

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        // Get the current user's UID
        String currentUserId = auth.getCurrentUser().getUid();

        // retrieve the last entered event dynamically
        DatabaseReference eventsRef = database.getReference("public_events").child(currentUserId);
        eventsRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                        String lastEventKey = eventSnapshot.getKey();
                        DatabaseReference reference = eventsRef.child(lastEventKey);

                        PublicEvent event = eventSnapshot.getValue(PublicEvent.class);
                        if (event != null) {
                            title.setText(event.getTitle());
                            description.setText(event.getDescription());
                            venue.setText(event.getVenue());
                            date.setText(event.getDate());
                            time.setText(event.getTime());
                            limitations.setText(event.getLimitations());

                            userId = event.getUserId();
                            eventId = event.getEventID();
                            imageName = event.getImageName();
                            String path = "/"+userId+ "/" + eventId +"/"+imageName;
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
                                public void onFailure(@NonNull Exception e) {
                                    // Handle any errors that may occur while getting the download URL
                                    Log.e("FirebaseStorage", "Error getting download URL: " + e.getMessage());
                                }
                            });

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that occur.
            }
        });

        binding.dashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PublicEventShowActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
