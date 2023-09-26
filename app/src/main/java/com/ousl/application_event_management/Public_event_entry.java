package com.ousl.application_event_management;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ousl.application_event_management.databinding.ActivityPublicEventEntryBinding;
import com.ousl.application_event_management.models.PublicEvent;
import com.ousl.application_event_management.models.Users;

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
    ImageView banner;
    private static final int SELECT_IMAGE = 100;

    PublicEvent publicEvent;

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
        banner = binding.imageView2;

        try {
            dateStr = binding.pubEventDate.getText().toString();
            timeStr = binding.pubEventTime.getText().toString();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
            date = dateFormat.parse(dateStr);
            time = new Time(timeFormat.parse(timeStr).getTime());
        }catch (Exception e){
            e.printStackTrace();
        }

        binding.pubEventPublishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!title.isEmpty() && !description.isEmpty() && !venue.isEmpty() && !dateStr.isEmpty() && !timeStr.isEmpty()) {
// TODO solve error coming on if condition
                    BitmapDrawable drawable = (BitmapDrawable) banner.getDrawable();
                    Bitmap imageBitmap = drawable.getBitmap();

                    FirebaseUser currentUser = auth.getCurrentUser();
                    if (currentUser != null) {
                        String uid = currentUser.getUid();
                        publicEvent = new PublicEvent(title, description, venue, limitations, date, time, imageBitmap);
                        database = FirebaseDatabase.getInstance();
                        reference = database.getReference("public_events");
                        reference.child(uid).setValue(publicEvent).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                binding.pubEventTitle.setText("");
                                binding.pubEventDescription.setText("");
                                binding.pubEventVenue.setText("");
                                binding.pubEventLimitations.setText("");
                                binding.pubEventDate.setText("");
                                binding.pubEventTime.setText("");

                            }
                        });
//                                            database.getReference().child("users").child(uid).setValue(user);
                    }

                    Intent intent = new Intent(Public_event_entry.this, PublicEventShowActivity.class);
                    startActivity(intent);
                } else {
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
