package com.example.layout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class LoginActivity extends AppCompatActivity {

    private TabLayout tablayout;
    private ViewPager2 viewPager2;
    private LoginPageAdapter loginPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tablayout = findViewById(R.id.login_tablayout);
        viewPager2 = findViewById(R.id.loginPager);

        tablayout.addTab(tablayout.newTab().setText(getString(R.string.signin)));
        tablayout.addTab(tablayout.newTab().setText(getString(R.string.signup)));

        int selectedColor = getResources().getColor(R.color.black);
        int unselectedColor = getResources().getColor(R.color.lavender);
        tablayout.setTabTextColors(unselectedColor, selectedColor);

        FragmentManager fragmentManager = getSupportFragmentManager();
        loginPageAdapter = new LoginPageAdapter(fragmentManager, getLifecycle());

        viewPager2.setAdapter(loginPageAdapter);

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tablayout.selectTab(tablayout.getTabAt(position));
            }
        });
    }
}