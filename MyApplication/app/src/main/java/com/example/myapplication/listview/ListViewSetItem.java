package com.example.myapplication.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;
public class ListViewSetItem {

    private String set_Name ;
    private int word_cnt ;
    private String owner_id ;
    private int set_no ;
    private int set_Click;
    public String user_id;

    public void setSet_Name(String name) {
        set_Name = name ;
    }
    public void setWord_cnt(int cnt) {
        word_cnt = cnt ;
    }
    public void setOwner_id(String id) { owner_id = id;}
    public void setSet_no(int no) { set_no = no; }
    public void setSet_Click(int click) { set_Click = click; }
    public void setUser_id(String id) { user_id = id;}

    public String getSet_Name() {
        return this.set_Name ;
    }
    public int getWord_cnt() {
        return this.word_cnt ;
    }
    public String getOwner_id() { return this.owner_id ; }
    public int getSet_no() { return this.set_no ; }
    public int getSet_Click() { return this.set_Click ; }
    public String getUser_id() { return this.user_id ; }

}
