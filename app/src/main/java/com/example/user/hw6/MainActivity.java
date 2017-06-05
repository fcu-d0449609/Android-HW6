package com.example.user.hw6;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button button;
    ListView note;
    SQLiteDatabase db;
    ArrayList<String> titlelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(addtext);

        note = (ListView)findViewById(R.id.note);
        note.setOnItemClickListener(click);
        note.setOnItemLongClickListener(clickremove);
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.close();
    }

    @Override
    protected void onResume() {
        super.onResume();

        DB openhelper = new DB(this);
        db = openhelper.getWritableDatabase();

        titlelist = NDB.getTitleList(db);
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titlelist);
        note.setAdapter(adapter);
    }

    View.OnClickListener addtext = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,NEditor.class);
            intent.putExtra("NOTEPOS", -1);
            startActivity(intent);
        }
    };

    AdapterView.OnItemClickListener click = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> av, View v,int position, long id) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,NEditor.class);
            intent.putExtra("NOTEPOS", position);
            startActivity(intent);
        }
    };
    AdapterView.OnItemLongClickListener clickremove = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> av, View v,int position, long id) {
            String title = titlelist.get(position);
            NDB.delNote(db, title);
            titlelist = NDB.getTitleList(db);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, titlelist);
            note.setAdapter(adapter);
            return false;
        }

    };
}
