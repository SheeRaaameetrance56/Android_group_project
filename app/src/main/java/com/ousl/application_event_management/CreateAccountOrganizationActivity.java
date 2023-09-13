package com.ousl.application_event_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
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
import com.ousl.application_event_management.databinding.ActivityCreateAccountOrganizationBinding;
import com.ousl.application_event_management.models.Users;
import com.ousl.application_event_management.models.UsersOrganization;

public class CreateAccountOrganizationActivity extends AppCompatActivity {

    ActivityCreateAccountOrganizationBinding binding;
    private FirebaseAuth createAccountAuth;
    FirebaseDatabase database;
    DatabaseReference reference;

    ProgressDialog progressDialog;

    String nameOrg, emailOrg, phoneNumberOrg, addressOrg, passwordOrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Used view binding for screen connectivity and access.
        binding = ActivityCreateAccountOrganizationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Firebase authentication and realtime database initiate
        createAccountAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // Progress dialog shows progress while creating account
        progressDialog = new ProgressDialog(CreateAccountOrganizationActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("Wait a minute....");

        // button create account function (Creating an account on database)
        binding.buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get text on text inputs input by the user
                nameOrg = binding.inputOrganizationName.getText().toString();
                emailOrg = binding.inputOrganizationEmail.getText().toString();
                phoneNumberOrg = binding.inputOrganizationPhone.getText().toString();
                addressOrg = binding.inputOrganizationAddress.getText().toString();
                passwordOrg = binding.inputOrganizationPassword.getText().toString();

                // validation conditions.
                if(!nameOrg.isEmpty() && !emailOrg.isEmpty() && !addressOrg.isEmpty() &&!passwordOrg.isEmpty() && !phoneNumberOrg.isEmpty()) {
                    progressDialog.show();

                    // creating google authentication and task performing on complete
                    createAccountAuth.createUserWithEmailAndPassword(emailOrg, passwordOrg)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // If successful authentication created.
                                    if (task.isSuccessful()) {
                                        Toast.makeText(CreateAccountOrganizationActivity.this, "Account created successfully.", Toast.LENGTH_SHORT).show();
                                        // save authentication details on realtime database.
                                        FirebaseUser currentUser = createAccountAuth.getCurrentUser();
                                        if (currentUser != null) {
                                            String uid = currentUser.getUid();
                                            UsersOrganization userOrg = new UsersOrganization(nameOrg, emailOrg, phoneNumberOrg, addressOrg, passwordOrg);
                                            database = FirebaseDatabase.getInstance();
                                            reference = database.getReference("organization_users");
                                            reference.child(uid).setValue(userOrg).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    binding.inputOrganizationName.setText("");
                                                    binding.inputOrganizationEmail.setText("");
                                                    binding.inputOrganizationPhone.setText("");
                                                    binding.inputOrganizationAddress.setText("");
                                                    binding.inputOrganizationPassword.setText("");
                                                }
                                            });
//                                            database.getReference().child("users").child(uid).setValue(user);
                                        }
                                    }
                                    else{
                                        Toast.makeText(CreateAccountOrganizationActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                    progressDialog.dismiss();
                                }
                            });
                }
                else{
                    Toast.makeText(CreateAccountOrganizationActivity.this, "All Fields must required for create an account", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}