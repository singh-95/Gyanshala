package com.apps.devamit.gyanshala;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ActionBar actionBar;

    private static final int RC_SIGN_IN = 123;
    private static FirebaseUser thisUser;
    private DatabaseReference mDatabase;
    List<AuthUI.IdpConfig> providers = Collections.singletonList(
            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout= findViewById(R.id.drawer_layout);
        mDrawerToggle=new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_opened, R.string.drawer_closed) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        actionBar=getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            mDrawerToggle.syncState();
        }

        thisUser=FirebaseAuth.getInstance().getCurrentUser();
        //if this User==null the user hasn't signed in even once
        if(thisUser==null) {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setLogo(R.mipmap.ic_launcher_round)
                    .build(), RC_SIGN_IN);
            onPause();
        } else
            loggedIn();
    }

    private void loggedIn() {
        Toast.makeText(this, "the current user is : " + thisUser.getDisplayName(), Toast.LENGTH_SHORT).show();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("gyanshala-hackathon");
        myRef.setValue("let's get this done");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_OK) {
            thisUser= FirebaseAuth.getInstance().getCurrentUser();
            loggedIn();
        } else
            Toast.makeText(this, "sign-in failed", Toast.LENGTH_SHORT).show();
    }
}
