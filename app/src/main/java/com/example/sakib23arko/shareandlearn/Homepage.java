
package com.example.sakib23arko.shareandlearn;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class Homepage extends AppCompatActivity {


    private Button createNewPostButton;
    ArrayList<infoOfUser> userlist;
    ListView ListUserView;
    Bundle bundle;
    TextView continueReading;
    DatabaseReference userDatabase;
    private FirebaseAuth mAuth;

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    ArrayList<userNameAndImage> uNameAndImages;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        userDatabase = FirebaseDatabase.getInstance().getReference("USER");
        mAuth=FirebaseAuth.getInstance();

        mDrawerList = (ListView)findViewById(R.id.navList); // List View Ta
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        uNameAndImages = new ArrayList<>();
        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        createNewPostButton = findViewById(R.id.CreateNewPostID);
        userlist = new ArrayList<>();
        ListUserView = findViewById(R.id.ListUserViewID);
        continueReading = findViewById(R.id.continueReadingid);



        ListUserView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                Intent intent = new Intent(Homepage.this, PostDetails.class);
                infoOfUser itemClicked = userlist.get(position);
                intent.putExtra("Title", itemClicked.getTitle());
                intent.putExtra("Timee", itemClicked.getDateTime());
                intent.putExtra("Description", itemClicked.getDescription());
                intent.putExtra("Chosen",itemClicked.getchosenFileUrl());
                intent.putExtra("Ext",itemClicked.getExt());
                intent.putExtra("UID",itemClicked.getUserUid());
                intent.putExtra("Name",itemClicked.getUserName());
                intent.putExtra("vis","0");
                Log.d("ppppp",itemClicked.getchosenFileUrl());
                startActivity(intent);

            }
        });


        createNewPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, CreateNewPost.class);
                intent.putExtra("Title", "");
                intent.putExtra("Timee", "");
                intent.putExtra("Description", "");
                intent.putExtra("Tag","");
                intent.putExtra("Chosen","");
                intent.putExtra("Ext","");
                intent.putExtra("UID","");
                intent.putExtra("Name","");
                intent.putExtra("PostID","");
                intent.putExtra("vis","From Homepage");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userlist.clear();
                for (DataSnapshot X : dataSnapshot.getChildren()) {
                    userlist.add(X.getValue(infoOfUser.class));
                }
                Collections.reverse(userlist);
                UserList adapter = new UserList(Homepage.this, userlist);
                ListUserView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
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
        if (id == R.id.action_settings) { return true; }
        if (mDrawerToggle.onOptionsItemSelected(item)) { return true; }
        return super.onOptionsItemSelected(item);
    }
}





