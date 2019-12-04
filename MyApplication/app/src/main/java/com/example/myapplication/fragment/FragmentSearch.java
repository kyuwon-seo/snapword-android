package com.example.myapplication.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.R;
import com.example.myapplication.listview.SearchPageAdapter;
import com.google.android.material.tabs.TabLayout;

import static com.example.myapplication.MainActivity.homeBar;

public class FragmentSearch extends Fragment {

    EditText search_Btn;
    TabLayout tabs;
    public static View searchActionBar;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_search, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        searchActionBar = getLayoutInflater().inflate(R.layout.actionbar_search, null);
        homeBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        homeBar.setCustomView(searchActionBar);
        homeBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME |
                ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);

        tabs = v.findViewById(R.id.search_tab);
        tabs.addTab(tabs.newTab().setText("세트"));
        tabs.addTab(tabs.newTab().setText("폴더"));
        tabs.addTab(tabs.newTab().setText("사용자"));

        //어답터 설정
        final ViewPager viewPager = v.findViewById(R.id.pager_content);
        viewPager.setOffscreenPageLimit(10);
        final SearchPageAdapter pageAdapter = new SearchPageAdapter(getChildFragmentManager(), tabs.getTabCount());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return v;
    }

}