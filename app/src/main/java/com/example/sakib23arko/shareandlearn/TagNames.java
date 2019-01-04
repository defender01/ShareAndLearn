package com.example.sakib23arko.shareandlearn;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TagNames extends AppCompatActivity {

    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_names);

        listView = findViewById(R.id.ListUserViewID);

        final String[] TagName = getResources().getStringArray(R.array.Tags);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TagNames.this, R.layout.sample_tag, R.id.TextViewID, TagName);
        Log.d("tagtagtagtag", "sk");
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

