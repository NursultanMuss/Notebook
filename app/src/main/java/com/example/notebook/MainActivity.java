package com.example.notebook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;



import com.firebase.ui.auth.AuthUI;

import com.github.clans.fab.FloatingActionButton;


import android.view.MenuItem;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    DatabaseReference mDatabase;
    //for Navigation Drawer
    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    //for Navigation Drawer

    ProgressDialog PD;


    FloatingActionButton fab_note, fab_task, fab_aim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ButterKnife.bind(this);

//        fabs leave hear in MainActivity
        fab_aim = (FloatingActionButton) findViewById(R.id.fab_aim);
        fab_note = (FloatingActionButton) findViewById(R.id.fab_note);
        fab_task = (FloatingActionButton) findViewById(R.id.fab_task);




// ProgressDialog maybe come in handy
        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);
// ProgressDialog maybe come in handy

//        btnSignOut = (Button) findViewById(R.id.sign_out_button);
//
//
//
//        btnSignOut.setOnClickListener(new View.OnClickListener() {
//            @Override            public void onClick(View view) {
//                auth.signOut();
//                FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
//                    @Override
//                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                        FirebaseUser user = firebaseAuth.getCurrentUser();
//                        if (user == null) {
//                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                            finish();
//                        }
//                    }
//                };
//            }
//        });
//
//        findViewById(R.id.change_password_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), ForgetAndChangePasswordActivity.class).putExtra("Mode", 1));
//            }
//        });
//
//        findViewById(R.id.change_email_button).setOnClickListener(new View.OnClickListener() {
//            @Override            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), ForgetAndChangePasswordActivity.class).putExtra("Mode", 2));
//            }
//        });
//
//        findViewById(R.id.delete_user_button).setOnClickListener(new View.OnClickListener() {
//            @Override            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), ForgetAndChangePasswordActivity.class).putExtra("Mode", 3));
//            }
//        });

        /*For Navigation Drawer leave in MainActivity*/
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new AllNotesFragment()).commit();
        toolbar = findViewById(R.id.draw_toolbar);
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
                        break;
                    case R.id.nav_notebooks:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new AllNotebooksFragment()).commit();
                        break;
                    case R.id.nav_tasks:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new AllTasksFragment()).commit();
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



    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }




    public void ClickMakeNote(View v) {
        Intent intent = new Intent(MainActivity.this, MakeNoteActivity.class);
        startActivity(intent);
    }

    public void ClickMakeTask(View v) {
        Intent intent = new Intent(MainActivity.this, MakeTaskActivity.class);
        startActivity(intent);
    }

    public void ClickMakeAim(View v) {
        Intent intent = new Intent(MainActivity.this, MakeAimActivity.class);
        startActivity(intent);
    }
}
