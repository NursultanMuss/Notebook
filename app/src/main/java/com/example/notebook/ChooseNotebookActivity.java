package com.example.notebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notebook.fragments.Dialog;
import com.example.notebook.models.Notebook;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;

public class ChooseNotebookActivity extends AppCompatActivity implements Dialog.DialogListener {

    private Toolbar mtoolbar;
    private Button button_chg_nb;

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<Notebook, NotebookViewHolder> fAdapter;


    private final int DIALOG = 1;
    private DialogFragment dlg1;

    private DatabaseReference f_notebook_ref;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;

    private Notebook notebook;
    private static final String TAG = "ChgNoteBookActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_notebook);

        mtoolbar = findViewById(R.id.choose_note_toolbar);
        button_chg_nb = findViewById(R.id.choose_notebook_btn);
        //for RecyclerView
        recyclerView = findViewById(R.id.rv_choose_notebook);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        //for RecyclerView

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        Log.d(TAG, "onCreate: "+ fUser.getDisplayName());
        f_notebook_ref = FirebaseDatabase.getInstance().getReference();
        f_notebook_ref.keepSynced(true);

        dlg1 = new DialogFragment();

        setSupportActionBar(mtoolbar);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: start" + fUser.getDisplayName().toString());
        if (fUser != null) {
            Log.d(TAG, "user is not null");
            FirebaseUserMetadata metadata = fAuth.getCurrentUser().getMetadata();
            if (metadata.getCreationTimestamp() != metadata.getLastSignInTimestamp()) {
                Query query = f_notebook_ref.child("Notebooks")
                        .child(fAuth.getCurrentUser().getUid());
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
                                .inflate(R.layout.row_notebook, parent, false);
                        return new NotebookViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull NotebookViewHolder holder, int position, @NonNull Notebook model) {
                        holder.setNotebookTitle(model.getNotebookName());
                    }
                };

                recyclerView.setAdapter(fAdapter);
                fAdapter.startListening();
            } else {
                TextView textView = new TextView(this);
                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setGravity(Gravity.CENTER);
                textView.setText(R.string.no_notebooks);
                recyclerView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (fUser != null) {
            fAdapter.stopListening();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chg_notebook, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_notebook_app_bar_search:
                break;
            case R.id.add_notebook:
                openDialog();
                break;
            case android.R.id.home:
                finish();
            default:

        }
        return super.onOptionsItemSelected(item);

    }

    public void openDialog() {
        Dialog dlg2 = new Dialog();
        dlg2.show(getSupportFragmentManager(), "example dialog");
    }

    public void onChooseNoteBook(View view) {
//        if(!TextUtils.isEmpty(newNotebook.getText())) {
//            String s_newNotebook = newNotebook.getText().toString().trim();
//            intent.putExtra("new", s_newNotebook);
//            getActivity().setResult(Activity.RESULT_OK, intent);
//            getActivity().finish();
    }

    @Override
    public void applyText(String s_new_notebook) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference newNotebookRef = f_notebook_ref.push();
        final Map noteMap = new HashMap();
        noteMap.put("user", firebaseUser.getDisplayName());
        noteMap.put("notebookName", s_new_notebook);

        Thread mainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                newNotebookRef.setValue(noteMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ChooseNotebookActivity.this, R.string.new_notebook_add, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ChooseNotebookActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        mainThread.run();

        Intent intent = new Intent();
        intent.putExtra("new", s_new_notebook);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
