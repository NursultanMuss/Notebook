package com.example.notebook.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notebook.NotebookViewHolder;
import com.example.notebook.R;
import com.example.notebook.models.Note;
import com.example.notebook.models.Notebook;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AllNotebooksFragment extends Fragment {
    private static final String TAG = "AllNotebooksFragment";
    private FirebaseAuth fAuth;
    private DatabaseReference fNoteDataBase;
    private FirebaseRecyclerAdapter<Notebook, NotebookViewHolder> fAdapter;
    private LinearLayout linearLayout;
    private LinearLayoutManager linearLayoutManager;
    RecyclerView rvNotes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_all_notebooks,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view= getView();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        // Firebase initialize
        fAuth = FirebaseAuth.getInstance();
        fNoteDataBase = FirebaseDatabase.getInstance().getReference();

        // Firebase initialize

        //for RecyclerView
        linearLayout = view.findViewById(R.id.ll_empty_notebooks);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvNotes = view.findViewById(R.id.rv_notebooks);
        rvNotes.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNotes.setHasFixedSize(true);
        //for RecyclerView
    }

    @Override
    public void onStart() {
        super.onStart();
        if(fAuth.getCurrentUser() != null){
        Query query = fNoteDataBase.child("Notebooks").child(fAuth.getCurrentUser().getUid());;
        FirebaseRecyclerOptions<Notebook> options = new FirebaseRecyclerOptions.Builder<Notebook>()
                .setQuery(query, new SnapshotParser<Notebook>() {
                    @NonNull
                    @Override
                    public Notebook parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return new Notebook(snapshot.child("user").getValue().toString(),
                                snapshot.child("notebookName").getValue().toString());
                    }
                }).build();
        fAdapter = new FirebaseRecyclerAdapter<Notebook, NotebookViewHolder>(options) {
            @NonNull
            @Override
            public NotebookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_notebook,parent,false);
                return new NotebookViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull NotebookViewHolder holder, int position, @NonNull Notebook model) {
                holder.setNotebookTitle(model.getNotebookName());
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

}
