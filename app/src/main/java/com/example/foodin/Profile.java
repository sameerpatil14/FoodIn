package com.example.foodin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
public class Profile extends AppCompatActivity {

    TextInputLayout name, phoneno, address, email;
    TextView profileName, profileEmail, verify;
    Button update;
    String Tag;
    String user_name, user_phoneno, user_address, user_email;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());

        loadData();

    }

    private void init() {
        name = findViewById(R.id.name);
        phoneno = findViewById(R.id.phoneno);
        address = findViewById(R.id.address);
        email = findViewById(R.id.email);
        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        update = findViewById(R.id.update);
        verify = findViewById(R.id.verify);
        toolbar = findViewById(R.id.toolbar);

    }


    private void loadData() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                user_name = snapshot.child("name").getValue(String.class);
                user_phoneno = snapshot.child("phoneno").getValue(String.class);
                user_address = snapshot.child("address").getValue(String.class);
                user_email = snapshot.child("email").getValue(String.class);

                profileName.setText(user_name);
                profileEmail.setText(user_email);
                name.getEditText().setText(user_name);
                phoneno.getEditText().setText(user_phoneno);
                address.getEditText().setText(user_address);
                email.getEditText().setText(user_email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(Tag, error.getMessage());
            }
        });
    }

    //name validations
    private boolean validateName() {
        String val = name.getEditText().getText().toString();
        if (val.isEmpty()) {
            name.setError("Field cannot be Empty");
            return false;
        } else {
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }

    //Phone No validations
    private boolean validatePhoneno() {
        String val = phoneno.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\d{10,10}\\z";
        if (val.isEmpty()) {
            phoneno.setError("Field cannot be Empty");
            return false;
        } else if (val.length() < 10) {
            phoneno.setError("Enter 10 digit mobile no.");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            phoneno.setError("Only digits are allowed");
            return false;
        } else {
            phoneno.setError(null);
            phoneno.setErrorEnabled(false);
            return true;
        }
    }

    //address validations
    private boolean validateAddress() {
        String val = address.getEditText().getText().toString();
        if (val.isEmpty()) {
            address.setError("Field cannot be Empty");
            return false;
        } else {
            address.setError(null);
            address.setErrorEnabled(false);
            return true;
        }
    }

    //Email validations
    private boolean validateEmail() {
        String val = email.getEditText().getText().toString();
        if (val.isEmpty()) {
            email.setError("Field cannot be Empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(val).matches()) {
            email.setError("Enter Valid Email");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }


    //OnCLICK for button SignUp
    public void update(View v) {
        if (!validateName() | !validatePhoneno() | !validateAddress() | !validateEmail()) {
            return;
        } else {
            if (isNameChanged() || isPhonenoChanged() || isAddressChanged() || isEmailChanged()) {
                Toast.makeText(getApplicationContext(), "Data has been updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "There is no change in data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isNameChanged() {
        if (!user_name.equals(name.getEditText().getText().toString())) {
            reference.child("name").setValue(name.getEditText().getText().toString());
            user_name = name.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isPhonenoChanged() {
        if (!user_phoneno.equals(phoneno.getEditText().getText().toString())) {
            reference.child("phoneno").setValue(phoneno.getEditText().getText().toString());
            user_phoneno = phoneno.getEditText().getText().toString();

            return true;
        } else {
            return false;
        }
    }

    private boolean isAddressChanged() {
        if (!user_address.equals(address.getEditText().getText().toString())) {
            reference.child("address").setValue(address.getEditText().getText().toString());
            user_address = address.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isEmailChanged() {

        if (!user_email.equals(email.getEditText().getText().toString())) {

            //updating email in firebase authentication as well
            firebaseUser.updateEmail(email.getEditText().getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //updating email in database
                                reference.child("email").setValue(email.getEditText().getText().toString());
                                user_email = email.getEditText().getText().toString();
                                Toast.makeText(Profile.this, "Verify your Email and SignIn again", Toast.LENGTH_LONG).show();

                                firebaseUser.sendEmailVerification();
                                firebaseAuth.signOut();

                                startActivity(new Intent(getApplicationContext(), SigninActivity.class));
                                finishAffinity();
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                email.getEditText().setText(user_email);
                            }
                        }
                    });

            return true;
        } else {
            return false;
        }
    }

}