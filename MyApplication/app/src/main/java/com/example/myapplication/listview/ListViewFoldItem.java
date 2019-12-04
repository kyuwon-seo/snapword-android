package com.example.myapplication.listview;

import android.graphics.drawable.Drawable;

public class ListViewFoldItem {
    private Drawable iconDrawable ;
    private String titleStr ;
    private String descStr ;
    private int folder_no;

    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setDesc(String desc) {
        descStr = desc ;
    }
    public void setFolder_no(int no) { folder_no = no;}

    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getDesc() {
        return this.descStr ;
    }
    public int getFolder_no() { return this.folder_no ;}
}