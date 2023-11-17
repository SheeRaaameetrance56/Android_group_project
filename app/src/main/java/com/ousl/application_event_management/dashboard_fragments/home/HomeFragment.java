package com.ousl.application_event_management.dashboard_fragments.home;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ousl.application_event_management.R;
import com.ousl.application_event_management.EventDisplay;
import com.ousl.application_event_management.adapters.MyEventsAdapter;
import com.ousl.application_event_management.adapters.PublicEventAdapter;
import com.ousl.application_event_management.databinding.FragmentHomeBinding;
import com.ousl.application_event_management.models.PublicEvent;
import com.squareup.picasso.Picasso;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private PublicEventAdapter publicEventAdapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
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

        publicEventAdapter.setOnItemClickListener(new PublicEventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PublicEvent event) {
                // Handle click, start the EventDisplay activity
                Intent intent = new Intent(requireActivity(), EventDisplay.class);
                // Pass necessary data to the intent if needed
                startActivity(intent);
            }
        });

        return root;
    }

    private void getPublicEvents() {
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
                binding.publicEventRecycler.setAdapter(publicEventAdapter);
                publicEventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }

//    private void getMyEvents() {
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//// Check if the user is authenticated
//        if (currentUser != null) {
//            String userId = currentUser.getUid();
//            DatabaseReference userEventsRef = FirebaseDatabase.getInstance().getReference("public_events").child(userId);
//
//            // Now, instead of fetching all users' events, this will fetch only the events for the current user
//            userEventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot eventsSnapshot) {
//                    for (DataSnapshot eventSnapshot : eventsSnapshot.getChildren()) {
//                        PublicEvent event = eventSnapshot.getValue(PublicEvent.class);
//                        publicEventAdapter.addEvent(event);
////                        if (event != null) {
////                            CardView cardView = createCardView(event);
////                            binding.myEventRecycler.addView(cardView);
////                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    // Handle errors
//                }
//            });
//        }
//    }

    private CardView createCardView(PublicEvent event) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        CardView cardView = (CardView) inflater.inflate(R.layout.item_card_view, null);

        TextView titleTextView = cardView.findViewById(R.id.card_view_title);
        ImageView imageView = cardView.findViewById(R.id.card_view_image);

        titleTextView.setText(event.getTitle());

        if (event.getImageUrl() != null && !event.getImageUrl().isEmpty()) {
            Picasso.get().load(event.getImageUrl()).into(imageView, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    // Image loaded successfully
                }

                @Override
                public void onError(Exception e) {
                    // Log or handle the error
                }
            });
            //Picasso.get().load(event.getImageUrl()).into(imageView);
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), EventDisplay.class);
                startActivity(intent);
            }
        });

        return cardView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}