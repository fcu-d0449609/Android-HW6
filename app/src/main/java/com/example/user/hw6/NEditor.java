package com.example.user.hw6;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by USER on 2017/6/5.
 */

public class NEditor extends ActionBarActivity {
    EditText et_title, et_body;
    ArrayList<String> titlelist;
    SQLiteDatabase db;
    int notepos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noteedit);
        et_title = (EditText)findViewById(R.id.title);
        et_body = (EditText)findViewById(R.id.body);

        Intent intent = getIntent();
        notepos = intent.getIntExtra("NOTEPOS", -1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DB openhelper = new DB(this);
        db = openhelper.getWritableDatabase();

        titlelist = NDB.getTitleList(db);

        if (notepos != -1) {
            String title = titlelist.get(notepos);
            et_title.setText(title);
            et_body.setText(NDB.getBody(db, title));
        } else {
            et_title.setText("");
            et_body.setText("");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        String title = et_title.getText().toString();
        if (title.length() == 0) {
            Toast.makeText(this, "標題為空，便條無法儲存!!!!",Toast.LENGTH_LONG).show();
        } else {
            NDB.addNote(db, et_title.getText().toString(),et_body.getText().toString());
        }
    }

    boolean isTitleExist(String title) {
        for (int i = 0; i < titlelist.size(); i++)
            if (title.equalsIgnoreCase(titlelist.get(i)))
                return true;
        return false;
    }
}
