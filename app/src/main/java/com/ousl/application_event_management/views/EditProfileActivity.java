package com.ousl.application_event_management.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ousl.application_event_management.databinding.ActivityEditProfileBinding;

import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private EditText name, email, phoneNo, currentPassword, newPassword;
    private Button editButton, cancelButton;
    private DatabaseReference reference;
    private FirebaseAuth authProfile;
    private FirebaseUser user;
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

        authProfile = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(authProfile.getCurrentUser().getUid());

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
                try {
                    editDatabaseDetails();
                    editAuthenticationDetails();
                }catch (Exception e){
                    Toast.makeText(EditProfileActivity.this, "Error. Suggest re-authenticate.", Toast.LENGTH_SHORT).show();
                }

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

    public void editDatabaseDetails(){
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

    public void editAuthenticationDetails(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        user.updateEmail(email.getText().toString());
        AuthCredential credential = EmailAuthProvider.getCredential(Objects.requireNonNull(user.getEmail()), currentPassword.getText().toString());
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                user.updatePassword(newPassword.getText().toString());
            }
        });

    }

}