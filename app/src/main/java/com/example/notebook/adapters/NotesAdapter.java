package com.example.notebook.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notebook.R;
import com.example.notebook.models.Note;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    Context context;
    List<Note> noteList = new ArrayList<>();

    public NotesAdapter(Context context, List<Note> noteList){
        this.context= context;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public NotesAdapter.NotesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_note, viewGroup,false);
        NotesViewHolder vh = new NotesViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.NotesViewHolder notesViewHolder, int i) {
        notesViewHolder.tvTitle.setText(noteList.get(i).getTitle());
        notesViewHolder.tvNote.setText(noteList.get(i).getContent());
        notesViewHolder.tvTime.setText(noteList.get(i).getTimestamp());

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle,tvNote, tvTime;
        public NotesViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvNoteTitle);
            tvNote = itemView.findViewById(R.id.tvNoteText);
            tvTime = itemView.findViewById(R.id.tvNoteDate);

        }
    }
}
