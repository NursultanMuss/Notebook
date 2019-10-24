package com.example.notebook;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.example.notebook.models.Note;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class  MakeNoteActivity extends AppCompatActivity {

    private FloatingActionButton fab_edit;
    private boolean isClicked;

//    private Toolbar toolbar, edit_toolbar;
    private EditText new_note_title, new_note_content;
    private ImageButton make_reminder, info;
    private ImageView notebook_img;
    private TextView notebook_name;
    private Menu mMenu;

    private DatabaseReference fNotesDatabase;
    private FirebaseAuth fAuth;
    private static final int PICK_NOTEBOOK_REQUEST =1;

    private static final String TAG = "MakeNoteActivity";
    private boolean isExist;
    private String noteID = "no";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_note);
        try {
            noteID = getIntent().getStringExtra("noteID");
            if(!noteID.trim().equals("")){
                isExist = true;
            }else{
                isExist = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        fab_edit = (FloatingActionButton) findViewById(R.id.fab_edit);
        isClicked = false;
//        toolbar = (Toolbar) findViewById(R.id.new_note_toolbar);
//        edit_toolbar = (Toolbar) findViewById(R.id.edit_note_toolbar);
        new_note_title = (EditText) findViewById(R.id.new_note_title);
        new_note_content = (EditText) findViewById(R.id.new_note_content);
        make_reminder = (ImageButton) findViewById(R.id.make_reminder);
        info = (ImageButton) findViewById(R.id.info);
        notebook_img = (ImageView) findViewById(R.id.notebook_image_view);
        notebook_name = (TextView) findViewById(R.id.notebook_name);

        ActionBar actionBar = this.getSupportActionBar();
        if(!isClicked) {
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowTitleEnabled(false);
            }
        }
        //SharedPreference - get Default Notebook name
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        notebook_name.setText(pref.getString(SettingsFragment.KEY_NOTEBOOK_PREF,""));
        Log.d(TAG, "onCreate: " + pref.getString(SettingsFragment.KEY_NOTEBOOK_PREF,""));
        //SharedPreference - get Default Notebook name
        fAuth = FirebaseAuth.getInstance();
        fNotesDatabase = FirebaseDatabase.getInstance().getReference().child("Notes").child(fAuth.getCurrentUser().getUid());
        fNotesDatabase.keepSynced(true);
    putData();
    }



    public void clickEditText(final View view){
        Log.d("edit","editted");
        if(!isClicked){
            isClicked=true;
            invalidateOptionsMenu();
//            invalidateOptionsMenu();
            fab_edit.animate()
                    .translationY(view.getHeight())
                    .alpha(0.0f)
                    .setDuration(200)
                    .setListener(new Animator.AnimatorListener() {

                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            view.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });

        }
            new_note_content.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(new_note_content, InputMethodManager.SHOW_IMPLICIT);
            Log.d("edit","editted in");
    }

    public void clickDone(MenuItem item){
        if(isClicked){
            isClicked=false;
            getSupportActionBar().setDisplayHomeAsUpEnabled(isClicked);
            invalidateOptionsMenu();
            fab_edit.animate()
                    .translationY(0)
                    .alpha(1.0f)
                    .setDuration(200)
                    .setListener(new Animator.AnimatorListener() {

                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            fab_edit.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(new_note_content.getWindowToken(), 0);

            String title = new_note_title.getText().toString().trim();
            String note_text = new_note_content.getText().toString().trim();
            String notebook = notebook_name.getText().toString().trim();


            if(!TextUtils.isEmpty(title) || !TextUtils.isEmpty(note_text) || !TextUtils.isEmpty(notebook)){
                createNote(title,note_text,notebook);
            }else{
                Toast.makeText(this, "Please fill all the fields before saving", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void clickChoseNotebook(View view){
        Intent intent = new Intent(MakeNoteActivity.this, ChgNoteBookActivity.class);
        startActivityForResult(intent,PICK_NOTEBOOK_REQUEST);
    }

    private void putData(){
        if (isExist) {
            fNotesDatabase.child(noteID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild("title") && dataSnapshot.hasChild("content") && dataSnapshot.hasChild("notebook")
                    && dataSnapshot.hasChild("timeStamp")) {
                        String title = dataSnapshot.child("title").getValue().toString();
                        String content = dataSnapshot.child("content").getValue().toString();
                        String notebook = dataSnapshot.child("notebook").getValue().toString();
                        String timeStamp = dataSnapshot.child("timeStamp").getValue().toString();

                        new_note_title.setText(title);
                        new_note_content.setText(content);
                        notebook_name.setText(notebook);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void createNote(String title, String content, String notebook){

        if (fAuth.getCurrentUser()!= null) {
            if (!isExist) {
                //Creating new Note
                final DatabaseReference newNoteRef = fNotesDatabase.push();

                final Map noteMap = new HashMap();
                noteMap.put("title", title);
                noteMap.put("content", content);
                noteMap.put("notebook", notebook);
                noteMap.put("timeStamp", ServerValue.TIMESTAMP);


                Thread mainThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        newNoteRef.setValue(noteMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(MakeNoteActivity.this, "Заметка добавлена в блокнот", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(MakeNoteActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                mainThread.run();
            } else {
                //updating picked note
                Map updateMap = new HashMap();
                updateMap.put("title", new_note_title.getText().toString().trim());
                updateMap.put("content", new_note_content.getText().toString().trim());
                updateMap.put("timeStamp", ServerValue.TIMESTAMP);
                updateMap.put("notebook", notebook_name.getText().toString().trim());
                fNotesDatabase.child(noteID).updateChildren(updateMap);
            }
        }else{

        }

    }

    private void deleteNote(){
        fNotesDatabase.child(noteID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MakeNoteActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Log.d(TAG, "onComplete: deleteNote"+ task.getException().getMessage());
                    Toast.makeText(MakeNoteActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.edit_notebook, menu);
        mMenu = menu;
        if(!isExist){
            mMenu.findItem(R.id.delete).setVisible(false);
        }
        return true;
    }
    //Method for change menu when fab_edit button clicked or when EditText is clicked
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(!isClicked);


            menu.findItem(R.id.undo).setVisible(isClicked);
            menu.findItem(R.id.redo).setVisible(isClicked);
            menu.findItem(R.id.done).setVisible(isClicked);
            menu.findItem(R.id.edit_tools).setVisible(isClicked);
            menu.findItem(R.id.attach).setVisible(isClicked);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                if(TextUtils.isEmpty(new_note_title.getText().toString()) || TextUtils.isEmpty(new_note_content.getText().toString())){
                    Toast.makeText(this, R.string.warn_empty_notebook, Toast.LENGTH_SHORT).show();
            }
                break;
            case R.id.done:
                break;
            case R.id.delete:
                deleteNote();
                break;

        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
       clickEditText(fab_edit);
        if(new_note_title.requestFocus()|| new_note_content.requestFocus()){
       clickEditText(fab_edit);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_NOTEBOOK_REQUEST){
            if(resultCode == RESULT_OK){
                String notebookName = data.getStringExtra("new");
                notebook_name.setText(notebookName);
            }
        }
    }
    //Converting dp to pixel

    private int dpToPx(Context context, float dp){
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
