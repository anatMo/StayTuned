package com.example.staytuned.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.staytuned.R;
import com.example.staytuned.fragments.Fragment_FilterEvents;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "tagMainActivity";
    private FloatingActionButton main_FAB_createEvent;
    private FloatingActionButton main_FAB_createNewEvent;
    private FirebaseAuth mAuth;
    private CollectionReference usersCollectionRef;

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public FirebaseAuth getFirebaseAuth() {
        return mAuth;
    }

    private void initializeFields() {
        mAuth = FirebaseAuth.getInstance();
        main_FAB_createEvent = findViewById(R.id.main_FAB_createEvent);
        main_FAB_createNewEvent = findViewById(R.id.main_FAB_createNewEvent);
    }


    private void initializeListeners() {
        main_FAB_createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createEventIntent = new Intent(MainActivity.this,
                        CreateEventActivity.class);
                startActivity(createEventIntent);

            }
        });

        main_FAB_createNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_FilterEvents fragment_filterEvents = new Fragment_FilterEvents();
                getSupportFragmentManager().beginTransaction().replace(R.id.panel_FRAME_content, fragment_filterEvents).commit();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
//        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                logout();
                return true;
            case R.id.menu_account:
                gotoAccount();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
//        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFormat(PixelFormat.RGBA_8888);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);

        setContentView(R.layout.activity_main);
        initializeFields();
        initializeListeners();
        initEvents();
    }


    @Override
    protected void onStart() {

        super.onStart();
        Log.d(TAG, "in onStart");
//      if current user is null, we want to log in
        checkIfUserExistsAndAct();

    }

    private void checkIfUserExistsAndAct() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // if user does not exist in auth, goto login
        if (currentUser == null) {
            gotoLogin();
        } else {

            // if he does exist in auth, he might still posses token (remains for one hour after
            // accout is deleted from auth
            String uid = currentUser.getUid();
        }
    }

    private void initEvents() {
        Fragment_FilterEvents fragment_filterEvents = new Fragment_FilterEvents();
        getSupportFragmentManager().beginTransaction().replace(R.id.panel_FRAME_content, fragment_filterEvents).commit();

    }

    private void gotoLogin() {
        Log.d(TAG, "in goToLogin");
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        // finish() makes sure user can't press back button and get here
        finish();
    }

    private void gotoAccount() {
        Intent accountIntent = new Intent(MainActivity.this, AccountActivity.class);
        accountIntent.putExtra(getString(R.string.arg_did_come_from_register_intent), false);

        startActivity(accountIntent);
        // finish() makes sure user can't press back button and get here
//        finish();
    }


    private void logout() {
        mAuth.signOut();
        gotoLogin();
        finish();
    }
}