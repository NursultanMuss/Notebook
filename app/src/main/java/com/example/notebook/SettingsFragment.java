package com.example.notebook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private FirebaseUser fUser;
    private FirebaseDatabase fDatabase;
    private FirebaseAuth fAuth;
    private static final String TAG = "SettingsFragment";
    private static final int PICK_NOTEBOOK_REQUEST = 2;
    public static final String KEY_NOTEBOOK_PREF = "NOTEBOOK_PREF";
    public static final String KEY_LANGUAGE_PREF = "language";
    public static final String ENGLISH_LOCALE = "en";
    public static final String KAZAKH_LOCALE = "kk";
    public static final String RUSSIAN_LOCALE = "ru";
    private Locale locale;

    private Preference notebook_preference;
    private ListPreference langListPref;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences sp = getPreferenceManager().getSharedPreferences();
        PreferenceScreen sharedPreferences = getPreferenceScreen();
        //Language Preference
//        langListPref = sharedPreferences.findPreference(KEY_LANGUAGE_PREF);

        //Language Preference
        //Notebook Preference

        notebook_preference = sharedPreferences.findPreference(KEY_NOTEBOOK_PREF);
        if (notebook_preference != null) {
            notebook_preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(), ChgNoteBookActivity.class);
                    startActivityForResult(intent, PICK_NOTEBOOK_REQUEST);
                    return true;
                }
            });
        }
        //Notebook Preference


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == PICK_NOTEBOOK_REQUEST) {
                String notebookName = data.getStringExtra("new");
                notebook_preference.setSummary(notebookName);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences pref = getPreferenceManager().getSharedPreferences();
        onSharedPreferenceChanged(pref, KEY_LANGUAGE_PREF);
//        onSharedPreferenceChanged(pref,KEY_NOTEBOOK_PREF);

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
