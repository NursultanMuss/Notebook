package com.example.notebook.fragments;

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

import com.example.notebook.GetTimeAgo;
import com.example.notebook.MainActivity;
import com.example.notebook.MakeNoteActivity;
import com.example.notebook.NoteViewHolder;
import com.example.notebook.R;
import com.example.notebook.models.Note;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Arrays;


public class AllNotesFragment extends Fragment {
    private static final String TAG = "AllNotesFragment";
    private FirebaseAuth fAuth;
    private DatabaseReference fNoteDataBase;
    private FirebaseRecyclerAdapter<Note, NoteViewHolder> fAdapter;


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



        // Firebase initialize

        //for RecyclerView
        linearLayout = view.findViewById(R.id.ll_empty_notes);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
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
            FirebaseUserMetadata metadata = fAuth.getCurrentUser().getMetadata();
            if (metadata.getCreationTimestamp() != metadata.getLastSignInTimestamp()) {
                Query query = fNoteDataBase.child("Notes").child(fAuth.getCurrentUser().getUid()).orderByChild("timeStamp").limitToLast(15);
                Log.d(TAG, "onStart: " + fNoteDataBase.toString());
                FirebaseRecyclerOptions<Note> options =
                        new FirebaseRecyclerOptions.Builder<Note>()
                                .setQuery(query, new SnapshotParser<Note>() {
                                    @NonNull
                                    @Override
                                    public Note parseSnapshot(@NonNull DataSnapshot snapshot) {

                                        return new Note(snapshot.child("title").getValue().toString(),
                                                snapshot.child("content").getValue().toString(),
                                                snapshot.child("timeStamp").getValue().toString(),
                                                snapshot.child("notebook").getValue().toString()
                                        );
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

                        return new NoteViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note model) {
                        Log.d(TAG, "onBindViewHolder: is called");
                        String noteID= getRef(position).getKey();
                        holder.setNoteTitle(model.getTitle());
                        holder.setNoteContent(model.getContent());
                        GetTimeAgo getTimeAgo =  new GetTimeAgo();
                        Log.d(TAG, "onBindViewHolder: " + model.getTimestamp());
                        holder.setNoteTime(getTimeAgo.getTimeAgo(Long.parseLong(model.getTimestamp()), getActivity().getApplicationContext()));



                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), MakeNoteActivity.class);
                                intent.putExtra("noteID", noteID);
                                startActivity(intent);
                            }
                        });
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

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: is called");
        if (fAuth.getCurrentUser() != null) {
            fAdapter.stopListening();
        }
    }





}
