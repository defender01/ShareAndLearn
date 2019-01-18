package com.example.sakib23arko.shareandlearn;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class profileDetails extends AppCompatActivity implements View.OnClickListener {

    private String displayName,university,email;
    List<String> list;
    private TextView displayNameTextView, universityTextView, emailTextView;
    private Button selectTopicsTextView, saveTextView;

    private DatabaseReference userDatabaseRef;
    private FirebaseUser user;



    @Override
    protected void onCreate(Bundle saveTextViewdInstanceState) {
        super.onCreate(saveTextViewdInstanceState);
        setContentView(R.layout.activity_profile_details);

        displayNameTextView = findViewById(R.id.displayNameId);
        universityTextView = findViewById(R.id.universityNameId);
        emailTextView = findViewById(R.id.displayEmailId);
        selectTopicsTextView = findViewById(R.id.selectTopicsId);
        saveTextView = findViewById(R.id.profileDetailSaveId);

        userDatabaseRef = FirebaseDatabase.getInstance().getReference("userPersonalInfo");
        user = FirebaseAuth.getInstance().getCurrentUser();

        list= new ArrayList<>();

        loadUserInformation();

        displayNameTextView.setOnClickListener(this);
        universityTextView.setOnClickListener(this);
        emailTextView.setOnClickListener(this);
        selectTopicsTextView.setOnClickListener(this);
        saveTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == displayNameTextView) {

        }
        if (v == universityTextView) {

        }
        if (v == emailTextView) {

        }
        if (v == selectTopicsTextView) {
            topic_selection topicSelection=new topic_selection();
            startActivity(new Intent(profileDetails.this,topicSelection.getClass()));

        }
        if (v == saveTextView) {
            save();
        }
    }

    private void loadUserInformation() {


        if(user.getDisplayName()!=null)
        {
            displayName=user.getDisplayName();
            displayNameTextView.setText(displayName);
        }

        userDatabaseRef.child(user.getUid()).child("university").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    university=dataSnapshot.getValue().toString();
                    Log.d("univ","university= "+university);
                    universityTextView.setText(university);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        userDatabaseRef.child(user.getUid()).child("email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    email=dataSnapshot.getValue().toString();
                    emailTextView.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void save() {
        displayName = displayNameTextView.getText().toString();
        university= universityTextView.getText().toString();
        email=emailTextView.getText().toString();

        if (user != null && displayName != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build();
            user.updateProfile(profile);
        }
        userDatabaseRef.child(user.getUid()).child("university").setValue(university);
        userDatabaseRef.child(user.getUid()).child("email").setValue(email);
        startActivity(new Intent(profileDetails.this,Homepage.class));
        finish();
    }

}

