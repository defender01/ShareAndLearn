package com.example.sakib23arko.shareandlearn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context context;
    private ImageView imageView;
    ArrayList<userNameAndImage> arrayList;
    private LayoutInflater layoutInflater;

    CustomAdapter(Context context, ArrayList<userNameAndImage> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() { return arrayList.size(); }

    @Override
    public Object getItem(int position) { return arrayList.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FirebaseAuth mAuth;
        mAuth=FirebaseAuth.getInstance();
        String userName = arrayList.get(position).getUserName();
        String userImage = arrayList.get(position).getUserImage();
        if(convertView == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.image_name_profile_view, null);
        }

        imageView = convertView.findViewById(R.id.imageViewID);
        TextView textView = convertView.findViewById(R.id.UserNameID);
        TextView homeID = convertView.findViewById(R.id.HomeID);
        TextView viewProfile = convertView.findViewById(R.id.ViewProfileID);
        TextView signoutButton = convertView.findViewById(R.id.LogOutID);
        TextView tagNameID = convertView.findViewById(R.id.TagsID);
        TextView editProfile=convertView.findViewById(R.id.editProfileID);
        tagNameID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, TagNames.class));
            }
        });

        homeID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Homepage.class));
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, userProfile.class));
            }
        });

        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, userProfile.class));
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,profileDetails.class));
            }
        });
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(context,"You have signout",Toast.LENGTH_LONG).show();
                context.startActivity(new Intent(context,MainActivity.class));
                ((Activity)context).finish();
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, userProfile.class));
            }
        });

        textView.setText(userName);
        StorageReference userProfileImageRef = FirebaseStorage.getInstance().getReference("profilePictures/" + userImage + ".jpg");
        GlideApp.with(this.context)
                .load(userProfileImageRef)
                .into(imageView);
        return convertView;
    }
}