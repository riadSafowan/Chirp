package com.example.chirp.main;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.chirp.R;
import com.example.chirp.login.LoginActivity;
import com.example.chirp.profile.ProfileActivity;
import com.example.chirp.search.findFriends.FindFriendsActivity;
import com.example.chirp.settings.SettingsActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabsAccessorAdapter myTabsAccessorAdapter;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        initialize();
        try {
            currentUserId = currentUser.getUid();
        } catch (NullPointerException npe) {
            firebaseAuth.signOut();
            SendUserToLoginActivity();
        }
        databaseReference.child("User").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ((snapshot.exists()) && snapshot.hasChild("userName")) {
                    String retrievedUserName = Objects.requireNonNull(snapshot.child("userName").getValue()).toString();
                    Objects.requireNonNull(getSupportActionBar()).setTitle(retrievedUserName);
                } else {
//                    Toast.makeText(MainActivity.this, "Name doesn't exist", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    SendUserToLoginActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        setSupportActionBar(mToolbar);
//      Objects.requireNonNull(getSupportActionBar()).setTitle("Chirp");
        myTabsAccessorAdapter = new TabsAccessorAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(myTabsAccessorAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser == null) {
            SendUserToLoginActivity();
        }
    }

    private void initialize() {
        mToolbar = findViewById(R.id.main_page_toolbar);
        mViewPager = findViewById(R.id.main_tabs_pager);
        mTabLayout = findViewById(R.id.main_tabs);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.settings) {
            SendUserToSettingsActivity();
        }
        if (item.getItemId() == R.id.logout) {
            firebaseAuth.signOut();
            SendUserToLoginActivity();
        }
        if (item.getItemId() == R.id.exit) {
            exitApp();
        }
        if (item.getItemId() == R.id.find_friends) {

        }
        if (item.getItemId() == R.id.profile) {
            SendUserToProfileActivity();
        }
        if (item.getItemId() == R.id.find_friends) {
            sendUserToFindFriendActivity();
        }
        return true;
    }

    private void sendUserToFindFriendActivity() {
        Intent intent = new Intent(MainActivity.this, FindFriendsActivity.class);
        startActivity(intent);
    }

    private void SendUserToLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void SendUserToProfileActivity() {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        intent.putExtra("id", currentUserId);
        startActivity(intent);
    }

    private void SendUserToSettingsActivity() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void exitApp() {
        finishAffinity();
        System.exit(0);
    }
}