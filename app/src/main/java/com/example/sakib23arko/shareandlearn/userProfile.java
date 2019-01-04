package com.example.sakib23arko.shareandlearn;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class userProfile extends AppCompatActivity implements View.OnClickListener {

    private static final int PROFILE_IMAGE = 100;
    private ImageView userImage;
    private TextView addChange;
    private EditText userName;
    private Button saveButton;
    private Uri uriProfileImage;
    private ProgressBar progressBarUserProfileImage;
    private String profileImageUrl;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private RecyclerView userProfileRecyclerView;
    private RecyclerView.Adapter userProfileAdapter;
    private RecyclerView.LayoutManager userProfileLayoutManager;
    private DatabaseReference userDatabaseRef;
    private List<infoOfUser> userlist;

    //----

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    ArrayList<userNameAndImage> uNameAndImages;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    //----//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userDatabaseRef = FirebaseDatabase.getInstance().getReference("USER");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        userlist= new ArrayList<>();

        //----

        mDrawerList = (ListView)findViewById(R.id.navList); // List View Ta
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        uNameAndImages = new ArrayList<>();
        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        //--//


        //this is for recycler view of user post collection
        userProfileRecyclerView = findViewById(R.id.userProfileRecyclerViewID);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        userProfileRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        userProfileLayoutManager = new LinearLayoutManager(this);
        userProfileRecyclerView.setLayoutManager(userProfileLayoutManager);
        userDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userlist.clear();
                for (DataSnapshot X : dataSnapshot.getChildren()) {
                    if(X.getValue(infoOfUser.class).getUserUid().equals(user.getUid()))
                        userlist.add(0, X.getValue(infoOfUser.class));
                }

                // specify an adapter (see also next example)
                userProfileAdapter = new userPostAdapter(userProfile.this,userlist);
                userProfileRecyclerView.setAdapter(userProfileAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        userImage = (ImageView) findViewById(R.id.userImageViewId);
        addChange = (TextView) findViewById(R.id.addChangePhotoId);
        userName = (EditText) findViewById(R.id.userNameProfileId);
        saveButton = (Button) findViewById(R.id.saveUserProfileId);
        progressBarUserProfileImage = (ProgressBar) findViewById(R.id.progressBarUserProfileImageId);



        loadUserInformation();

        addChange.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(userProfile.this, MainActivity.class));
        }
    }

    private void loadUserInformation() {

        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(userImage);
                Log.d("profilepic", user.getPhotoUrl().toString());
            }
            if (user.getDisplayName() != null) {
                userName.setText(user.getDisplayName());
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addChangePhotoId) {
            showImageChooser();
        }
        if (v.getId() == R.id.saveUserProfileId) {
            saveUserInformation();
        }
    }


    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), PROFILE_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PROFILE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                userImage.setImageBitmap(bitmap);
                uploadImageToFirebaseStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {

        StorageReference userProfileImageRef = FirebaseStorage.getInstance().getReference("profilePictures/" + user.getUid() + ".jpg");

        if (uriProfileImage != null) {
            progressBarUserProfileImage.setVisibility(View.VISIBLE);


            userProfileImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(userProfile.this, "old photo deleted", Toast.LENGTH_LONG).show();
                }
            });
            userProfileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBarUserProfileImage.setVisibility(View.GONE);
                            taskSnapshot.getMetadata().getReference().getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            profileImageUrl = uri.toString();

                                            //next code is to temporary show of pro pic
                                            FirebaseUser user = mAuth.getCurrentUser();

                                            if (user != null && profileImageUrl != null) {
                                                UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                                                        .setPhotoUri(Uri.parse(profileImageUrl))
                                                        .build();
                                            }
                                            //
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressBarUserProfileImage.setVisibility(View.GONE);
                                            Toast.makeText(userProfile.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBarUserProfileImage.setVisibility(View.GONE);
                            Toast.makeText(userProfile.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void saveUserInformation() {
        String name = userName.getText().toString();
        if (name.isEmpty()) {
            userName.setText("Please enter name");
            userName.requestFocus();
            return;
        }
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null && profileImageUrl != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(userProfile.this, Homepage.class));
                        Toast.makeText(userProfile.this, "Profile Updated", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(userProfile.this, "Some error occured", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }

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
