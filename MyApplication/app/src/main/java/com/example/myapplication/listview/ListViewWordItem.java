package com.example.myapplication.listview;

import android.graphics.Bitmap;

public class ListViewWordItem {

    private String wordA ;
    private String wordB ;
    private String hint;
    private String imgPath;
    private String imgName;
    private Bitmap image ;
    private int num;

    public void setWordA(String wordA) { this.wordA = wordA ; }
    public void setWordB(String wordB) {
        this.wordB = wordB ;
    }
    public void setImgPath(String path) {
        this.imgPath = path ;
    }
    public void setImgName(String name) { this.imgName = name; }
    public void setImage(Bitmap image) { this.image = image ; }
    public void setNum(int num) {this.num = num;}
    public String getHint() {
        return hint;
    }
    public void setHint(String hint) {
        this.hint = hint;
    }
    public String getWordA() {
        return this.wordA ;
    }
    public String getWordB() {
        return this.wordB ;
    }
    public String getImgPath() { return this.imgPath; }
    public String getImgName() { return this.imgName; }
    public Bitmap getImage() { return this.image ; }
    public int getNum(){ return this.num; }
}