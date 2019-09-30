package com.example.notebook.adapters;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.example.notebook.models.Notebook;

import java.util.List;

public class NewNotebookAdapter extends RecyclerView.Adapter<> {
    Context context;
    List<Notebook> notebookList;

    public NewNotebookAdapter(Context _context, List<Notebook> _notebookList){
        this.context = _context;
        this.notebookList = _notebookList;
    }


}
