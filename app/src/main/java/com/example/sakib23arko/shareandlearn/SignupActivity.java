package com.example.sakib23arko.shareandlearn;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;


public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText emailEditText,passwordEditText;
    private Button signupButton;
    private FirebaseAuth mAuth;
    private ProgressBar progressBarSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailEditText =(EditText) findViewById(R.id.emailSignupId);
        passwordEditText =(EditText) findViewById(R.id.passwordSignupId);
        signupButton =(Button) findViewById(R.id.signupButtonIdSignupPage);
        mAuth = FirebaseAuth.getInstance();
        progressBarSignup =(ProgressBar) findViewById(R.id.progressBarSignupId);

        signupButton.setOnClickListener(this);

    }

    private void registerUser()
    {
        String email =emailEditText.getText().toString().trim();//trim removes leaging and trailing zeros
        String password =passwordEditText.getText().toString().trim();
        if(email.isEmpty())
        {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailEditText.setError("Please enter a valid email ");
            emailEditText.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            passwordEditText.setError("password is required");
            passwordEditText.requestFocus();
            return;
        }
        if(password.length()<6)
        {
            passwordEditText.setError("Minimum password length should be 6");
            passwordEditText.requestFocus();
            return;
        }
        progressBarSignup.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBarSignup.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Signup completed",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SignupActivity.this,Homepage.class));
                }
                else if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                    Toast.makeText(getApplicationContext(), "This email is already registered", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Unexpected error occured",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.signupButtonIdSignupPage)
        {
            registerUser();
        }

    }
}
