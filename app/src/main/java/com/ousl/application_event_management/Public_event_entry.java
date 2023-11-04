package com.ousl.application_event_management;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ousl.application_event_management.databinding.ActivityPublicEventEntryBinding;
import com.ousl.application_event_management.models.PublicEvent;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Public_event_entry extends AppCompatActivity {

    ActivityPublicEventEntryBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference reference;
    String title, description, venue, limitations, dateStr, timeStr;
    Date date;
    Time time;
    Drawable banner;
    private static final int SELECT_IMAGE = 100;

    PublicEvent publicEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPublicEventEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        reference = database.getReference(); // Get the root reference

        binding.pubEventPublishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = binding.pubEventTitle.getText().toString();
                description = binding.pubEventDescription.getText().toString();
                venue = binding.pubEventVenue.getText().toString();
                limitations = binding.pubEventLimitations.getText().toString();
                dateStr = binding.pubEventDate.getText().toString();
                timeStr = binding.pubEventTime.getText().toString();

                // Ensure the user is authenticated
                FirebaseUser currentUser = auth.getCurrentUser();
                if (currentUser != null) {
                    String uid = currentUser.getUid();

                    // Create a new event with a unique key under the user's node
                    DatabaseReference userEventsReference = reference.child("public_events").child(uid).push();

                    try {
                        // Convert date and time to the correct format
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
                        date = dateFormat.parse(dateStr);
                        time = new Time(timeFormat.parse(timeStr).getTime());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Fetch the banner image here as well
                    Bitmap imageBitmap = null;
                    try {
                        BitmapDrawable drawable = (BitmapDrawable) banner;
                        imageBitmap = drawable.getBitmap();
                    } catch (Exception e) {
                        // Handle the exception
                    }

                    publicEvent = new PublicEvent(title, description, venue, limitations, date, time, imageBitmap);

                    // Set the event data under the unique key
                    userEventsReference.setValue(publicEvent).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Public_event_entry.this, "Event saved successfully", Toast.LENGTH_SHORT).show();
                                // Clear input fields
                                binding.pubEventTitle.setText("");
                                binding.pubEventDescription.setText("");
                                binding.pubEventVenue.setText("");
                                binding.pubEventLimitations.setText("");
                                binding.pubEventDate.setText("");
                                binding.pubEventTime.setText("");
                                binding.banner.setImageDrawable(null);
                            } else {
                                Toast.makeText(Public_event_entry.this, "Failed to save the event", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    Intent intent = new Intent(Public_event_entry.this, PublicEventShowActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Public_event_entry.this, "User not authenticated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.pubEventSelectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(
                        Intent.createChooser(intent, "Select Image"), SELECT_IMAGE
                );
            }
        });

        binding.pubEventCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.pubEventTitle.setText("");
                binding.pubEventDescription.setText("");
                binding.pubEventVenue.setText("");
                binding.pubEventLimitations.setText("");
                binding.pubEventDate.setText("");
                binding.pubEventTime.setText("");
                binding.banner.setImageDrawable(null);
                Intent intent = new Intent(Public_event_entry.this, dashboard.class);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            binding.banner.setImageURI(uri);
        }
    }
}
