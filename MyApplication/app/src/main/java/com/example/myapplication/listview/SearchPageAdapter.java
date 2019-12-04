package com.example.myapplication.listview;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myapplication.fragment.FragmentSearchFolder;
import com.example.myapplication.fragment.FragmentSearchSet;
import com.example.myapplication.fragment.FragmentSearchUser;

public class SearchPageAdapter extends FragmentPagerAdapter {

    int mNumOfTabs; //tab의 갯수
    FragmentSearchSet setTab = new FragmentSearchSet();
    FragmentSearchFolder folderTab = new FragmentSearchFolder();
    FragmentSearchUser userTab = new FragmentSearchUser();

    public SearchPageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.mNumOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return setTab;
            case 1:
                return folderTab;
            case 2:
                return userTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}