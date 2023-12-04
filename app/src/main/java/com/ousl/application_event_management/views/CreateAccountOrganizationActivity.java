package com.ousl.application_event_management.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ousl.application_event_management.controllers.AuthenticationManager;
import com.ousl.application_event_management.controllers.DataBaseManager;
import com.ousl.application_event_management.databinding.ActivityCreateAccountBinding;
import com.ousl.application_event_management.databinding.ActivityCreateAccountOrganizationBinding;
import com.ousl.application_event_management.models.UsersOrganization;

public class CreateAccountOrganizationActivity extends AppCompatActivity {

    ActivityCreateAccountOrganizationBinding binding;
    private FirebaseAuth createAccountAuth;
    DatabaseReference reference;

    ProgressDialog progressDialog;

    String nameOrg, emailOrg, phoneNumberOrg, addressOrg, passwordOrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountOrganizationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createAccountAuth = FirebaseAuth.getInstance();

        binding.buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameOrg = binding.inputOrganizationName.getText().toString();
                emailOrg = binding.inputOrganizationEmail.getText().toString();
                phoneNumberOrg = binding.inputOrganizationPhone.getText().toString();
                addressOrg = binding.inputOrganizationAddress.getText().toString();
                passwordOrg = binding.inputOrganizationPassword.getText().toString();

                if(!nameOrg.isEmpty() && !emailOrg.isEmpty() && !addressOrg.isEmpty() &&!passwordOrg.isEmpty() && !phoneNumberOrg.isEmpty()) {
                    createAccount();
//                    progressDialog.show();
//                    // creating google authentication and task performing on complete
//                    createAccountAuth.createUserWithEmailAndPassword(emailOrg, passwordOrg)
//                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    // If successful authentication created.
//                                    if (task.isSuccessful()) {
//                                        Toast.makeText(CreateAccountOrganizationActivity.this, "Account created successfully.", Toast.LENGTH_SHORT).show();
//                                        // save authentication details on realtime database.
//                                        FirebaseUser currentUser = createAccountAuth.getCurrentUser();
//                                        if (currentUser != null) {
//                                            String uid = currentUser.getUid();
//                                            UsersOrganization userOrg = new UsersOrganization(nameOrg, emailOrg, phoneNumberOrg, addressOrg);
//                                            DataBaseManager dataBaseManager = DataBaseManager.getInstance();
//                                            reference = dataBaseManager.getReferenceOrgUser();
//                                            reference.child(uid).setValue(userOrg).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//
//                                                }
//                                            });
////                                            database.getReference().child("users").child(uid).setValue(user);
//                                        }
//                                    }
//                                    else{
//                                        Toast.makeText(CreateAccountOrganizationActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                                    }
//                                    progressDialog.dismiss();
//                                }
//                            });
                }
                else{
                    Toast.makeText(CreateAccountOrganizationActivity.this, "All Fields must required for create an account", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void createAccount(){
        progressDialog = new ProgressDialog(CreateAccountOrganizationActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We are creating your account");
        progressDialog.show();
        AuthenticationManager authManager = AuthenticationManager.getInstance();
        authManager.createOrgUserWithEmailAndPassword(emailOrg, passwordOrg, nameOrg, phoneNumberOrg, addressOrg, new AuthenticationManager.AuthenticationListener() {
            @Override
            public void onAccountCreated() {
                progressDialog.dismiss();
                binding.inputOrganizationName.setText("");
                binding.inputOrganizationEmail.setText("");
                binding.inputOrganizationPhone.setText("");
                binding.inputOrganizationAddress.setText("");
                binding.inputOrganizationPassword.setText("");
                Toast.makeText(CreateAccountOrganizationActivity.this, "Account Created successfully.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAccountCreationFailed() {
                progressDialog.dismiss();
                binding.inputOrganizationEmail.setText("");
                binding.inputOrganizationPassword.setText("");
                Toast.makeText(CreateAccountOrganizationActivity.this, "Failed to create account", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                progressDialog.dismiss();
                binding.inputOrganizationEmail.setText("");
                binding.inputOrganizationPassword.setText("");
                Toast.makeText(CreateAccountOrganizationActivity.this, "Failed to authenticate", Toast.LENGTH_SHORT).show();
            }
        });
    }
}