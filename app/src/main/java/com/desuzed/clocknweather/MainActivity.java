package com.desuzed.clocknweather;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.desuzed.clocknweather.databinding.ActivityMainBinding;
import com.desuzed.clocknweather.util.PagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private String [] tabNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind();
        hideStatusBar();
        initTabs();
    }

    private List<Fragment> getFragmentsList(){
        List<Fragment> fragments = new ArrayList<>();
        ClockFragment clockFragment = ClockFragment.newInstance();
        WeatherFragment weatherFragment = WeatherFragment.newInstance();
        fragments.add(clockFragment);
        fragments.add(weatherFragment);
        return fragments;
    }

    private void bind (){
        com.desuzed.clocknweather.databinding.ActivityMainBinding amb = ActivityMainBinding.inflate(getLayoutInflater());
        View view = amb.getRoot();
        setContentView(view);
        viewPager = amb.viewPager;
        tabLayout = amb.tabLayout;
    }

    private void hideStatusBar() {
        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
//        ActionBar actionBar = getActionBar();
//        actionBar.hide();
    }

    private void initTabs (){
        tabNames  = new String[]{getResources().getString(R.string.clock), getResources().getString(R.string.weather)};
        PagerAdapter adapter = new PagerAdapter(this, getFragmentsList());
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(tabNames[position])).attach();

    }
}