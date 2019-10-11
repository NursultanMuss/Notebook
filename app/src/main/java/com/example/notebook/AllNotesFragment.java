package com.example.notebook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notebook.models.Note;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Arrays;


public class AllNotesFragment extends Fragment {
    private static final String TAG = "MainActivity";
    private FirebaseAuth fAuth;
    private DatabaseReference fNoteDataBase;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseRecyclerAdapter<Note, NoteViewHolder> fAdapter;
    public static final int RC_SIGN_IN = 144;
    private String mUsername;


    private LinearLayout linearLayout;
    private LinearLayoutManager linearLayoutManager;
    RecyclerView rvNotes;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_notes,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //get view of MainActivity
        View view= getView();
        // Firebase initialize
        fAuth = FirebaseAuth.getInstance();
        fNoteDataBase = FirebaseDatabase.getInstance().getReference();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser fUser = firebaseAuth.getCurrentUser();
                if (fUser != null) {
                    //User is signed in
                    onSingedInInitialize(fUser.getDisplayName());
                } else {
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

        // Firebase initialize

        //for RecyclerView
        linearLayout = view.findViewById(R.id.ll_empty_notebook);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvNotes = view.findViewById(R.id.rv_notes);
        rvNotes.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNotes.setHasFixedSize(true);
        //for RecyclerView
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: is called");
        if (fAuth.getCurrentUser() != null) {

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
                    return  new NoteViewHolder(view);
                }

                @Override
                protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note model) {
                    Log.d(TAG, "onBindViewHolder: is called");
                    holder.setNoteTitle(model.getTitle());
                    holder.setNoteContent(model.getContent());
                    holder.setNoteTime(model.getTimestamp());
                }
            };
            rvNotes.setAdapter(fAdapter);
            Log.d(TAG, "onStart: start listenning");
            fAdapter.startListening();
        } else {
            linearLayout.setVisibility(View.VISIBLE);
            rvNotes.setVisibility(View.GONE);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: is called");
        if (fAuth.getCurrentUser() != null) {
            fAdapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        fAuth.removeAuthStateListener(mAuthStateListener);
    }

    private void onSingedInInitialize(String username) {
        mUsername = username;
//        final Map authMap = new HashMap();
//        authMap.put("name",mUsername);
//        fNoteDataBase.child("Users").push().setValue(authMap);
    }

    private void onSignedOutCleanUp() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (getActivity() != null) {
            if (requestCode == RC_SIGN_IN) {
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if (resultCode == getActivity().RESULT_OK) {
                    Toast.makeText(getActivity(), "Signed in!", Toast.LENGTH_SHORT).show();
                } else if (response == null) {
                    Toast.makeText(getActivity(), "Sign in canceled", Toast.LENGTH_SHORT).show();
                    getActivity().finish();

                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}