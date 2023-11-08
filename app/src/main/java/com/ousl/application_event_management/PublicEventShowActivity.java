package com.ousl.application_event_management;

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
import com.ousl.application_event_management.databinding.ActivityPublicEventEntryBinding;
import com.ousl.application_event_management.databinding.ActivityPublicEventShowBinding;
import com.ousl.application_event_management.models.PublicEvent;

public class PublicEventShowActivity extends AppCompatActivity {
    ActivityPublicEventShowBinding binding;

    TextView title, description, venue, date, time, limitations;

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

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        // Get the current user's UID
        String currentUserId = auth.getCurrentUser().getUid();

        // Assuming you store the last entered event key in a variable called "lastEventKey"
        String lastEventKey = "YOUR_LAST_EVENT_KEY";

        DatabaseReference reference = database.getReference("public_events").child(currentUserId).child(lastEventKey);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve event data and set it in your TextViews
                    PublicEvent event = dataSnapshot.getValue(PublicEvent.class);
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

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that occur.
            }
        });

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
