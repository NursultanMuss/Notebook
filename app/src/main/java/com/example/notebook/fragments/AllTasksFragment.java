package com.example.notebook.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notebook.GetTimeAgo;
import com.example.notebook.MakeNoteActivity;
import com.example.notebook.NoteViewHolder;
import com.example.notebook.R;
import com.example.notebook.TaskViewHolder;
import com.example.notebook.models.Note;
import com.example.notebook.models.Task;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AllTasksFragment extends Fragment {
    private static final String TAG = "AllTasksFragment";
    private FirebaseAuth fAuth;
    private DatabaseReference fNoteDataBase;
    private FirebaseRecyclerAdapter<Task, TaskViewHolder> fAdapter;

    private LinearLayout linearLayout;
    private LinearLayoutManager linearLayoutManager;
    RecyclerView rvNotes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_tasks,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //get view of MainActivity
        View view= getView();
        // Firebase initialize
        fAuth = FirebaseAuth.getInstance();
        fNoteDataBase = FirebaseDatabase.getInstance().getReference();
        fNoteDataBase.keepSynced(true);



        // Firebase initialize

        //for RecyclerView
        linearLayout = view.findViewById(R.id.ll_empty_tasks);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvNotes = view.findViewById(R.id.rv_tasks);
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
                Query query = fNoteDataBase.child("Tasks").child(fAuth.getCurrentUser().getUid()).orderByChild("priority");
                Log.d(TAG, "onStart: " + fNoteDataBase.toString());
                FirebaseRecyclerOptions<Task> options =
                        new FirebaseRecyclerOptions.Builder<Task>()
                                .setQuery(query, new SnapshotParser<Task>() {
                                    @NonNull
                                    @Override
                                    public Task parseSnapshot(@NonNull DataSnapshot snapshot) {

                                        return new Task(snapshot.child("taskText").getValue().toString(),
                                                ((Long)snapshot.child("priority").getValue()).intValue(),
                                                snapshot.child("timeStamp").getValue().toString()
                                        );
                                    }
                                })
                                .build();

                fAdapter = new FirebaseRecyclerAdapter<Task, TaskViewHolder>(options) {
                    @NonNull
                    @Override
                    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.row_task, parent, false);
                        Log.d(TAG, "onCreateViewHolder: is called");

                        return new TaskViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull TaskViewHolder holder, int position, @NonNull Task model) {
                        Log.d(TAG, "onBindViewHolder: is called");
                        String noteID= getRef(position).getKey();
                        holder.setTaskText(model.getTaskText());
                        holder.setTaskPriority(model.getPriority());
                        GetTimeAgo getTimeAgo =  new GetTimeAgo();
                        Log.d(TAG, "onBindViewHolder: " + model.getTimeStamp());



                        holder.itemView.findViewById(R.id.task_checkbox).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CheckBox checkBox = (CheckBox) v;
                                boolean checked = checkBox.isChecked();
                                if(checked){
                                    Toast.makeText(getActivity(), R.string.task_checkbox_check, Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getActivity(),R.string.task_checkbox_uncheck , Toast.LENGTH_SHORT).show();
                                }

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
