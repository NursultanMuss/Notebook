package com.example.notebook.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.notebook.R;
import com.google.firebase.database.FirebaseDatabase;

public class Dialog extends AppCompatDialogFragment {

    private EditText et_notebook;
    private DialogListener listener;

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_new, null);

        builder.setView(view)
                .setTitle(R.string.new_notebook)
                .setNegativeButton("ОТМЕНА", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(" ОК", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String new_notebook = et_notebook.getText().toString().trim();
                        listener.applyText(new_notebook);
                    }
                });

        et_notebook = (EditText) view.findViewById(R.id.edit_new_notebook);
        return builder.create();
    }

    public interface DialogListener {
        void applyText(String new_notebook);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DialogListener)context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }
}
