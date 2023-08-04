package com.ousl.application_event_management;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateAccountOrganizationActivity extends AppCompatActivity {

    Button navigation_sign_indvidual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_organization);


        navigation_sign_indvidual = findViewById(R.id.navigation_sign_indvidual);

        navigation_sign_indvidual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountOrganizationActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });
    }
}