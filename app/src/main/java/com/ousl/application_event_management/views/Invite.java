package com.ousl.application_event_management.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ousl.application_event_management.R;
import com.ousl.application_event_management.controllers.DataBaseManager;
import com.ousl.application_event_management.models.Users;
import java.util.ArrayList;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import android.content.Context;

import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

public class Invite extends AppCompatActivity {

    ListView usersListView;
    Button sendInvitationButton;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference usersReference;
    ArrayList<String> invitedUserEmails = new ArrayList<>();
    ArrayList<Users> userList;
    ArrayAdapter<Users> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        usersListView = findViewById(R.id.userlistview);
        sendInvitationButton = findViewById(R.id.sendinvitationbtn);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usersReference = database.getReference("users");

        userList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, userList);

        usersListView.setAdapter(adapter);
        usersListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        String eventId = getIntent().getStringExtra("EVENT_ID");

        // Load users from Firebase and display in ListView
        loadUsers();

        sendInvitationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInvitations(eventId);
            }
        });
    }
    private void loadUsers() {
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users name = dataSnapshot.getValue(Users.class);
                    if (name != null) {
                        userList.add(name);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Invite.this, "Failed to load users.", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void sendInvitations(String eventId) {
        SparseBooleanArray checked = usersListView.getCheckedItemPositions();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        invitedUserEmails.clear();

        for (int i = 0; i < userList.size(); i++) {
            if (checked.get(i)) {
                Users selectedUser = userList.get(i);
                String userEmail = selectedUser.getEmail();

                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
                Query emailQuery = usersRef.orderByChild("email").equalTo(userEmail);

                emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        DataBaseManager dataBaseManager = DataBaseManager.getInstance();
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String userId = userSnapshot.getKey(); // Retrieve the user ID associated with the email
                            if (userId != null) {
                                // Store the event ID under the user's ID node in 'invites'
                                DatabaseReference eventRef = dataBaseManager.getReferenceInvite().child(userId).child(eventId);
                                eventRef.setValue(eventId);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle onCancelled event
                    }
                });
            }
        }

        Toast.makeText(this, "Invitations sent successfully.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Invite.this, PrivateEventShowActivity.class));
    }
}

