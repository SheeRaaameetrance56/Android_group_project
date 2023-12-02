package com.ousl.application_event_management.views.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;

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
import com.ousl.application_event_management.views.adapters.PublicEventAdapter;
import com.ousl.application_event_management.databinding.FragmentHomeBinding;
import com.ousl.application_event_management.models.PublicEvent;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private PublicEventAdapter publicEventAdapter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private RecyclerView recyclerView ;
    private androidx.appcompat.widget.SearchView searchView;
    private List<PublicEvent> publicEventList;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        publicEventAdapter = new PublicEventAdapter(getActivity());
        searchView = binding.searchView;
        searchView.clearFocus();
        publicEventList = new ArrayList<>();

        binding.publicEventRecycler.setLayoutManager(new GridLayoutManager(requireContext(),3));
        recyclerView =binding.publicEventRecycler;
        getPublicEvents();

        publicEventAdapter.setOnItemClickListener(new PublicEventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PublicEvent event, String eventId, String userId) {
                Intent intent = new Intent(requireActivity(), EventDisplay.class);
                intent.putExtra("EVENT_ID", eventId);
                intent.putExtra("USER_ID", userId);
                intent.putExtra("IS_PUBLIC_EVENT", true);
                intent.putExtra("PUBLIC_EVENT", String.valueOf(event));
                startActivity(intent);
            }
        });

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        return root;
    }

    public void getPublicEvents() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("public_event");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot usersSnapshot) {
                publicEventList.clear();
                for (DataSnapshot userSnapshot : usersSnapshot.getChildren()) {
                    DatabaseReference eventsRef = userSnapshot.getRef();
                    eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot eventsSnapshot) {
                            for (DataSnapshot eventSnapshot : eventsSnapshot.getChildren()) {
                                PublicEvent event = eventSnapshot.getValue(PublicEvent.class);
                                publicEventAdapter.addEvent(event);
                                publicEventList.add(event);
                            }
                            recyclerView.setAdapter(publicEventAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle onCancelled
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    public void searchList(String text){
        ArrayList<PublicEvent> searchList = new ArrayList<>();
        if (!text.isEmpty()) {
            for (PublicEvent publicEvent : publicEventList) {
                if (publicEvent.getTitle().toLowerCase().contains(text.toLowerCase())) {
                    searchList.add(publicEvent);
                }
            }
        } else {
            searchList.addAll(publicEventList);
        }
        publicEventAdapter.searchedDataList(searchList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}