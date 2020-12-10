package com.example.foodin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SigninActivity extends AppCompatActivity {

    TextInputLayout email, password;
    Button signin, signup, forgot_password;
    ImageView logo;
    TextView foodin, desc;
    ProgressBar progressBar;

    RelativeLayout rl_pb;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signin);
        init();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }


    public void init() {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        forgot_password = findViewById(R.id.forgot_password);
        signin = findViewById(R.id.signin);
        signup = findViewById(R.id.signup);
        progressBar = findViewById(R.id.progressBar);
        logo = findViewById(R.id.logo);
        foodin = findViewById(R.id.foodin);
        desc = findViewById(R.id.desc);
        rl_pb = findViewById(R.id.rl_pb);

        ll = findViewById(R.id.ll);

    }

    //Validate email
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

    //validate password
    private boolean validatePassword() {
        String val = password.getEditText().getText().toString();
        if (val.isEmpty()) {
            password.setError("Field cannot be Empty");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }


    //OnCLICK button for SignIn
    public void signin(View v) {

        if (!validateEmail() | !validatePassword()) {
            return;
        } else {
            LogInUser();
        }
    }

    //Logging user In
    private void LogInUser() {
        //Storing email and password in string
        String UserEmail = email.getEditText().getText().toString().trim();
        String UserPassword = password.getEditText().getText().toString().trim();

        //progressbar
        rl_pb.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(UserEmail, UserPassword).addOnCompleteListener(SigninActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                firebaseUser = firebaseAuth.getCurrentUser();
                if (task.isSuccessful()) {

                    email.setError(null);
                    email.setErrorEnabled(false);

                    password.setError(null);
                    password.setErrorEnabled(false);

                    if (firebaseUser.isEmailVerified()) {
                        startActivity(new Intent(SigninActivity.this, HomeActivity.class));
                        Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        firebaseUser.sendEmailVerification();
                        Toast.makeText(getApplicationContext(), "Email not verified. \nVerification email sent again.", Toast.LENGTH_LONG).show();
                        firebaseAuth.signOut();
                    }

                    rl_pb.setVisibility(View.GONE);
                } else {
                    //Countering Errors if task is not successful
                    rl_pb.setVisibility(View.GONE);

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException invalidEmail) { //invalid email
                        email.setError("Email Id doesn't exist");
                        email.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                        password.setError("Wrong Password");
                        password.requestFocus();
                    } catch (Exception e) {
                        Toast.makeText(SigninActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


/*Logging in using RealTime Database
    private void isUser() {
        progressBar.setVisibility(View.VISIBLE);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        Query checkUser = reference.orderByChild("phoneno").equalTo(UserPhoneno);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    phoneno.setError(null);
                    phoneno.setErrorEnabled(false);

                    String passwordFromFB = snapshot.child(UserPhoneno).child("password").getValue(String.class);

                    if (passwordFromFB.equals(UserPassword)) {

                        password.setError(null);
                        password.setErrorEnabled(false);

                        String nameFromFB = snapshot.child(UserPhoneno).child("name").getValue(String.class);
                        String phonenoFromFB = snapshot.child(UserPhoneno).child("phoneno").getValue(String.class);
                        String addressFromFB = snapshot.child(UserPhoneno).child("address").getValue(String.class);
                        String emailFromFB = snapshot.child(UserPhoneno).child("email").getValue(String.class);

                        Intent i = new Intent(getApplicationContext(), Profile.class);
                        i.putExtra("name", nameFromFB);
                        i.putExtra("phoneno", phonenoFromFB);
                        i.putExtra("address", addressFromFB);
                        i.putExtra("email", emailFromFB);
                        i.putExtra("password", passwordFromFB);
                        startActivity(i);
                        finish();

                    } else {
                        progressBar.setVisibility(View.GONE);
                        password.setError("Wrong Password");
                        password.requestFocus();

                    }

                } else {
                    progressBar.setVisibility(View.GONE);
                    phoneno.setError("No such User exists");
                    phoneno.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/

    //shared animation and Intent to SignUp activity
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void signup(View v) {
        Intent i = new Intent(SigninActivity.this, SignupActivity.class);

        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(ll, "ll");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SigninActivity.this, pairs);
        startActivity(i, options.toBundle());
    }

    //forgot password
    public void forgot_password(View view) {
        startActivity(new Intent(getApplicationContext(), ForgotPassword.class));
    }

    //Keep user LoggedIn
    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseUser != null && firebaseUser.isEmailVerified()) {
            startActivity(new Intent(SigninActivity.this, HomeActivity.class));
            finish();
        }
    }

    //Exit if back button is clicked twice
    private static long back_pressed_time;

    @Override
    public void onBackPressed() {
        if (back_pressed_time + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else
            Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        back_pressed_time = System.currentTimeMillis();
    }
}