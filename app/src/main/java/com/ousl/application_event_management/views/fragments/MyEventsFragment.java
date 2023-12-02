package com.ousl.application_event_management.views.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ousl.application_event_management.databinding.FragmentMyEventsBinding;
import com.ousl.application_event_management.models.PrivateEvents;
import com.ousl.application_event_management.views.EventDisplay;
import com.ousl.application_event_management.views.ListedEventsActivity;
import com.ousl.application_event_management.views.adapters.PrivateEventAdapter;

public class MyEventsFragment extends Fragment {

    private FragmentMyEventsBinding binding;
    private PrivateEventAdapter privateEventAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyEventsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        privateEventAdapter = new PrivateEventAdapter(requireContext());
        binding.listedPrivateEvents.setLayoutManager(new LinearLayoutManager(requireContext()));
        getPrivateEvents();

        privateEventAdapter.setOnItemClickListener(new PrivateEventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PrivateEvents event, String eventId, String userId) {
                Intent intent = new Intent(requireContext(), EventDisplay.class);
                intent.putExtra("USER_ID", userId);
                intent.putExtra("EVENT_ID", eventId);
                intent.putExtra("IS_PUBLIC_EVENT", false);
                startActivity(intent);
            }
        });

        return root;
    }

    public MyEventsFragment() {
        // Required empty public constructor
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setMessage("Please check the mobile connection or WIFI")
                            .setTitle("Connection Error");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }

        binding.listedPrivateEvents.setAdapter(privateEventAdapter);
    }

}