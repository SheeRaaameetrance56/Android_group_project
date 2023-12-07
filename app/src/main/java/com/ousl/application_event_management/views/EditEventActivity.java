package com.ousl.application_event_management.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ousl.application_event_management.controllers.DataBaseManager;
import com.ousl.application_event_management.databinding.ActivityEditEventBinding;
import com.ousl.application_event_management.models.PublicEvent;
import com.squareup.picasso.Picasso;

public class EditEventActivity extends AppCompatActivity {

    private ActivityEditEventBinding binding;
    private EditText title, description, venue, date, time, limitations;
    private ImageView imageView;
    private Button setImage;
    private Button edit, cancel;
    private ImageButton delete;
    private Uri imageUri;
    private static final int SELECT_IMAGE = 100;

    private DatabaseReference reference;

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
        delete = binding.deleteEventBtn;

        String eventId = getIntent().getStringExtra("EVENT_ID");
        String userId = getIntent().getStringExtra("USER_ID");
        boolean isPublicEvent = getIntent().getBooleanExtra("IS_PUBLIC_EVENT", true);

        if(isPublicEvent){
            loadPublicEventDetails(userId, eventId);
        }
        else{
            loadPrivateEventDetails(userId, eventId);
        }

        setImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(
                        Intent.createChooser(intent, "Select Image"), SELECT_IMAGE
                );
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPublicEvent){
                    if (imageUri != null) {
                        editPublicEvent(userId, eventId, imageUri);
                    }
                    else{
                        Uri existImg = null;
                        editPublicEvent(userId, eventId, existImg);
                    }
                }
                else {
                    editPrivateEvent(userId, eventId);
                }
                startActivity(new Intent(EditEventActivity.this, ListedEventsActivity.class));
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent(userId, eventId, isPublicEvent);
                startActivity(new Intent(EditEventActivity.this, ListedEventsActivity.class));
                finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            binding.editEventImageView.setImageURI(imageUri);
        }
    }

    public void loadPublicEventDetails(String userId, String eventId){
        DataBaseManager dataBaseManager = DataBaseManager.getInstance();
        reference = dataBaseManager.getReferencePublicEvent().child(userId).child(eventId);
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
        DataBaseManager dataBaseManager = DataBaseManager.getInstance();
        reference = dataBaseManager.getReferencePrivateEvent().child(userId).child(eventId);
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

    public void editPublicEvent(String userId, String eventId, Uri uri){
        DataBaseManager dataBaseManager = DataBaseManager.getInstance();
        reference = dataBaseManager.getReferencePublicEvent().child(userId).child(eventId);

        reference.child("title").setValue(title.getText().toString());
        reference.child("description").setValue(description.getText().toString());
        reference.child("venue").setValue(venue.getText().toString());
        reference.child("date").setValue(date.getText().toString());
        reference.child("time").setValue(time.getText().toString());
        reference.child("limitations").setValue(limitations.getText().toString());

        if(uri!=null) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    PublicEvent publicEvent = snapshot.getValue(PublicEvent.class);
                    String path = "/" + userId + "/" + eventId + "/" + publicEvent.getImageName();
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(path);
                    storageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    public void editPrivateEvent(String userId, String eventId){
        DataBaseManager dataBaseManager = DataBaseManager.getInstance();
        reference = dataBaseManager.getReferencePrivateEvent().child(userId).child(eventId);

        reference.child("title").setValue(title.getText().toString());
        reference.child("description").setValue(description.getText().toString());
        reference.child("venue").setValue(venue.getText().toString());
        reference.child("date").setValue(date.getText().toString());
        reference.child("time").setValue(time.getText().toString());
        reference.child("limitations").setValue(limitations.getText().toString());
    }

    public void deleteEvent(String userId, String eventId, boolean isPublicEvent){
        if(isPublicEvent){
            DataBaseManager dataBaseManager = DataBaseManager.getInstance();
            reference = dataBaseManager.getReferencePublicEvent().child(userId).child(eventId);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        PublicEvent publicEvent = snapshot.getValue(PublicEvent.class);

                        reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    String imageName = publicEvent.getImageName();
                                    String imagePath = userId + "/" + eventId + "/" + imageName;
                                    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(imagePath);

                                    storageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> imageTask) {
                                            if (imageTask.isSuccessful()) {
                                                Log.i("Image_delete", "Deleted");
                                            } else {
                                                Log.e("Image_delete", "Failed to delete image");
                                            }
                                        }
                                    });
                                } else {
                                    Log.e("Event_delete", "Failed to delete event");
                                }
                            }
                        });
                    } else {
                        Log.e("Event_delete", "Event does not exist");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Error", "Database error: " + error.getMessage());
                }
            });
        }
        else{
            deletePrivateEvent(userId, eventId);
        }

    }

    private void deletePrivateEvent(String userId, String eventId) {
        DataBaseManager dataBaseManager = DataBaseManager.getInstance();
        reference = dataBaseManager.getReferencePrivateEvent().child(userId).child(eventId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    PublicEvent publicEvent = snapshot.getValue(PublicEvent.class);
                    reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                deletePrivateEventInvites(eventId);
                            } else {
                                Log.e("Event_delete", "Failed to delete event");
                            }
                        }
                    });
                } else {
                    Log.e("Event_delete", "Event does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", "Database error: " + error.getMessage());
            }
        });
    }

    public void deletePrivateEventInvites(String eventId){
        DataBaseManager dataBaseManager = DataBaseManager.getInstance();
        DatabaseReference invitesReference = dataBaseManager.getReferenceInvite();

        invitesReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot userSnapshot: snapshot.getChildren()){
                    for (DataSnapshot inviteSnapshot : userSnapshot.getChildren()) {
                        String inviteEventId = inviteSnapshot.getValue(String.class);
                        if (inviteEventId != null && inviteEventId.equals(eventId)) {
                            inviteSnapshot.getRef().removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", "Database error: " + error.getMessage());
            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(EditEventActivity.this, ListedEventsActivity.class));
        finish();
        super.onBackPressed();
    }
}














