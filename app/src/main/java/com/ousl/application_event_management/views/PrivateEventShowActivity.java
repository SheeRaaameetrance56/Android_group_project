package com.ousl.application_event_management.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ousl.application_event_management.databinding.ActivityPrivateEventShowBinding;
import com.ousl.application_event_management.databinding.ActivityPublicEventEntryBinding;
import com.ousl.application_event_management.databinding.ActivityPublicEventShowBinding;
import com.ousl.application_event_management.models.PrivateEvents;

public class PrivateEventShowActivity extends AppCompatActivity {

    ActivityPrivateEventShowBinding binding;
    TextView title, description, venue, date, time, limitations;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrivateEventShowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        title = binding.publicEventNameView;
        description = binding.privateEventDescriptionView;
        venue = binding.privateEventVenueView;
        date = binding.privateEventDateView;
        time = binding.privateEventTimeView;
        limitations = binding.privateEventLimitationView;

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        // Get the current user's UID
        String currentUserId = auth.getCurrentUser().getUid();

        // Assuming you want to retrieve the last entered event dynamically
        DatabaseReference eventsRef = database.getReference("private_event").child(currentUserId);
        eventsRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                        String lastEventKey = eventSnapshot.getKey();
                        DatabaseReference reference = eventsRef.child(lastEventKey);

                        PrivateEvents event = eventSnapshot.getValue(PrivateEvents.class);
                        if (event != null) {
                            title.setText(event.getTitle());
                            description.setText(event.getDescription());
                            venue.setText(event.getVenue());
                            date.setText(event.getDate());
                            time.setText(event.getTime());
                            limitations.setText(event.getLimitations());
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
                Intent intent = new Intent(PrivateEventShowActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}

