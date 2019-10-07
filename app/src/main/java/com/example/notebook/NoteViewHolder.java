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
    }

    public void setNoteTitle(String title){
        noteTitle = mView.findViewById(R.id.tvNoteTitle);
        noteTitle.setText(title);
    }

    public void setNoteContent(String content){
        noteContent = mView.findViewById(R.id.tvNoteText);
        noteContent.setText(content);
    }

    public void setNoteTime(String time){
        noteTime = mView.findViewById(R.id.tvNoteDate);
        noteTime.setText(time);
    }
}
