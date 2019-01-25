
package com.example.sakib23arko.shareandlearn;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Homepage extends AppCompatActivity {


    private Button createNewPostButton;

    Bundle bundle;
    TextView continueReading;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    ArrayList<userNameAndImage> uNameAndImages;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    private RecyclerView homepageRecyclerView;
    private RecyclerView.Adapter homepageAdapter;
    private RecyclerView.LayoutManager homepageLayoutManager;
    private DatabaseReference userDatabaseRef, profileInfoDatabaseRef;
    private List<infoOfUser> userlist,userListForShow;

    String[] tagCollectoin;
    private int found = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        userDatabaseRef = FirebaseDatabase.getInstance().getReference("USER");
        profileInfoDatabaseRef = FirebaseDatabase.getInstance().getReference("userPersonalInfo");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        mDrawerList = (ListView) findViewById(R.id.navList); // List View Ta
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        uNameAndImages = new ArrayList<>();
        tagCollectoin = getResources().getStringArray(R.array.Tags);
        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        createNewPostButton = findViewById(R.id.CreateNewPostID);
        userlist = new ArrayList<>();
        userListForShow = new ArrayList<>();
        continueReading = findViewById(R.id.continueReadingid);
        //this is for recycler view of user post collection
        homepageRecyclerView = findViewById(R.id.homepageRecyclerViewID);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        homepageRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        homepageLayoutManager = new LinearLayoutManager(this);
        homepageRecyclerView.setLayoutManager(homepageLayoutManager);
        userDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userlist.clear();
                userListForShow.clear();
                for (DataSnapshot X : dataSnapshot.getChildren()) {
                    final infoOfUser info = X.getValue(infoOfUser.class);
                    userlist.add(info);
                }

                Collections.reverse(userlist);
                final int noOfPost = userlist.size();
                for (int i = 0; i < userlist.size(); i++) {
                    final int finalI = i;
                    profileInfoDatabaseRef.child(user.getUid()).child("tagName").child(userlist.get(i).getTag()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            found++;
                            if (dataSnapshot.getValue() == null || dataSnapshot.getValue().toString().equals("1"))
                                userListForShow.add(userlist.get(finalI));


                            if (found == noOfPost) {
                                // specify an adapter (see also next example)
                                homepageAdapter = new userPostAdapter(Homepage.this, userListForShow);
                                ((userPostAdapter) homepageAdapter).setVis("from homepage");
                                homepageRecyclerView.setAdapter(homepageAdapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        createNewPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, CreateNewPost.class);
                intent.putExtra("Title", "");
                intent.putExtra("Timee", "");
                intent.putExtra("Description", "");
                intent.putExtra("Tag", "");
                intent.putExtra("Chosen", "");
                intent.putExtra("Ext", "");
                intent.putExtra("UID", "");
                intent.putExtra("Name", "");
                intent.putExtra("PostID", "");
                intent.putExtra("vis", "From Homepage");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    private void addDrawerItems() {
        String UserName = mAuth.getCurrentUser().getDisplayName();
        final String userImage = mAuth.getCurrentUser().getUid();
        uNameAndImages.clear();
        uNameAndImages.add(new userNameAndImage(UserName, userImage));
        CustomAdapter customAdapter = new CustomAdapter(this, uNameAndImages);
        mDrawerList.setAdapter(customAdapter);

    }


    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}




