package com.example.notebook;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notebook.fragments.Dialog;
import com.example.notebook.models.Notebook;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;

public class ChgNoteBookActivity extends AppCompatActivity implements Dialog.DialogListener {

    private Toolbar mtoolbar;
    private Button button_chg_nb;
    private RecyclerView recyclerView;
    private final int DIALOG=1;
    private DialogFragment dlg1;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chg_note_book);
        mtoolbar = findViewById(R.id.chg_note_toolbar);
        button_chg_nb = findViewById(R.id.change_notebook_btn);
        recyclerView = findViewById(R.id.recycler_v_change_notebook);

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
        mDatabase = FirebaseDatabase.getInstance().getReference("Notebooks");
        String userId = mDatabase.push().getKey();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Notebook notebook = new Notebook(firebaseUser.toString(),new_notebook);
        mDatabase.child(userId).setValue(notebook);
        Intent intent = new Intent();
        intent.putExtra("new", new_notebook);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
