package com.example.notebook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;


import com.example.notebook.adapters.NotesAdapter;
import com.example.notebook.models.Note;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.github.clans.fab.FloatingActionButton;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

//    @BindView(R.id.rv_notes)
    RecyclerView rvNotes;
//    RecyclerView.Adapter adapter;
    List<Note> notesList;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference fNoteDataBase;
    FirebaseRecyclerAdapter<Note,NoteViewHolder> fAdapter;

    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private static final String TAG = "MainActivity";
    public static final int RC_SIGN_IN = 144;
    String mUsername;

    private LinearLayout linearLayout;

    //for Navigation Drawer
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    //for Navigation Drawer
    Button btnSignOut;

    ProgressDialog PD;


    FloatingActionButton fab_plus, fab_note, fab_task, fab_aim;
    Animation FabOpen, FabClose, ChgBtn;
    boolean isOpen = false;



    @Override    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ButterKnife.bind(this);

//        fab_plus = (FloatingActionButton) findViewById(R.id.fab_plus);
        fab_aim = (FloatingActionButton) findViewById(R.id.fab_aim);
        fab_note = (FloatingActionButton) findViewById(R.id.fab_note);
        fab_task = (FloatingActionButton) findViewById(R.id.fab_task);


//        rvNotes.setHasFixedSize(true);
//        rvNotes.setLayoutManager(linearLayoutManager);

        fAuth = FirebaseAuth.getInstance();


        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

//        btnSignOut = (Button) findViewById(R.id.sign_out_button);
//
//
//
//        btnSignOut.setOnClickListener(new View.OnClickListener() {
//            @Override            public void onClick(View view) {
//                auth.signOut();
//                FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
//                    @Override
//                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                        FirebaseUser user = firebaseAuth.getCurrentUser();
//                        if (user == null) {
//                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                            finish();
//                        }
//                    }
//                };
//            }
//        });
//
//        findViewById(R.id.change_password_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), ForgetAndChangePasswordActivity.class).putExtra("Mode", 1));
//            }
//        });
//
//        findViewById(R.id.change_email_button).setOnClickListener(new View.OnClickListener() {
//            @Override            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), ForgetAndChangePasswordActivity.class).putExtra("Mode", 2));
//            }
//        });
//
//        findViewById(R.id.delete_user_button).setOnClickListener(new View.OnClickListener() {
//            @Override            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), ForgetAndChangePasswordActivity.class).putExtra("Mode", 3));
//            }
//        });
     //For Navigation Drawer
        toolbar = findViewById(R.id.draw_toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.draw_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar
                ,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        //For Navigation Drawer
        linearLayout = findViewById(R.id.ll_empty_notebook);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rvNotes = findViewById(R.id.rv_notes);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotes.setHasFixedSize(true);
        fNoteDataBase= FirebaseDatabase.getInstance().getReference();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser fUser = firebaseAuth.getCurrentUser();
                if( fUser != null){
                    //User is signed in
                    onSingedInInitialize(fUser.getDisplayName());
                }else{
                    //User is signed out
                    onSignedOutCleanUp();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart()  {
        super.onStart();
        Log.d(TAG, "onStart: is called");
        if(fAuth.getCurrentUser()!=null) {

            Query query = fNoteDataBase.child("Notes").child(fAuth.getCurrentUser().getUid());
            Log.d(TAG, "onStart: " + fNoteDataBase.toString());
            FirebaseRecyclerOptions<Note> options =
                    new FirebaseRecyclerOptions.Builder<Note>()
                            .setQuery(query, new SnapshotParser<Note>() {
                                @NonNull
                                @Override
                                public Note parseSnapshot(@NonNull DataSnapshot snapshot) {
                                    return new Note(snapshot.child("title").getValue().toString(),
                                            snapshot.child("content").getValue().toString(),
                                            snapshot.child("timeStamp").getValue().toString());
                                }
                            })
                            .build();

            fAdapter = new FirebaseRecyclerAdapter<Note, NoteViewHolder>(options) {
                @NonNull
                @Override
                public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.row_note, parent, false);
                    Log.d(TAG, "onCreateViewHolder: is called");
                    NoteViewHolder noteViewHolder = new NoteViewHolder(view);
                    return noteViewHolder;
                }

                @Override
                protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note model) {
                    String noteId = getRef(position).getKey();
                    Log.d(TAG, "onBindViewHolder: is called");
                    holder.setNoteTitle(model.getTitle());
                    holder.setNoteContent(model.getContent());
                    holder.setNoteTime(model.getTimestamp());
                }
            };
            rvNotes.setAdapter(fAdapter);
            Log.d(TAG, "onStart: start listenning");
            fAdapter.startListening();
        }else{
            linearLayout.setVisibility(View.VISIBLE);
            rvNotes.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: is called");
        if(fAuth.getCurrentUser() != null) {
            fAdapter.stopListening();
        }
        
    }

    @Override    protected void onResume() {
        super.onResume();
        fAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        fAuth.removeAuthStateListener(mAuthStateListener);
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

    private void onSingedInInitialize(String username){
        mUsername = username;
//        final Map authMap = new HashMap();
//        authMap.put("name",mUsername);
//        fNoteDataBase.child("Users").push().setValue(authMap);
    }
    private void onSignedOutCleanUp(){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK){
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            }else if(response == null){
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();

            }else if(response.getError().getErrorCode() == ErrorCodes.NO_NETWORK){
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }
}
