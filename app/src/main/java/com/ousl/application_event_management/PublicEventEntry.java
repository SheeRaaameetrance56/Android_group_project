package com.ousl.application_event_management;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ousl.application_event_management.databinding.ActivityPublicEventEntryBinding;
import com.ousl.application_event_management.models.PublicEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.StringJoiner;

public class PublicEventEntry extends AppCompatActivity {

    ActivityPublicEventEntryBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference reference;
    StorageReference storageReference;
    FirebaseStorage storage;
    String title, description, venue, limitations, dateStr, timeStr;
    Uri imageUri;
    private static final int SELECT_IMAGE = 100;
    PublicEvent publicEvent = new PublicEvent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPublicEventEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        reference = database.getReference(); // Get the root reference

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        binding.pubEventDate.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onLongClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(PublicEventEntry.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.pubEventDate.setText(String.valueOf(year)+"."+String.valueOf(month+1)+"."+String.valueOf(dayOfMonth));
                    }
                }, Year.now().getValue(), YearMonth.now().getMonthValue()-1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                dialog.show();
                return false;
            }
        });

        binding.pubEventTime.setOnLongClickListener(new View.OnLongClickListener() {
            final Calendar c = Calendar.getInstance();
            int mHour = c.get(Calendar.HOUR_OF_DAY);
            int mMinute = c.get(Calendar.MINUTE);
            @Override
            public boolean onLongClick(View v) {

                TimePickerDialog dialog = new TimePickerDialog(PublicEventEntry.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String timeFormat = String.format("%02d:%02d", hourOfDay, minute);

                        try {
                            // Convert the 24-hour format to AM/PM format
                            SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm");
                            SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a");
                            Date date = inputFormat.parse(timeFormat);
                            String formattedTime = outputFormat.format(date);

                            // Set the formatted time to the TextView
                            binding.pubEventTime.setText(formattedTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, mHour, mMinute,false);
                dialog.show();
                return false;
            }
        });

        binding.pubEventPublishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToDatabase();

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
                Intent intent = new Intent(PublicEventEntry.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            binding.banner.setImageURI(imageUri);
        }
    }

    public void saveToDatabase(){
        // Ensure the user is authenticated
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            title = binding.pubEventTitle.getText().toString();
            description = binding.pubEventDescription.getText().toString();
            venue = binding.pubEventVenue.getText().toString();
            limitations = binding.pubEventLimitations.getText().toString();
            dateStr = binding.pubEventDate.getText().toString();
            timeStr = binding.pubEventTime.getText().toString();

            // Create a new event with a unique key under the user's node
            DatabaseReference userEventsReference = reference.child("public_events").child(uid).push();

            publicEvent.setTimestamp(System.currentTimeMillis());
            if (imageUri != null) {
                publicEvent.setImageUrl(imageUri.toString());
            }

            publicEvent = new PublicEvent(currentUser.getUid(),userEventsReference.getKey(),title, description, venue, limitations, dateStr, timeStr);
            // Set the event data under the unique key
            userEventsReference.setValue(publicEvent).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(PublicEventEntry.this, "Event saved successfully", Toast.LENGTH_SHORT).show();

                        binding.pubEventTitle.setText("");
                        binding.pubEventDescription.setText("");
                        binding.pubEventVenue.setText("");
                        binding.pubEventLimitations.setText("");
                        binding.pubEventDate.setText("");
                        binding.pubEventTime.setText("");
                        binding.banner.setImageDrawable(null);

                        DatabaseReference newEventRef = userEventsReference.getRef();
                        String specificEventId = newEventRef.getKey();

                        publicEvent.setEventID(specificEventId);

                        if(imageUri!=null){
                            uploadToFirebase(currentUser.getUid(),specificEventId ,imageUri);
                        }else {
                            Toast.makeText(PublicEventEntry.this, "Making banner to the event might get more attention", Toast.LENGTH_SHORT).show();
                        }

                        Intent intent = new Intent(PublicEventEntry.this, PublicEventShowActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(PublicEventEntry.this, "Failed to save the event", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(PublicEventEntry.this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadToFirebase(final String userId, final String eventKey, Uri uri) {
        final StorageReference userRef = storageReference.child(userId);
        final StorageReference eventRef = userRef.child(eventKey);

        final StorageReference fileRef = eventRef.child(System.currentTimeMillis() + "." + getFileExtension(uri)); // Reference to the image file

        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUrl = uri.toString();
                        storeDownloadUrlInDatabase(eventKey, downloadUrl);

                        // Update the PublicEvent's image URL
                        publicEvent.setImageUrl(downloadUrl);
                    }
                });
            }
        });
    }

    private String getFileExtension(Uri muri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(muri));
    }

    private void storeDownloadUrlInDatabase(String eventKey, String downloadUrl) {
        FirebaseUser currentUser = auth.getCurrentUser();
        String uid = currentUser.getUid();
        DatabaseReference urlReference = reference.child("public_events").child(uid).child(eventKey).child("downloadUrl");
        urlReference.setValue(downloadUrl);
    }
}
