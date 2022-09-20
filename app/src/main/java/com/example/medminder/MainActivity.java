package com.example.medminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import android.app.Notification;
import android.app.NotificationManager;
import android.provider.Settings;
import android.support.v4.media.session.IMediaSession;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    // Tab labels
    public static final String MEDICATION_TAB = "Medication";
    public static final String TODAY_TAB = "Today";
    public static final String INFORMATION_TAB = "Information";

    // Fragments
    private MedicationFragment mMedicationFragment;
    private TodayFragment mTodayFragment;
    private InformationFragment mInformationFragment;

    // Widgets for swipe-able tabs
    private ViewPager2 mViewPager;
    private TabLayout mTabLayout;
    private TabsViewPageAdapter mTabsViewPageAdapter;
    private ArrayList<Fragment> fragments;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find and initiate widgets
        mViewPager = (ViewPager2) findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);

        //Initiate fragments and put them in ArrayList
        mMedicationFragment = new MedicationFragment();
        mTodayFragment = new TodayFragment();
        mInformationFragment = new InformationFragment();
        fragments = new ArrayList<>();
        fragments.add(mMedicationFragment);
        fragments.add(mTodayFragment);
        fragments.add(mInformationFragment);

        //Bind ArrayList to adapter and TabView
        mTabsViewPageAdapter = new TabsViewPageAdapter(this, fragments);
        mViewPager.setAdapter(mTabsViewPageAdapter);

        //Create tab labels
        TabLayoutMediator.TabConfigurationStrategy tabConfigurationStrategy = new
                TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(TabLayout.Tab tab, int position) {
                        if(position == 0) tab.setText(MEDICATION_TAB);
                        else if(position == 1) tab.setText(TODAY_TAB);
                        else if(position == 2) tab.setText(INFORMATION_TAB);
                    }
                };
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(mTabLayout, mViewPager, tabConfigurationStrategy);
        tabLayoutMediator.attach();

    }

}