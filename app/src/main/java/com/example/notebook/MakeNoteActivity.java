package com.example.notebook;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class MakeNoteActivity extends AppCompatActivity {
    private FloatingActionButton fab_edit;
    private Toolbar toolbar;
    private EditText new_note_title, new_note_content;
    private ImageButton make_reminder, info;

    private DatabaseReference fNotesDatabase;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_note);

        fab_edit = (FloatingActionButton) findViewById(R.id.fab_edit);
        toolbar = (Toolbar) findViewById(R.id.new_note_toolbar);
        new_note_title = (EditText) findViewById(R.id.new_note_title);
        new_note_content = (EditText) findViewById(R.id.new_note_content);
        make_reminder = (ImageButton) findViewById(R.id.make_reminder);
        info = (ImageButton) findViewById(R.id.info);
        setSupportActionBar(toolbar);
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        fAuth = FirebaseAuth.getInstance();
        fNotesDatabase = FirebaseDatabase.getInstance().getReference().child("Notes").child(fAuth.getCurrentUser().getUid());

        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = new_note_title.getText().toString().trim();
                String content = new_note_content.getText().toString().trim();

                if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)){
                    createNote(title,content);
                }else{
                    Snackbar.make(v, "Fill empty fields",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createNote(String title, String content){
        if(fAuth.getCurrentUser() != null){
            DatabaseReference newNoteRef = fNotesDatabase.push();

            Map noteMap = new HashMap();
            noteMap.put("title", title);
            noteMap.put("content", content);
            noteMap.put("timeStamp", ServerValue.TIMESTAMP);

            newNoteRef.setValue(noteMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                    }
                }
            })
        }else{
            Toast.makeText(this, "User is not signed in", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
