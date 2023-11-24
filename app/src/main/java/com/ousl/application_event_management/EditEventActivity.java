package com.ousl.application_event_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ousl.application_event_management.databinding.ActivityEditEventBinding;
import com.ousl.application_event_management.models.PublicEvent;
import com.squareup.picasso.Picasso;

public class EditEventActivity extends AppCompatActivity {

    private ActivityEditEventBinding binding;
    private EditText title, description, venue, date, time, limitations;
    private ImageView imageView;
    private Button setImage, edit, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        title = binding.editEventTitle;
        description = binding.editEventDescription;
        venue = binding.editEventVenue;
        date = binding.editEventDate;
        time = binding.editEventTime;
        limitations = binding.editEventLimitations;

        imageView = binding.editEventImageView;

        setImage = binding.setImageBtn;
        edit = binding.editBtn;
        cancel = binding.cancelBtn;

        String eventId = getIntent().getStringExtra("EVENT_ID");
        String userId = getIntent().getStringExtra("USER_ID");

        if(getIntent().getBooleanExtra("IS_PUBLIC_EVENT", true)){
            loadPublicEventDetails(userId, eventId);
        }
        else{
            loadPrivateEventDetails(userId, eventId);
        }

        setImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditEventActivity.this, ListedEventsActivity.class));
                finish();
            }
        });
    }

    public void loadPublicEventDetails(String userId, String eventId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("public_events").child(userId).child(eventId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    PublicEvent event = snapshot.getValue(PublicEvent.class);

                    title.setText(snapshot.child("title").getValue(String.class));
                    description.setText(snapshot.child("description").getValue(String.class));
                    venue.setText(snapshot.child("venue").getValue(String.class));
                    date.setText(snapshot.child("date").getValue(String.class));
                    time.setText(snapshot.child("time").getValue(String.class));
                    limitations.setText(snapshot.child("limitations").getValue(String.class));

                    String path = "/"+event.getUserId()+ "/" + event.getEventID() +"/"+event.getImageName();
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(path);
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get()
                                    .load(uri)
                                    .into(imageView);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@androidx.annotation.NonNull Exception e) {
                            // Handle any errors that may occur while getting the download URL
                            Log.e("FirebaseStorage", "Error getting download URL: " + e.getMessage());
                        }
                    });

                }
                else{
                    Toast.makeText(EditEventActivity.this, "No data to represent", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditEventActivity.this, "Unable to connect database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadPrivateEventDetails(String userId, String eventId){
        Log.w("User and Event IDs", userId+eventId );
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("private_events").child(userId).child(eventId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    title.setText(snapshot.child("title").getValue(String.class));
                    description.setText(snapshot.child("description").getValue(String.class));
                    venue.setText(snapshot.child("venue").getValue(String.class));
                    date.setText(snapshot.child("date").getValue(String.class));
                    time.setText(snapshot.child("time").getValue(String.class));
                    limitations.setText(snapshot.child("limitations").getValue(String.class));

                    setImage.setClickable(false);
                    setImage.setText("No banners for this type.");
                }
                else{
                    Toast.makeText(EditEventActivity.this, "No data to represent. Something went error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}