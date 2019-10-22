package com.example.notebook;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    View mView;

    TextView taskText;
    CardView cardView;

    public TaskViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setTaskText(String title){
        taskText = mView.findViewById(R.id.tvTaskText);
        taskText.setText(title);
    }

    public void setTaskPriority(int priority){
        cardView = mView.findViewById(R.id.task_card_view);
        switch (priority){
            case 1:
                cardView.setCardBackgroundColor(mView.getResources().getColor(R.color.task_red));
                break;
            case 2:
                cardView.setCardBackgroundColor(mView.getResources().getColor(R.color.task_orange));
                break;
            case 3:
                cardView.setCardBackgroundColor(mView.getResources().getColor(R.color.task_coral));
                break;
        }
    }
}
