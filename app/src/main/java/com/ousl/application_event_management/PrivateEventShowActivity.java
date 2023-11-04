package com.ousl.application_event_management;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ousl.application_event_management.databinding.ActivityPrivateEventShowBinding;
import com.ousl.application_event_management.databinding.ActivityPublicEventEntryBinding;
import com.ousl.application_event_management.databinding.ActivityPublicEventShowBinding;

public class PrivateEventShowActivity extends AppCompatActivity {

    ActivityPrivateEventShowBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrivateEventShowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}