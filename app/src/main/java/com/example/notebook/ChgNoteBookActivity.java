package com.example.notebook;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import butterknife.BindView;

public class ChgNoteBookActivity extends AppCompatActivity implements Dialog.DialogListener {

    private Toolbar mtoolbar;
    private Button button_chg_nb;
    private ListView listView;


    private final int DIALOG=1;
    private DialogFragment dlg1;
    private DatabaseReference f_notebook_ref;
    private FirebaseAuth fAuth;
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

        listView = findViewById(R.id.lv_change_notebook);
        adapter  = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, arrayList);

        listView.setAdapter(adapter);

        fAuth = FirebaseAuth.getInstance();
        f_notebook_ref = FirebaseDatabase.getInstance().getReference().child("Notebooks").child(fAuth.getCurrentUser().getUid());
        f_notebook_ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for(DataSnapshot childSnapShot :dataSnapshot.getChildren()) {
                    if (childSnapShot.child("notebookName").getValue() != null) {
                        String notebookName = dataSnapshot.child("notebookName").getValue().toString();
                        arrayList.add(notebookName);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for(DataSnapshot childSnapShot :dataSnapshot.getChildren()) {
                    if (childSnapShot.child("notebookName").getValue() != null) {
//                        String notebookName = dataSnapshot.child("notebookName").getValue().toString();
//                        arrayList.add(notebookName);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapShot :dataSnapshot.getChildren()) {
                    if (childSnapShot.child("notebookName").getValue() != null) {
                        String notebookName = dataSnapshot.child("notebookName").getValue().toString();
                        arrayList.remove(notebookName);
                        adapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                adapter.notifyDataSetChanged();
            }
        });

        dlg1 = new DialogFragment();

        setSupportActionBar(mtoolbar);
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true);
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
    public void applyText(String new_notebook) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Notebook notebook = new Notebook(firebaseUser.getDisplayName(),new_notebook);
        f_notebook_ref.setValue(notebook);
        Intent intent = new Intent();
        intent.putExtra("new", new_notebook);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    //    @Override
    //    protected void onStart() {
    //        super.onStart();
    //        Query query= f_notebook_ref;
    //        FirebaseRecyclerOptions<Notebook> options =
    //                new FirebaseRecyclerOptions.Builder<Notebook>()
    //                .setQuery(query, new SnapshotParser<Notebook>() {
    //                    @NonNull
    //                    @Override
    //                    public Notebook parseSnapshot(@NonNull DataSnapshot snapshot) {
    //                        return new Notebook(snapshot.child("user").getValue().toString(),
    //                                snapshot.child("notebookName").getValue().toString());
    //                    }
    //                })
    //                .build();
    //    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//    }
//
//    public class NotebookViewHolder extends RecyclerView.ViewHolder{
//
//        View mView;
//        public NotebookViewHolder(@NonNull View itemView) {
//            super(itemView);
//        }
//    }
}
