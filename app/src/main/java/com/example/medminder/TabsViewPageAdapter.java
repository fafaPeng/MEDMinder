package com.example.medminder;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class TabsViewPageAdapter extends FragmentStateAdapter {
    private ArrayList<Fragment> fragments;

    public TabsViewPageAdapter(FragmentActivity fragmentActivity, ArrayList<Fragment> fragments){
        super(fragmentActivity);
        this.fragments = fragments;
    }
    @Override
    public int getItemCount() {
        return fragments.size();
    }
    @Override
    public Fragment createFragment(int position) { return fragments.get(position); }

}
