package com.example.engineerdemo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.engineerdemo.view.HomeFragment;

import timber.log.Timber;


public class MainActivity extends AppCompatActivity {

    public Toolbar toolbar;
    public TextView toolbarTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");

        toolbarTitle = findViewById(R.id.toolbarTitle);

        if (savedInstanceState == null) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                addFragment(new HomeFragment(), false);
            }
        }
    }

    public void addFragment(Fragment fragment, boolean addToBackStack) {

        if (addToBackStack)
            getSupportFragmentManager().beginTransaction().replace(R.id.home_container, fragment).addToBackStack(null).commit();
        else
            getSupportFragmentManager().beginTransaction().add(R.id.home_container, fragment).commit();

    }

    public void removeAllBackStack() {
        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            getSupportFragmentManager().popBackStack();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Timber.d("MainActivity: onDestroy");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


}
