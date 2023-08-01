package com.ousl.application_event_management;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateAccountActivity extends AppCompatActivity {

    Button navigation_sign_organization;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Instantiate Elements
        navigation_sign_organization = findViewById(R.id.navigation_sign_organization);

        // Button functions navigation
        navigation_sign_organization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountActivity.this, CreateAccountOrganizationActivity.class);
                startActivity(intent);
            }
        });
    }
}