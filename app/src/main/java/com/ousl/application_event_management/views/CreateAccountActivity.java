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

                // Check if email is in correct format
                if (!isValidEmail(email)) {
                    Toast.makeText(CreateAccountActivity.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if password has at least 8 characters
                if (password.length() < 8) {
                    Toast.makeText(CreateAccountActivity.this, "Password must have at least 8 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!name.isEmpty() && !email.isEmpty() && !password.toString().isEmpty() && !phoneNo.isEmpty()) {

                    createAccountTask();

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

    public void createAccountTask(){
        progressDialog = new ProgressDialog(CreateAccountActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We are creating your account");
        progressDialog.show();

        AuthenticationManager authManager = AuthenticationManager.getInstance();
        authManager.createUserWithEmailAndPassword(email, password.getText().toString(), name, phoneNo, new AuthenticationManager.AuthenticationListener() {
            @Override
            public void onAccountCreated() {
                progressDialog.dismiss();
                binding.inputCreateName.setText("");
                binding.inputCreateEmail.setText("");
                binding.inputCreatePhoneNo.setText("");
                binding.inputCreatePassword.setText("");
                Toast.makeText(CreateAccountActivity.this, "Account created successfully.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAccountCreationFailed() {
                progressDialog.dismiss();
                binding.inputCreateName.setText("");
                binding.inputCreatePhoneNo.setText("");
                Toast.makeText(CreateAccountActivity.this, "Failed to create account.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                progressDialog.dismiss();
                binding.inputCreateName.setText("");
                binding.inputCreatePhoneNo.setText("");
                Toast.makeText(CreateAccountActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        });

    }
    // Function to validate email format
    private boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}