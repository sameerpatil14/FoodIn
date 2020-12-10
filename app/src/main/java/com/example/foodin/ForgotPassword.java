package com.example.foodin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

public class ForgotPassword extends AppCompatActivity {

    TextInputLayout email;
    Button changePassword;
    TextView label;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        email = findViewById(R.id.email);
        changePassword = findViewById(R.id.changePassword);
        label = findViewById(R.id.label);
    }

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

    public void changePassword(View view) {

        if (!validateEmail()) {
            return;
        } else {
            firebaseAuth.fetchSignInMethodsForEmail(email.getEditText().getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                    if (task.getResult().getSignInMethods().isEmpty()) {
                        email.setError("Email Id doesn't exist");
                        label.setText("");
                    } else {

                        email.setError(null);
                        email.setErrorEnabled(false);

                        firebaseAuth.sendPasswordResetEmail(email.getEditText().getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    label.setText("Password Reset email has been sent to your email id");
                                }else{
                                    label.setText(task.getException().getMessage());
                                }

                            }
                        });
                    }
                }
            });

        }

    }
}