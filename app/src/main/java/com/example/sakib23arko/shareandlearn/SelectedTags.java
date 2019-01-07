package com.example.sakib23arko.shareandlearn;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectedTags extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private RecyclerView SelectedTagsRecyclerView;
    private RecyclerView.Adapter SelectedTagsAdapter;
    private RecyclerView.LayoutManager SelectedTagsLayoutManager;
    private DatabaseReference userDatabaseRef;
    private List<infoOfUser> userlist;

    String SelectedTagName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_tags);

        userDatabaseRef = FirebaseDatabase.getInstance().getReference("USER");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        userlist= new ArrayList<>();

        SelectedTagName= getIntent().getStringExtra("FoundTag");

        //this is for recycler view of user post collection
        SelectedTagsRecyclerView = findViewById(R.id.selectedTagsRecyclerViewID);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        SelectedTagsRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        SelectedTagsLayoutManager = new LinearLayoutManager(this);
        SelectedTagsRecyclerView.setLayoutManager(SelectedTagsLayoutManager);
        userDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userlist.clear();
                for (DataSnapshot X : dataSnapshot.getChildren()) {
                    if(X.getValue(infoOfUser.class).getTag().equals(SelectedTagName))
                        userlist.add( X.getValue(infoOfUser.class));
                }
                Collections.reverse(userlist);
                // specify an adapter (see also next example)
                SelectedTagsAdapter = new userPostAdapter(SelectedTags.this,userlist);
                ((userPostAdapter) SelectedTagsAdapter).setVis("from SelectedTags");
                SelectedTagsRecyclerView.setAdapter(SelectedTagsAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
