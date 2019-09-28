package com.example.notebook;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    View mView;

    TextView noteTitle, noteContent, noteTime;
    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        noteTitle = mView.findViewById(R.id.tvNoteTitle);
        noteContent = mView.findViewById(R.id.tvNoteText);
        noteTime = mView.findViewById(R.id.tvNoteDate);
    }

    public void setNoteTitle(String title){
        noteTitle.setText(title);
    }

    public void setNoteContent(String content){
        noteContent.setText(content);
    }

    public void setNoteTime(int time){
        noteTime.setText(time);
    }
}
