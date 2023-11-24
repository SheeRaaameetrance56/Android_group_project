package com.ousl.application_event_management;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ousl.application_event_management.databinding.ActivityIndividualEventEntryBinding;
import com.ousl.application_event_management.models.PrivateEvents;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;

public class PrivateEventEntry extends AppCompatActivity {

    private ActivityIndividualEventEntryBinding binding;
    private String title, description, venue, date, time, limitations;
    private DatabaseReference reference;
    private FirebaseDatabase database;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIndividualEventEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        reference = database.getReference();

        binding.priEventDate.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onLongClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(PrivateEventEntry.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.priEventDate.setText(String.valueOf(year)+"."+String.valueOf(month+1)+"."+String.valueOf(dayOfMonth));
                    }
                }, Year.now().getValue(), YearMonth.now().getMonthValue()-1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                dialog.show();
                return false;
            }
        });

        binding.priEventTime.setOnLongClickListener(new View.OnLongClickListener() {
            final Calendar c = Calendar.getInstance();
            int mHour = c.get(Calendar.HOUR_OF_DAY);
            int mMinute = c.get(Calendar.MINUTE);
            @Override
            public boolean onLongClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(PrivateEventEntry.this, new TimePickerDialog.OnTimeSetListener() {
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
                            binding.priEventTime.setText(formattedTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, mHour, mMinute,false);
                dialog.show();
                return false;
            }
        });

        binding.priEventCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PrivateEventEntry.this, DashboardActivity.class));
                finish();
            }
        });

        binding.priInviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PrivateEventEntry.this, Invite.class));
                finish();
            }
        });

        binding.priEventPublishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = binding.priEventTitle.getText().toString();
                description = binding.priEventDescription.getText().toString();
                venue = binding.priEventVenue.getText().toString();
                date = binding.priEventDate.getText().toString();
                time = binding.priEventTime.getText().toString();
                limitations = binding.priEventLimitations.getText().toString();

                FirebaseUser currentUser = auth.getCurrentUser();
                String uID = currentUser.getUid();
                DatabaseReference userEventReference = reference.child("private_events").child(uID).push();

                String userId = currentUser.getUid();
                String eventId = userEventReference.getKey();

                PrivateEvents privateEvent = new PrivateEvents(title, description, venue, date, time, limitations, eventId, userId);

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
                            Toast.makeText(PrivateEventEntry.this, "Event added successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(PrivateEventEntry.this, PrivateEventShowActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(PrivateEventEntry.this, "Something went wrong.!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}