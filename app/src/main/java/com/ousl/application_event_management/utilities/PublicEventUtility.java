package com.ousl.application_event_management.utilities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ousl.application_event_management.controllers.DataBaseManager;
import com.ousl.application_event_management.models.PublicEvent;
import com.ousl.application_event_management.views.adapters.PublicEventAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PublicEventUtility {

    public static void getOrderedPublicEvents(final RecyclerView recyclerView, final PublicEventAdapter publicEventAdapter) {
        DataBaseManager dataBaseManager = DataBaseManager.getInstance();
        DatabaseReference usersRef = dataBaseManager.getReferencePublicEvent();

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot usersSnapshot) {
                List<PublicEvent> publicEventList = new ArrayList<>();

                for (DataSnapshot userSnapshot : usersSnapshot.getChildren()) {
                    DatabaseReference eventsRef = userSnapshot.getRef();

                    eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot eventsSnapshot) {
                            for (DataSnapshot eventSnapshot : eventsSnapshot.getChildren()) {
                                PublicEvent event = eventSnapshot.getValue(PublicEvent.class);
                                if (event != null) {
                                    publicEventList.add(event);
                                }
                            }


                            Collections.sort(publicEventList, new Comparator<PublicEvent>() {
                                @Override
                                public int compare(PublicEvent event1, PublicEvent event2) {

                                    return event1.getDate().compareTo(event2.getDate());
                                }
                            });

                            publicEventAdapter.setEvents(publicEventList);

                            recyclerView.setAdapter(publicEventAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}

