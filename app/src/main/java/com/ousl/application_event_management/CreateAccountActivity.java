package com.ousl.application_event_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.ousl.application_event_management.databinding.ActivityCreateAccountBinding;
import com.ousl.application_event_management.models.Users;

import static android.content.ContentValues.TAG;

public class CreateAccountActivity extends AppCompatActivity {

    ActivityCreateAccountBinding binding;
    private FirebaseAuth createAccountAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    Button navigation_sign_organization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_account);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Firebase initiate
        createAccountAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(CreateAccountActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("Creating your account.");

        // button create account function (Creating an account on database)
        binding.buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.inputCreateName.getText().toString().isEmpty() &&
                    !binding.inputCreateEmail.getText().toString().isEmpty() &&
                    !binding.inputCreatePassword.getText().toString().isEmpty() &&
                    !binding.inputCreatePhoneNo.getText().toString().isEmpty())
                {
                    progressDialog.show();
                    createAccountAuth.createUserWithEmailAndPassword(binding.inputCreateEmail.getText().toString(), binding.inputCreatePassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if(task.isSuccessful()){
                                        Users user = new Users(binding.inputCreateEmail.getText().toString(), binding.inputCreatePassword.getText().toString());
                                        String id = task.getResult().getUser().getUid();
                                        database.getReference().child("users").child(id).setValue(user);
                                        Toast.makeText(CreateAccountActivity.this, "Your account created", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(CreateAccountActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        Log.e(TAG, task.getException().toString());
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(CreateAccountActivity.this, "Relevant Fields must required", Toast.LENGTH_SHORT).show();
                }
            }
        });

        navigation_sign_organization = findViewById(R.id.navigation_sign_organization);

        navigation_sign_organization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountActivity.this, CreateAccountOrganizationActivity.class);
                startActivity(intent);
            }
        });
    }
}