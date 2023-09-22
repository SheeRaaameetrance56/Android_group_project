package com.ousl.application_event_management;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ousl.application_event_management.databinding.ActivityPublicEventEntryBinding;

public class Public_event_entry extends AppCompatActivity {

    ActivityPublicEventEntryBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference reference;
    String title, description, venue, date, time, limitations;
    private static final int SELECT_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPublicEventEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // database instantiate
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        title = binding.pubEventTitle.getText().toString();
        description = binding.pubEventDescription.getText().toString();
        venue = binding.pubEventVenue.getText().toString();
        limitations = binding.pubEventLimitations.getText().toString();
        date = binding.pubEventDate.getText().toString();
        time = binding.pubEventTime.getText().toString();
        binding.pubEventPublishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!title.isEmpty() && !description.isEmpty() && !venue.isEmpty() && !time.isEmpty() && !date.isEmpty()){
                    // TODO database approach
                    Intent intent = new Intent(Public_event_entry.this, PublicEventShowActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(Public_event_entry.this, "Title, Description, Venue, Date and Time Required", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_IMAGE && null != data){
            Uri uri = data.getData();
            binding.imageView2.setImageURI(uri);
        }
    }
}