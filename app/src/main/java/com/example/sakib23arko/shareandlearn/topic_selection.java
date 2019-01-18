package com.example.sakib23arko.shareandlearn;

import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class topic_selection extends AppCompatActivity {

    private String[] tags;
    ListView listView;
    topicSelectAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_selection);

        tags=getResources().getStringArray(R.array.Tags);

        listView = findViewById(R.id.topicListViewId);

        adapter = new topicSelectAdapter(getApplicationContext(),tags);
        listView.setAdapter(adapter);
    }
}
