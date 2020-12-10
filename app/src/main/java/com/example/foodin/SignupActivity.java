package com.example.foodin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodin.HelperClass.UserHelperClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    TextInputLayout name, phoneno, address, email, password;
    Button signup, signin;

    ImageView logo;
    TextView foodin, desc;

    RelativeLayout rl_pb;

    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);
        init();
    }

    private void init() {
        name = findViewById(R.id.name);
        phoneno = findViewById(R.id.phoneno);
        address = findViewById(R.id.address);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signup = findViewById(R.id.signup);
        signin = findViewById(R.id.signin);

        logo = findViewById(R.id.logo);
        foodin = findViewById(R.id.foodin);
        desc = findViewById(R.id.desc);
        rl_pb = findViewById(R.id.rl_pb);

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

    //Password validations
    private boolean validatePassword() {
        String val = password.getEditText().getText().toString();
        String passwordPattern = "^" +
//                "(?=.*[0-9])" +
//                "(?=.*[a-z])" +
//                "(?=.*[A-Z])" +
//                "(?=.*[a-zA-Z])" +
//                "(?=.*[*.!@$%^&(){}[]:;<>,.?/~_+-=|\\])" +
                ".{6,}" +
                "$";
        String noWhiteSpace = "\\A\\w{6,}\\z";
        if (val.isEmpty()) {
            password.setError("Field cannot be Empty");
            return false;
        } else if (val.length() < 6) {
            password.setError("Password is too weak");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            password.setError("No white spaces allowed");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }


    //OnCLICK for button SignUp
    public void signup(View v) {

        if (!validateName() | !validatePhoneno() | !validateAddress() | !validateEmail() | !validatePassword()) {
            return;
        } else {
            registerUser();
        }
    }

    //register user cdde
    private void registerUser() {

        //getting values from TextInputLayout
        String sname = name.getEditText().getText().toString();
        String sphoneno = phoneno.getEditText().getText().toString();
        String saddress = address.getEditText().getText().toString();
        String semail = email.getEditText().getText().toString();
        String spassword = password.getEditText().getText().toString();
        String sCartTotalAmount = "0";


        //progressbar
        rl_pb.setVisibility(View.VISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();

        //Creating User
        firebaseAuth.createUserWithEmailAndPassword(semail, spassword).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    email.setError(null);
                    email.setErrorEnabled(false);

                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    assert firebaseUser != null;
                    UserId = firebaseUser.getUid();

                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference("users").child(UserId);

                    //Creating object and passing values to UserHelperClass we can also use Hash instead of another class
                    UserHelperClass userHelper = new UserHelperClass(sname, sphoneno, saddress, semail, sCartTotalAmount);

                    databaseReference.setValue(userHelper).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            rl_pb.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                startActivity(new Intent(getApplicationContext(), SigninActivity.class));
                                firebaseUser.sendEmailVerification();
                                Toast.makeText(getApplicationContext(), "Verification Email Sent \nPlease Verify and SignIn", Toast.LENGTH_LONG).show();
                                finishAffinity();
                            } else {
                                Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    firebaseAuth.signOut();
                } else {
                    rl_pb.setVisibility(View.GONE);
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthUserCollisionException emailExists) {
                        email.setError("Email Id already in use");
                        email.requestFocus();
                    } catch (Exception e) {
                        Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    public void signin(View v) {
        onBackPressed();
    }
}