package com.example.notebook;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotebookViewHolder extends RecyclerView.ViewHolder{
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
