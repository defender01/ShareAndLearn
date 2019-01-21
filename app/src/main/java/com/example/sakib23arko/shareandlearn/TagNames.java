package com.example.sakib23arko.shareandlearn;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TagNames extends AppCompatActivity {

    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_names);

        listView = findViewById(R.id.ListUserViewID);

        final String[] TagName = getResources().getStringArray(R.array.Tags);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TagNames.this, R.layout.sample_tag, R.id.TextViewID, TagName);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TagNames.this, SelectedTags.class);
                String SelectedTagName = TagName[position];
                intent.putExtra("FoundTag", SelectedTagName);
                startActivity(intent);
            }
        });
    }
}

