package com.example.notebook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;


import com.example.notebook.fragments.AllNotebooksFragment;
import com.example.notebook.fragments.AllNotesFragment;
import com.example.notebook.fragments.AllTasksFragment;
import com.example.notebook.fragments.Dialog;
import com.example.notebook.fragments.TaskDialog;
import com.firebase.ui.auth.AuthUI;

import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.github.clans.fab.FloatingActionButton;


import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Arrays;
import java.util.Locale;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity  {
    DatabaseReference mDatabase;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser fUser;
    public static final int RC_SIGN_IN = 144;
    private String mUsername;

    //for Navigation Drawer
    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    //for Navigation Drawer

    ProgressDialog PD;


    FloatingActionButton fab_note, fab_task;
    FloatingActionMenu fab_menu;

    //for SharedPreference
    public static final String KEY_LANGUAGE_PREF = "language";
    public static final String ENGLISH_LOCALE = "en";
    public static final String KAZAKH_LOCALE = "kk";
    public static final String RUSSIAN_LOCALE = "ru";
    private Locale locale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        fAuth = FirebaseAuth.getInstance();
        fUser=fAuth.getCurrentUser();
        ButterKnife.bind(this);

//        fabs leave hear in MainActivity
        fab_menu = findViewById(R.id.menu);
        fab_note = (FloatingActionButton) findViewById(R.id.fab_note);
        fab_task = (FloatingActionButton) findViewById(R.id.fab_task);




// ProgressDialog maybe come in handy
        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);
// ProgressDialog maybe come in handy


        /*For Navigation Drawer leave in MainActivity*/
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new AllNotesFragment()).commit();
        toolbar = findViewById(R.id.draw_toolbar);
        GetTimeAgo getTimeAgo =  new GetTimeAgo();
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.draw_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_notes:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new AllNotesFragment()).commit();
                        toolbar.setTitle(R.string.toolbar_all_notes);
                        break;
                    case R.id.nav_notebooks:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new AllNotebooksFragment()).commit();
                        toolbar.setTitle(R.string.toolbar_all_notebooks);
                        break;
                    case R.id.nav_tasks:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new AllTasksFragment()).commit();
                        toolbar.setTitle(R.string.toolbar_all_tasks);
                        break;
                    case R.id.nav_settings:
                        Intent intent = new Intent(MainActivity.this,Settings2Activity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_log_out:
                        AuthUI.getInstance().signOut(MainActivity.this);
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar
                , R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        /*For Navigation Drawer leave in MainActivity*/

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser fUser = firebaseAuth.getCurrentUser();
                if (fUser != null) {
                    //User is signed in
                    onSingedInInitialize(fUser.getDisplayName());
                } else {
                    //User is signed out
                    onSignedOutCleanUp();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

    }

    @Override
    public void onResume() {
        super.onResume();
        fAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        fAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void onSingedInInitialize(String username) {
        mUsername = username;
//        final Map authMap = new HashMap();
//        authMap.put("name",mUsername);
//        fNoteDataBase.child("Users").push().setValue(authMap);
    }

    private void onSignedOutCleanUp() {
    }



    public void ClickMakeNote(View v) {
        fab_menu.close(true);
        Intent intent = new Intent(MainActivity.this, MakeNoteActivity.class);
        startActivity(intent);
    }

    public void ClickMakeTask(View v) {
        TaskDialog taskDialog = new TaskDialog();
        fab_menu.close(true);
        taskDialog.show(getSupportFragmentManager(),"taskDialog");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_language) {
//            Modifying Locale if User clicked language from options pane
            Resources resources = getResources();
            SharedPreferences sharedPreferences = getSharedPreferences("localePref", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            if(sharedPreferences.getString(KEY_LANGUAGE_PREF, RUSSIAN_LOCALE).equals(ENGLISH_LOCALE)){
                locale = new Locale(RUSSIAN_LOCALE);
                editor.putString(KEY_LANGUAGE_PREF, RUSSIAN_LOCALE);
            } else {
                locale = new Locale(ENGLISH_LOCALE);
                editor.putString(KEY_LANGUAGE_PREF, ENGLISH_LOCALE);
            }
            editor.apply();

            Configuration configuration = resources.getConfiguration();
            configuration.setLocale(locale);
            getBaseContext().getResources().updateConfiguration(configuration,
                    getBaseContext().getResources().getDisplayMetrics());
            recreate();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        SharedPreferences sharedPreferences = getSharedPreferences("localePref", MODE_PRIVATE);

        MenuItem item = menu.getItem(1);
        if(sharedPreferences.getString(KEY_LANGUAGE_PREF, ENGLISH_LOCALE).equals(RUSSIAN_LOCALE)){
            item.setTitle("English");
        } else {
            item.setTitle("Русский");

        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == RC_SIGN_IN) {
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "Signed in, yuuuhoo!", Toast.LENGTH_SHORT).show();
                } else if (response == null) {
                    Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                    finish();

                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

//    @Override
//    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        if (key.equals(KEY_LANGUAGE_PREF)) {
////            langListPref.setSummary(sharedPreferences.getString(KEY_LANGUAGE_PREF,""));
//            Resources resources = getResources();
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            if (sharedPreferences.getString(KEY_LANGUAGE_PREF, RUSSIAN_LOCALE).equals(ENGLISH_LOCALE)) {
//                locale = new Locale(RUSSIAN_LOCALE);
//                editor.putString(KEY_LANGUAGE_PREF, RUSSIAN_LOCALE);
//            } else {
//                locale = new Locale(ENGLISH_LOCALE);
//                editor.putString(KEY_LANGUAGE_PREF, ENGLISH_LOCALE);
//            }
//            if (sharedPreferences.getString(KEY_LANGUAGE_PREF, RUSSIAN_LOCALE).equals(KAZAKH_LOCALE)) {
//                locale = new Locale(RUSSIAN_LOCALE);
//                editor.putString(KEY_LANGUAGE_PREF, RUSSIAN_LOCALE);
//            } else {
//                locale = new Locale(KAZAKH_LOCALE);
//                editor.putString(KEY_LANGUAGE_PREF, KAZAKH_LOCALE);
//            }
//            editor.apply();
////            Thread langthread = new Thread(new Runnable() {
////                @Override
////                public void run() {
////
////                }
////            });
////            langthread.run();
//            Configuration configuration = resources.getConfiguration();
//            configuration.setLocale(locale);
//            getBaseContext().getResources().updateConfiguration(configuration,
//                    getBaseContext().getResources().getDisplayMetrics());
//            recreate();
//        }
//    }

}
