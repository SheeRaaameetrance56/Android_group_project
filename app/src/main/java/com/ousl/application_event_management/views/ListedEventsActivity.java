package com.ousl.application_event_management.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ousl.application_event_management.views.adapters.PrivateEventAdapter;
import com.ousl.application_event_management.views.adapters.PublicEventAdapter;
import com.ousl.application_event_management.databinding.ActivityListedEventsBinding;
import com.ousl.application_event_management.models.PrivateEvents;
import com.ousl.application_event_management.models.PublicEvent;

public class ListedEventsActivity extends AppCompatActivity {

    private ActivityListedEventsBinding binding;
    private PublicEventAdapter publicEventAdapter;
    private PrivateEventAdapter privateEventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListedEventsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        publicEventAdapter = new PublicEventAdapter(this);
        privateEventAdapter = new PrivateEventAdapter(this);

        binding.publicEvents.setLayoutManager(new GridLayoutManager(this,3));
        binding.privateEvents.setLayoutManager(new LinearLayoutManager(this));

        getPublicEvents();
        getPrivateEvents();

        publicEventAdapter.setOnItemClickListener(new PublicEventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PublicEvent event, String eventId, String userId) {
                Intent intent = new Intent(ListedEventsActivity.this, EditEventActivity.class);
                intent.putExtra("EVENT_ID", eventId);
                intent.putExtra("USER_ID", userId);
                intent.putExtra("PUBLIC_EVENT", String.valueOf(event));
                intent.putExtra("IS_PUBLIC_EVENT", true);
                startActivity(intent);

            }
        });

        privateEventAdapter.setOnItemClickListener(new PrivateEventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PrivateEvents event, String eventId, String userId) {
                Intent intent = new Intent(ListedEventsActivity.this, EditEventActivity.class);
                intent.putExtra("EVENT_ID", eventId);
                intent.putExtra("USER_ID", userId);
                intent.putExtra("IS_PUBLIC_EVENT", false);
                startActivity(intent);
            }
        });

    }

    public void getPublicEvents() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("public_events").child(currentUserId);

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot eventsSnapshot) {
                    for (DataSnapshot eventSnapshot : eventsSnapshot.getChildren()) {
                        PublicEvent event = eventSnapshot.getValue(PublicEvent.class);
                        publicEventAdapter.addEvent(event);
                    }
                    publicEventAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database read error
                    AlertDialog.Builder builder = new AlertDialog.Builder(ListedEventsActivity.this);
                    builder.setMessage("Please check the mobile connection or WIFI")
                            .setTitle("Connection Error");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        } else {
            // User is not authenticated, handle accordingly
        }

        binding.publicEvents.setAdapter(publicEventAdapter);
    }


    public void getPrivateEvents(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("private_event").child(currentUserId);

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot eventsSnapshot) {
                    for (DataSnapshot eventSnapshot : eventsSnapshot.getChildren()) {
                        PrivateEvents event = eventSnapshot.getValue(PrivateEvents.class);
                        privateEventAdapter.addEvent(event);
                    }
                    privateEventAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database read error
                    AlertDialog.Builder builder = new AlertDialog.Builder(ListedEventsActivity.this);
                    builder.setMessage("Please check the mobile connection or WIFI")
                            .setTitle("Connection Error");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        } else {
            // User is not authenticated, handle accordingly
        }

        binding.privateEvents.setAdapter(privateEventAdapter);
    }

}