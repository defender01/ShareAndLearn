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

public class CommentAdapter extends BaseAdapter {
    Context context;
    private ImageView imageView;
    ArrayList<infoOfComment> arrayList;
    private LayoutInflater layoutInflater;

    CommentAdapter(Context context, ArrayList<infoOfComment> arrayList) {
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
        String userName = arrayList.get(position).getUserID();
        String userImage = arrayList.get(position).getUserImage();
        String userComment = arrayList.get(position).getMainComment();

        if(convertView == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.sample_comments, null);
        }

        imageView = convertView.findViewById(R.id.whoCommentedPicID);
        TextView NAME = convertView.findViewById(R.id.whoCommentedNameID);
        TextView COMMENT = convertView.findViewById(R.id.CommentID);

        NAME.setText(userName);
        COMMENT.setText(userComment);

        StorageReference userProfileImageRef = FirebaseStorage.getInstance().getReference("profilePictures/" + userImage + ".jpg");
        GlideApp.with(this.context)
                .load(userProfileImageRef)
                .into(imageView);
        return convertView;
    }
}