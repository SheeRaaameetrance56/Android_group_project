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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ousl.application_event_management.databinding.ActivityCreateAccountBinding;
import com.ousl.application_event_management.models.Users;

import static android.content.ContentValues.TAG;

public class CreateAccountActivity extends AppCompatActivity {

    ActivityCreateAccountBinding binding;
    private FirebaseAuth createAccountAuth;
    FirebaseDatabase database;
    DatabaseReference reference;

    ProgressDialog progressDialog;

    Button navigation_sign_organization;

    String name, email, password, phoneNo;

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
        progressDialog.setMessage("Wait a minute....");

        // button create account function (Creating an account on database)
        binding.buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = binding.inputCreateName.getText().toString();
                email = binding.inputCreateEmail.getText().toString();
                password = binding.inputCreatePassword.getText().toString();
                phoneNo = binding.inputCreatePhoneNo.getText().toString();

                if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !phoneNo.isEmpty()) {
                    progressDialog.show();
                    createAccountAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(CreateAccountActivity.this, "Account created successfully.", Toast.LENGTH_SHORT).show();
                                        FirebaseUser currentUser = createAccountAuth.getCurrentUser();
                                        if (currentUser != null) {
                                            String uid = currentUser.getUid();
                                            Users user = new Users(name, email, password, phoneNo);
                                            database = FirebaseDatabase.getInstance();
                                            reference = database.getReference("users");
                                            reference.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    binding.inputCreateName.setText("");
                                                    binding.inputCreateEmail.setText("");
                                                    binding.inputCreatePassword.setText("");
                                                    binding.inputCreatePhoneNo.setText("");
                                                }
                                            });
//                                            database.getReference().child("users").child(uid).setValue(user);
                                        }
                                    }
                                    else{
                                        Toast.makeText(CreateAccountActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                    progressDialog.dismiss();
                                }
                            });
                }
                else{
                    Toast.makeText(CreateAccountActivity.this, "All Fields must required for create an account", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // navigate to the organization account creating screen
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