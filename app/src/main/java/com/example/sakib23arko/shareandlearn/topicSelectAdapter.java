package com.example.sakib23arko.shareandlearn;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class topicSelectAdapter extends BaseAdapter {

    private String[] tags;
    private List<String> list;
    private Context context;
    private LayoutInflater inflater;

    private DatabaseReference userDatabaseRef;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private boolean[] tagcon;

    public  topicSelectAdapter(Context context, final String[] tags ){
        this.context=context;
        this.tags=tags;
        inflater = (LayoutInflater.from(context));

        list=new ArrayList<>();
        userDatabaseRef = FirebaseDatabase.getInstance().getReference("userPersonalInfo");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        tagcon=new boolean[tags.length];

    }

    @Override
    public int getCount() {
        return tags.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.sample_tag_select, parent, false);

        final CheckedTextView checkedTextView= (CheckedTextView)view.findViewById(R.id.checkedTextViewId);
        checkedTextView.setText(tags[position]);

        userDatabaseRef.child(user.getUid()).child("tagName").child(tags[position]).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()==null || dataSnapshot.getValue().toString().equals("1")) {
                    tagcon[position] = true;
                    checkedTextView.setChecked(tagcon[position]);
                    userDatabaseRef.child(user.getUid()).child("tagName").child(tags[position]).setValue("1");
                }
                else {
                    tagcon[position]=false;
                    checkedTextView.setChecked(tagcon[position]);
                    userDatabaseRef.child(user.getUid()).child("tagName").child(tags[position]).setValue("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Log.d("taaag","tag "+tags[position]+" "+tagcon[position]);

        checkedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tagcon[position]==false) {
                    tagcon[position]=true;
                    checkedTextView.setChecked(true);
                    userDatabaseRef.child(user.getUid()).child("tagName").child(tags[position]).setValue("1");
                }
                else{
                    checkedTextView.setChecked(false);
                    tagcon[position]=false;
                    userDatabaseRef.child(user.getUid()).child("tagName").child(tags[position]).setValue("0");
                }
            }
        });

        return view;
    }
}