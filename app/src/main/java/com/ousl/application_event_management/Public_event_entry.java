package com.ousl.application_event_management;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
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

public class Public_event_entry extends AppCompatActivity {

    ActivityPublicEventEntryBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference reference;
    StorageReference storageReference;
    FirebaseStorage storage;
    String title, description, venue, limitations, dateStr, timeStr;
    Uri imageUri;

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

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        binding.pubEventDate.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(Public_event_entry.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.pubEventDate.setText(String.valueOf(year)+"."+String.valueOf(month+1)+"."+String.valueOf(dayOfMonth));
                    }
                }, 2023, 00, 01);
                dialog.show();
                return false;
            }
        });

        binding.pubEventPublishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                    // Fetch the banner image here as well
                    if(imageUri!=null){
                        uploadToFirebase(userEventsReference.getKey(), imageUri);
                    }else {
                        Toast.makeText(Public_event_entry.this, "Making banner to the event might get more attention", Toast.LENGTH_SHORT).show();
                    }



                    publicEvent = new PublicEvent(title, description, venue, limitations, dateStr, timeStr, imageUri.toString());
                    publicEvent.setTimestamp(System.currentTimeMillis());

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

                                String newEventKey = userEventsReference.getKey();

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
            imageUri = data.getData();
            binding.banner.setImageURI(imageUri);
        }
    }

    private void uploadToFirebase(final String eventKey, Uri uri){
        StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Store the download URL in the Realtime Database
                        String downloadUrl = uri.toString();
                        storeDownloadUrlInDatabase(eventKey, downloadUrl);
                        Toast.makeText(Public_event_entry.this, "Uploaded image", Toast.LENGTH_SHORT).show();
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
        // Here, you can store the downloadUrl in your Realtime Database under the appropriate location.
        // Replace the following line with the correct location in your database.
        DatabaseReference urlReference = reference.child("public_events").child(uid).child(eventKey).child("downloadUrl");
        urlReference.setValue(downloadUrl);
    }
}
