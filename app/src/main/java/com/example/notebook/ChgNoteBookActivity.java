package com.example.notebook;


import android.content.Intent;
import android.os.Bundle;
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

import butterknife.BindView;

public class ChgNoteBookActivity extends AppCompatActivity {

    private Toolbar mtoolbar;
    private Button button_chg_nb;
    private RecyclerView recyclerView;
    private final int DIALOG=1;
    private DialogFragment dlg1;

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
                dlg1.show(getSupportFragmentManager(),"dlg1");
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void onChooseNoteBook(View view){
        Intent intent = new Intent();

        intent.putExtra("notebook", )
    }
}
