package com.ousl.application_event_management;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ousl.application_event_management.databinding.ActivityPublicEventEntryBinding;
import com.ousl.application_event_management.databinding.ActivityPublicEventShowBinding;

public class PublicEventShowActivity extends AppCompatActivity {

    ActivityPublicEventShowBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPublicEventShowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.dashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PublicEventShowActivity.this, dashboard.class);
                startActivity(intent);
                finish();
            }
        });
    }
}