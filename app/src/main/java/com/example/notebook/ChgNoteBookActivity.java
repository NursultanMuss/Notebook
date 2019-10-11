package com.example.notebook;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notebook.fragments.Dialog;
import com.example.notebook.models.Note;
import com.example.notebook.models.Notebook;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class ChgNoteBookActivity extends AppCompatActivity implements Dialog.DialogListener {

    private Toolbar mtoolbar;
    private Button button_chg_nb;

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<Notebook, NotebookViewHolder> fAdapter;


    private final int DIALOG=1;
    private DialogFragment dlg1;
    private DatabaseReference f_notebook_ref;
    private FirebaseAuth fAuth;
    private Notebook notebook;
    private static final String TAG = "ChgNoteBookActivity";

    //ArrayList
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chg_note_book);
        mtoolbar = findViewById(R.id.chg_note_toolbar);
        button_chg_nb = findViewById(R.id.change_notebook_btn);
        //for RecyclerView
        recyclerView = findViewById(R.id.rv_change_notebook);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        //for RecyclerView

        fAuth = FirebaseAuth.getInstance();
        f_notebook_ref = FirebaseDatabase.getInstance().getReference().child("Notebooks")
                .child(fAuth.getCurrentUser().getUid());

        dlg1 = new DialogFragment();

        setSupportActionBar(mtoolbar);
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query = f_notebook_ref;
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
        recyclerView.setAdapter(fAdapter);
        fAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        fAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chg_notebook, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.app_bar_search:
                break;
            case  R.id.add_notebook:
                openDialog();
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
            default:

        }
        return super.onOptionsItemSelected(item);

    }
    public void openDialog(){
        Dialog dlg2=new Dialog();
        dlg2.show(getSupportFragmentManager(),"example dialog");
    }

    public void onChooseNoteBook(View view){
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
                        if(task.isSuccessful()){
                            Toast.makeText(ChgNoteBookActivity.this, R.string.new_notebook_add, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(ChgNoteBookActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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

class NotebookViewHolder extends RecyclerView.ViewHolder{
    View mView;

    TextView notebookTitle;
    public NotebookViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
    }
    public void setNotebookTitle(String title){
        notebookTitle = mView.findViewById(R.id.notebook_title);
        notebookTitle.setText(title);
    }
}
