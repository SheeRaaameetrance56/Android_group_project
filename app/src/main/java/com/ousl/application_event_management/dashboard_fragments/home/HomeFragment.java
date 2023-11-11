package com.ousl.application_event_management.dashboard_fragments.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ousl.application_event_management.R;
import com.ousl.application_event_management.databinding.FragmentHomeBinding;
import com.ousl.application_event_management.models.PublicEvent;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        LinearLayout containerLayout = root.findViewById(R.id.container_layout);

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
                                if (event != null) {
                                    CardView cardView = createCardView(event);
                                    containerLayout.addView(cardView);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle errors
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });

        return root;
    }

    private CardView createCardView(PublicEvent event) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        CardView cardView = (CardView) inflater.inflate(R.layout.item_card_view, null);

        TextView titleTextView = cardView.findViewById(R.id.card_view_title);
        ImageView imageView = cardView.findViewById(R.id.card_view_image);

        titleTextView.setText(event.getTitle());

        if (event.getImageUrl() != null && !event.getImageUrl().isEmpty()) {
            Picasso.get().load(event.getImageUrl()).into(imageView);
        }

        return cardView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}