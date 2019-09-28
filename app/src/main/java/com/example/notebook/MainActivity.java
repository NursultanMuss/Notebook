package com.example.notebook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;


import com.example.notebook.adapters.NotesAdapter;
import com.example.notebook.database.DatabaseHandler;
import com.example.notebook.models.Note;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.github.clans.fab.FloatingActionButton;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_notes)
    RecyclerView rvNotes;
    RecyclerView.Adapter adapter;
    List<Note> notesList;

    Button btnSignOut;
    FirebaseAuth auth;
    FirebaseUser user;
    ProgressDialog PD;
    private DatabaseReference fNoteDB;

    FloatingActionButton fab_plus, fab_note, fab_task, fab_aim;
    Animation FabOpen, FabClose, ChgBtn;
    boolean isOpen = false;



    @Override    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        initViews();
        loadNotes();

//        fab_plus = (FloatingActionButton) findViewById(R.id.fab_plus);
        fab_aim = (FloatingActionButton) findViewById(R.id.fab_aim);
        fab_note = (FloatingActionButton) findViewById(R.id.fab_note);
        fab_task = (FloatingActionButton) findViewById(R.id.fab_task);


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user != null){
            fNoteDB = FirebaseDatabase.getInstance().getReference().child("Notes").child(auth.getCurrentUser().getUid());

        }

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        btnSignOut = (Button) findViewById(R.id.sign_out_button);

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View view) {
                auth.signOut();
                FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user == null) {
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                };
            }
        });

        findViewById(R.id.change_password_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgetAndChangePasswordActivity.class).putExtra("Mode", 1));
            }
        });

        findViewById(R.id.change_email_button).setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgetAndChangePasswordActivity.class).putExtra("Mode", 2));
            }
        });

        findViewById(R.id.delete_user_button).setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgetAndChangePasswordActivity.class).putExtra("Mode", 3));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

//        FirebaseRecyclerAdapter<Note, NoteViewHolder> firebaseRecyclerAdapter
//                =new FirebaseRecyclerAdapter<Note, NoteViewHolder>() {
//
//
//            @androidx.annotation.NonNull
//            @Override
//            public NoteViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
//                return null;
//            }
//
//            @Override
//            protected void onBindViewHolder(@androidx.annotation.NonNull NoteViewHolder holder, int position, @androidx.annotation.NonNull Note model) {
//
//            }
//        };
    }

    private void fetch(){
        Query query = FirebaseDatabase.getInstance().getReference().child("Notes");

        FirebaseRecyclerOptions<Note> options =
                new FirebaseRecyclerOptions.Builder<Note>()
                .setQuery(query, new SnapshotParser<Note>() {
                    @NonNull
                    @Override
                    public Note parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return new Note(snapshot.getKey(),
                                snapshot.child("title").getValue().toString(),
                                snapshot.child("content").getValue().toString(),
                                snapshot.child("notebook").getValue().toString(),
                                (int)snapshot.child("timeStamp").getValue());
                    }
                })
                .build();

        FirebaseRecyclerAdapter fAdapter = new FirebaseRecyclerAdapter<Note, NoteViewHolder>(options) {
            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_note,parent,false);

                return new NoteViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note model) {
                holder.setNoteTitle(model.getTitle());
                holder.setNoteContent(model.getNote());
                holder.setNoteTime(model.getTimestamp());

            }
        };
    }

    private void initViews(){
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadNotes(){
        DatabaseHandler db = new DatabaseHandler(this);

        notesList = db.getAllNotes();
        if(notesList.size() !=0){
            adapter = new NotesAdapter(this, notesList);
            rvNotes.setAdapter(adapter);
        }
    }


    @Override    protected void onResume() {
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        super.onResume();
    }

    public void ClickMakeNote(View v){
        Intent intent = new Intent(MainActivity.this,MakeNoteActivity.class);
        startActivity(intent);
    }

    public void ClickMakeTask(View v){
        Intent intent = new Intent(MainActivity.this, MakeTaskActivity.class);
        startActivity(intent);
    }
    public void ClickMakeAim(View v){
        Intent intent = new Intent(MainActivity.this,MakeAimActivity.class);
        startActivity(intent);
    }
}
