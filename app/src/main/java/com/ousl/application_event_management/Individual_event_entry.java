package com.ousl.application_event_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.ousl.application_event_management.databinding.ActivityIndividualEventEntryBinding;
import com.ousl.application_event_management.models.PrivateEvents;

public class Individual_event_entry extends AppCompatActivity {

    ActivityIndividualEventEntryBinding binding;
    String title, description, venue, date, time, limitations;
    DatabaseReference reference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIndividualEventEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.priEventPublishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = binding.priEventTitle.getText().toString();
                description = binding.priEventDescription.getText().toString();
                venue = binding.priEventVenue.getText().toString();
                date = binding.priEventDate.getText().toString();
                time = binding.priEventTime.getText().toString();
                limitations = binding.priEventLimitations.getText().toString();

                PrivateEvents privateEvent = new PrivateEvents(title,description,venue,date,time,limitations);

                FirebaseUser currentUser = auth.getCurrentUser();
                String uID = currentUser.getUid();
                DatabaseReference userEventReference = reference.child("private_events").child(uID).push();

                userEventReference.setValue(privateEvent).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            binding.priEventTitle.setText("");
                            binding.priEventDescription.setText("");
                            binding.priEventVenue.setText("");
                            binding.priEventDate.setText("");
                            binding.priEventTime.setText("");
                            binding.priEventLimitations.setText("");
                            Toast.makeText(Individual_event_entry.this, "Event added successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(Individual_event_entry.this, "Something went wrong.!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}