package com.ousl.application_event_management.views.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ousl.application_event_management.views.EventDisplay;
import com.ousl.application_event_management.views.adapters.MyEventsAdapter;
import com.ousl.application_event_management.views.adapters.PublicEventAdapter;
import com.ousl.application_event_management.databinding.FragmentHomeBinding;
import com.ousl.application_event_management.models.PublicEvent;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private PublicEventAdapter publicEventAdapter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        publicEventAdapter = new PublicEventAdapter(getActivity());
        MyEventsAdapter myEventsAdapter = new MyEventsAdapter(getActivity());

        binding.publicEventRecycler.setLayoutManager(new GridLayoutManager(requireContext(),3));
        getPublicEvents();
//        getMyEvents();
//        binding.publicEventRecycler.setAdapter(publicEventAdapter);
//        binding.myEventRecycler.setAdapter(myEventsAdapter);

        publicEventAdapter = new PublicEventAdapter(getActivity());
        publicEventAdapter.setOnItemClickListener(new PublicEventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PublicEvent event, String eventId, String userId) {
                Intent intent = new Intent(requireActivity(), EventDisplay.class);
                intent.putExtra("EVENT_ID", eventId);
                intent.putExtra("USER_ID", userId);
                intent.putExtra("PUBLIC_EVENT", String.valueOf(event));
                startActivity(intent);
            }
        });

        return root;
    }

    public void getPublicEvents() {

        RecyclerView recyclerView = binding.publicEventRecycler;

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("public_events");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot usersSnapshot) {
                for (DataSnapshot userSnapshot : usersSnapshot.getChildren()) {
                    DatabaseReference eventsRef = userSnapshot.getRef();
                    eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot eventsSnapshot) {
                            for (DataSnapshot eventSnapshot : eventsSnapshot.getChildren()) {
                                PublicEvent event = eventSnapshot.getValue(PublicEvent.class);
                                publicEventAdapter.addEvent(event);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Please Check on the Mobile Connection or WIFI")
                                    .setTitle("Connection Error");
                            AlertDialog dialog = builder.create();
                        }
                    });
                }
                recyclerView.setAdapter(publicEventAdapter);
                publicEventAdapter.notifyDataSetChanged();

                recyclerView.setHasFixedSize(true);
                recyclerView.setItemViewCacheSize(10);
                recyclerView.setDrawingCacheEnabled(true);
                recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}