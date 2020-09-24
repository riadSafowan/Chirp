package com.example.chirp.search.findFriends;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.chirp.R;
import com.example.chirp.search.SearchTabsAccessorAdapter;
import com.google.android.material.tabs.TabLayout;

public class FindFriendsActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private SearchTabsAccessorAdapter mTabsAccessorAdapter;
    private Toolbar toolbar;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_find_friend);

        initialize();
    }

    private void initialize() {
        mViewPager = findViewById(R.id.find_friend_tabs_pager);
        mTabLayout = findViewById(R.id.find_friend_tabs);
        mTabsAccessorAdapter = new SearchTabsAccessorAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mTabsAccessorAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        toolbar = findViewById(R.id.find_friends_page_toolbar);
        backButton = findViewById(R.id.find_friends_page_back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Intent intent= new Intent(FindFriendsActivity.this , MainActivity.class);
//                    startActivity(intent);
                finish();
            }
        });
    }
}