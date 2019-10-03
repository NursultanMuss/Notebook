package com.example.notebook.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.notebook.R;
import com.google.android.material.textfield.TextInputEditText;

public class NotebookChooseFragment extends DialogFragment implements View.OnClickListener {
    TextInputEditText newNotebook;
    Button buttonOk, buttonCancel;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Новый блокнот");
        View view = inflater.inflate(R.layout.fragment_notebook_choose, null);
        newNotebook = view.findViewById(R.id.edit_text_new_notebook);
        buttonCancel = view.findViewById(R.id.button_cancel);
        buttonOk = view.findViewById(R.id.button_ok);

        buttonOk.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        intent =new Intent();
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_cancel:
                dismiss();
                break;
            case R.id.button_ok:

                }else{
                    getActivity().setResult(Activity.RESULT_CANCELED,intent);
                    getActivity().finish();
                }

        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d("notebookFragment", "Dialog dismissed");
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d("notebookFragment", "Dialog canceled");
    }
}
