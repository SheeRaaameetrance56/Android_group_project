package com.ousl.application_event_management.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ousl.application_event_management.databinding.ActivityEditProfileBinding;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private EditText name, email, phoneNo, currentPassword, newPassword;
    private Button editButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        name = binding.editProfileName;
        email = binding.editProfileEmail;
        phoneNo = binding.editProfilePhone;
        currentPassword = binding.editProfileCuurentPassword;
        newPassword = binding.editProfileNewPassword;
        editButton = binding.editProfileEditBtn;
        cancelButton = binding.editProfileCancelBtn;

        FirebaseAuth authProfile = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(authProfile.getCurrentUser().getUid());
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(snapshot.child("name").getValue(String.class));
                email.setText(snapshot.child("email").getValue(String.class));
                phoneNo.setText(snapshot.child("phoneNo").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileActivity.this, "Something went wrong..!", Toast.LENGTH_SHORT).show();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = binding.editProfileName.getText().toString().trim();
                String newEmail = binding.editProfileEmail.getText().toString().trim();
                String newPhone = binding.editProfilePhone.getText().toString().trim();

                reference.child("email").setValue(newEmail);
                reference.child("name").setValue(newName);
                reference.child("phoneNo").setValue(newPhone);

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String currentPass = snapshot.child("password").toString();
                        if(currentPass.equals(currentPassword.getText().toString())){
                            reference.child("password").setValue(newPassword.getText().toString());
                        }
                        else{
                            Toast.makeText(EditProfileActivity.this, "Please enter current password on current password field.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, ProfileViewActivity.class));
                finish();
            }
        });
    }
}