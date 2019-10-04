package com.example.notebook.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notebook.models.Notebook;

import java.util.List;

public class NewNotebookAdapter extends RecyclerView.Adapter<NewNotebookAdapter.NewNotesViewHolder> {
    Context context;
    List<Notebook> notebookList;

    public NewNotebookAdapter(Context _context, List<Notebook> _notebookList){
        this.context = _context;
        this.notebookList = _notebookList;
    }

    @NonNull
    @Override
    public NewNotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull NewNotesViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class NewNotesViewHolder extends RecyclerView.ViewHolder{
        public NewNotesViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
