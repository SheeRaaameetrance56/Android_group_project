package com.ousl.application_event_management.views;

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
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
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
import com.ousl.application_event_management.controllers.DataBaseManager;
import com.ousl.application_event_management.databinding.ActivityPublicEventEntryBinding;
import com.ousl.application_event_management.models.PublicEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;

public class PublicEventEntry extends AppCompatActivity {

    private ActivityPublicEventEntryBinding binding;
    private DataBaseManager dataBaseManager;
    private FirebaseAuth auth;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private String userId, eventId, title, description, venue, limitations, dateStr, timeStr, imageUrl, imageName;
    private Uri imageUri;
    private static final int SELECT_IMAGE = 100;
    PublicEvent publicEvent = new PublicEvent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPublicEventEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase
        dataBaseManager = DataBaseManager.getInstance();
        auth = FirebaseAuth.getInstance();

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
                            SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm");
                            SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a");
                            Date date = inputFormat.parse(timeFormat);
                            String formattedTime = outputFormat.format(date);

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

            DatabaseReference userEventsReference = dataBaseManager.getReferencePublicEvent().child(uid).push();

            publicEvent.setTimestamp(System.currentTimeMillis());


            imageName = System.currentTimeMillis() + "." + getFileExtension(imageUri);

            DatabaseReference newEventRef = userEventsReference.getRef();
            String specificEventId = newEventRef.getKey();

            publicEvent.setEventID(specificEventId);
            if(imageUri!=null){
                uploadToFirebase(currentUser.getUid(),specificEventId ,imageUri);
            }else {
                Toast.makeText(PublicEventEntry.this, "Making banner to the event might get more attention", Toast.LENGTH_SHORT).show();
            }

            userId = currentUser.getUid();
            eventId = userEventsReference.getKey();

            publicEvent = new PublicEvent(userId, eventId,title, description, venue, limitations, dateStr, timeStr, imageName);
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

                        // todo change the navigation to event show
                        Intent intent = new Intent(PublicEventEntry.this, DashboardActivity.class);
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

        final StorageReference fileRef = eventRef.child(imageName); // Reference to the image file
        publicEvent.setImageName(imageName);

        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUrl = uri.toString();
                        storeDownloadUrlInDatabase(eventKey, downloadUrl);

                        imageUrl = downloadUrl;
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
    }

}
