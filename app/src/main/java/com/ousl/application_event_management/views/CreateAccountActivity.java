package com.ousl.application_event_management.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ousl.application_event_management.databinding.ActivityCreateAccountBinding;
import com.ousl.application_event_management.models.Users;

public class CreateAccountActivity extends AppCompatActivity {

    private ActivityCreateAccountBinding binding;
    private FirebaseAuth createAccountAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ProgressDialog progressDialog;
    private Button navigation_sign_organization;
    private String name, email,  phoneNo;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Used view binding for screen connectivity and access.
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // button create account function (Creating an account on database)
        binding.buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get text on text inputs input by the user
                name = binding.inputCreateName.getText().toString();
                email = binding.inputCreateEmail.getText().toString();
                password = binding.inputCreatePassword;
                phoneNo = binding.inputCreatePhoneNo.getText().toString();

                createAccountAuth = FirebaseAuth.getInstance();
                firebaseUser = createAccountAuth.getCurrentUser();

                // validation conditions.
                if(!name.isEmpty() && !email.isEmpty() && !password.toString().isEmpty() && !phoneNo.isEmpty()) {

                    createAccountTask(name, email, password.toString(), phoneNo);

                }
                else{
                    Toast.makeText(CreateAccountActivity.this, "All Fields must required for create an account", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // navigate to the organization account creating screen
        navigation_sign_organization = binding.navigationSignOrganization;
        navigation_sign_organization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountActivity.this, CreateAccountOrganizationActivity.class);
                startActivity(intent);
            }
        });

    }

    public void createAccountTask(String name, String email, String password, String phoneNo){
        progressDialog = new ProgressDialog(CreateAccountActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We are creating your account");
        // creating google authentication and task performing on complete
        progressDialog.show();

        database = FirebaseDatabase.getInstance();
        createAccountAuth = FirebaseAuth.getInstance();
        firebaseUser = createAccountAuth.getCurrentUser();
        createAccountAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If successful authentication created.
                        if (task.isSuccessful()) {
                        // save authentication details on realtime database.
                            FirebaseUser currentUser = createAccountAuth.getCurrentUser();
                            if (currentUser != null) {
                                String uid = currentUser.getUid();
                                Users user = new Users(name, email, phoneNo);
                                database = FirebaseDatabase.getInstance();
                                reference = database.getReference("users");
                                reference.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        Toast.makeText(CreateAccountActivity.this, "Account created successfully.", Toast.LENGTH_SHORT).show();
                                        binding.inputCreateName.setText("");
                                        binding.inputCreateEmail.setText("");
                                        binding.inputCreatePhoneNo.setText("");

                                        if(firebaseUser != null){
                                            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(CreateAccountActivity.this, "Email verification sent", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(CreateAccountActivity.this, "Failed to send Email verification", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                        else{
                            Toast.makeText(CreateAccountActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

}