package com.example.notebook;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
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
    private boolean isClicked;

    private Toolbar toolbar, edit_toolbar;
    private EditText new_note_title, new_note_content;
    private ImageButton make_reminder, info;
    private ImageView notebook_img;
    private TextView notebook_name;

    private DatabaseReference fNotesDatabase;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_note);

        fab_edit = (FloatingActionButton) findViewById(R.id.fab_edit);
        isClicked = false;
        toolbar = (Toolbar) findViewById(R.id.new_note_toolbar);
        edit_toolbar = (Toolbar) findViewById(R.id.edit_note_toolbar);
        new_note_title = (EditText) findViewById(R.id.new_note_title);
        new_note_content = (EditText) findViewById(R.id.new_note_content);
        make_reminder = (ImageButton) findViewById(R.id.make_reminder);
        info = (ImageButton) findViewById(R.id.info);
        notebook_img = (ImageView) findViewById(R.id.notebook_image_view);
        notebook_name = (TextView) findViewById(R.id.notebook_name);


        setSupportActionBar(toolbar);
        ActionBar actionBar = this.getSupportActionBar();
        if(!isClicked) {
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }else{
            actionBar.setIcon(R.drawable.ic_action_done);
        }
        fAuth = FirebaseAuth.getInstance();
        fNotesDatabase = FirebaseDatabase.getInstance().getReference().child("Notes").child(fAuth.getCurrentUser().getUid());
    }

    public void clickEditText(final View view){
        final Animation fabHide = AnimationUtils.loadAnimation(view.getContext(),R.anim.fab_scale_down);
        Log.d("edit","editted");
//        Toast.makeText(view.getContext(), "Edit", Toast.LENGTH_SHORT).show();
        if(!isClicked){
            isClicked=true;
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
            toolbar.animate()
                    .translationY(0)
                    .alpha(0.0f)
                    .setDuration(200)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            toolbar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
            edit_toolbar.animate()
                    .translationY(0)
                    .alpha(1.0f)
                    .setDuration(200)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            edit_toolbar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
            new_note_content.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(new_note_content, InputMethodManager.SHOW_IMPLICIT);
            Log.d("edit","editted in");
        }
        String title = new_note_title.getText().toString().trim();
        String content = new_note_content.getText().toString().trim();

        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)){
            createNote(title,content);
        }else{
            Snackbar.make(view, "Fill empty fields",Snackbar.LENGTH_SHORT).show();
        }
    }

    public void clickDone(View view){
        if(isClicked){
            isClicked=false;
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
            toolbar.animate()
                    .translationY(0)
                    .alpha(1.0f)
                    .setDuration(200)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            toolbar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
            edit_toolbar.animate()
                    .translationY(edit_toolbar.getHeight())
                    .alpha(0.0f)
                    .setDuration(200)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            edit_toolbar.setVisibility(View.GONE);
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

        }
    }

    public void clickChoseNotebook(View view){
        Intent intent = new Intent(MakeNoteActivity.this, ChgNoteBookActivity.class);
        startActivity(intent);
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
            });
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

    }
}
