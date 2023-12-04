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
import com.ousl.application_event_management.controllers.AuthenticationManager;
import com.ousl.application_event_management.controllers.DataBaseManager;
import com.ousl.application_event_management.databinding.ActivityCreateAccountBinding;
import com.ousl.application_event_management.models.Users;

import org.checkerframework.checker.units.qual.A;

public class CreateAccountActivity extends AppCompatActivity {

    private ActivityCreateAccountBinding binding;
//    private FirebaseAuth createAccountAuth;
//    private FirebaseUser firebaseUser;
//    private DatabaseReference reference;
    private ProgressDialog progressDialog;
    private Button navigation_sign_organization;
    private String name, email,  phoneNo;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = binding.inputCreateName.getText().toString();
                email = binding.inputCreateEmail.getText().toString();
                password = binding.inputCreatePassword;
                phoneNo = binding.inputCreatePhoneNo.getText().toString();

//                createAccountAuth = FirebaseAuth.getInstance();
//                firebaseUser = createAccountAuth.getCurrentUser();

                if(!name.isEmpty() && !email.isEmpty() && !password.toString().isEmpty() && !phoneNo.isEmpty()) {

                    createAccountTask(name, email, password.getText().toString(), phoneNo);

                }
                else{
                    Toast.makeText(CreateAccountActivity.this, "All Fields must required for create an account", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
        progressDialog.show();

        AuthenticationManager authManager = AuthenticationManager.getInstance();
        authManager.createUserWithEmailAndPassword(email, password, name, phoneNo, new AuthenticationManager.AuthenticationListener() {
            @Override
            public void onAccountCreated() {
                progressDialog.dismiss();
                Toast.makeText(CreateAccountActivity.this, "Account created successfully.", Toast.LENGTH_SHORT).show();
                binding.inputCreateName.setText("");
                binding.inputCreateEmail.setText("");
                binding.inputCreatePhoneNo.setText("");
                binding.inputCreatePassword.setText("");
            }

            @Override
            public void onAccountCreationFailed() {
                progressDialog.dismiss();
                Toast.makeText(CreateAccountActivity.this, "Failed to create account.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                progressDialog.dismiss();
                Toast.makeText(CreateAccountActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        });

//        createAccountAuth = FirebaseAuth.getInstance();
//        firebaseUser = createAccountAuth.getCurrentUser();
//
//        createAccountAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            FirebaseUser currentUser = createAccountAuth.getCurrentUser();
//                            if (currentUser != null) {
//                                String uid = currentUser.getUid();
//                                Users user = new Users(name, email, phoneNo);
//
//                                DataBaseManager dataBaseManager = DataBaseManager.getInstance();
//                                reference = dataBaseManager.getReferenceUser();
//                                reference.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        progressDialog.dismiss();
//                                        Toast.makeText(CreateAccountActivity.this, "Account created successfully.", Toast.LENGTH_SHORT).show();
//                                        binding.inputCreateName.setText("");
//                                        binding.inputCreateEmail.setText("");
//                                        binding.inputCreatePhoneNo.setText("");
//
//                                        if(firebaseUser != null){
//                                            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    Toast.makeText(CreateAccountActivity.this, "Email verification sent", Toast.LENGTH_SHORT).show();
//                                                }
//                                            }).addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//                                                    Toast.makeText(CreateAccountActivity.this, "Failed to send Email verification", Toast.LENGTH_SHORT).show();
//                                                }
//                                            });
//                                        }
//                                    }
//                                });
//                            }
//                        }
//                        else{
//                            Toast.makeText(CreateAccountActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                        }
//                        progressDialog.dismiss();
//                    }
//                });

    }

}