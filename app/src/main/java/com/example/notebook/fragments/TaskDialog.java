package com.example.notebook.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.example.notebook.MakeNoteActivity;
import com.example.notebook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class TaskDialog extends DialogFragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private static final String TAG = "TaskDialog";
    private EditText et_task;
    private RadioGroup rgPriority;
    private RadioButton radioButtonHigh;
    private RadioButton radioButtonMedium;
    private RadioButton radioButtonLow;
    private int priority;

    private FirebaseAuth fAuth;
    private DatabaseReference fNoteDataBase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle(R.string.new_task);
        View view = inflater.inflate(R.layout.dialog_task, null);
        et_task= view.findViewById(R.id.et_task);
        //RadioGroup and RadioButton
        priority=1;
        radioButtonHigh = view.findViewById(R.id.radio_high);
        radioButtonMedium = view.findViewById(R.id.radio_medium);
        radioButtonLow = view.findViewById(R.id.radio_low);

        radioButtonHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                if(checked)
                    priority = 1;
            }
        });
        radioButtonMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                if(checked)
                    priority = 2;
            }
        });
        radioButtonLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                if(checked)
                    priority = 3;
            }
        });
        //RadioGroup and RadioButton
        rgPriority = view.findViewById(R.id.rg_priority);

        Button btn_positive = view.findViewById(R.id.btnYes);
        Button btn_negative = view.findViewById(R.id.btnNo);
        ImageButton btn_date_time_picker = view.findViewById(R.id.btn_date_time_picker);
        btn_positive.setOnClickListener(this);
        btn_negative.setOnClickListener(this);
        btn_date_time_picker.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        fAuth = FirebaseAuth.getInstance();
        fNoteDataBase = FirebaseDatabase.getInstance().getReference().child("Tasks")
                .child(fAuth.getCurrentUser().getUid());
        fNoteDataBase.keepSynced(true);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnYes:
                String task = et_task.getText().toString().trim();
                addTask(task,priority);
                break;
            case R.id.btnNo:
                dismiss();
                break;
            case R.id.btn_date_time_picker:
                DialogFragment dateTimePicker = new DateTimePickerDialog();
                dateTimePicker.show(getFragmentManager(),"date time picker");
                break;
        }

    }

    private void addTask(String taskText, int priority){
        if(fAuth.getCurrentUser() != null){
            final DatabaseReference newTaskRef = fNoteDataBase.push();

            final Map noteMap = new HashMap();
            noteMap.put("taskText", taskText);
            noteMap.put("priority", priority);
            noteMap.put("timeStamp", ServerValue.TIMESTAMP);

            Thread mainThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    newTaskRef.setValue(noteMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getActivity(), "Заметка добавлена в блокнот", Toast.LENGTH_SHORT).show();
                                dismiss();
                            }else{
                                Toast.makeText(getActivity(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
            mainThread.run();
        }
    }
}
