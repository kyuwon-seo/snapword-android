package com.example.myapplication.listview;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

import static android.os.UserHandle.readFromParcel;

public class ListViewSetWordItem implements Parcelable {

    private String wordA;
    private String wordB;
    private String hint;
    private Bitmap imgBit;
    public ListViewSetWordItem(){}

    public void setWordA(String A){wordA = A;}
    public void setWordB(String B){wordB = B;}
    public void setHint(String C){hint = C;}
    public void setImgBit(Bitmap bit){imgBit = bit;}

    public String getWordA(){return this.wordA;}
    public String getWordB(){return this.wordB;}
    public String getHint(){return this.hint;}
    public Bitmap getImgBit(){return this.imgBit;}

    protected ListViewSetWordItem(Parcel in) {
        wordA = in.readString();
        wordB = in.readString();
        hint = in.readString();
        imgBit = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<ListViewSetWordItem> CREATOR = new Creator<ListViewSetWordItem>() {
        @Override
        public ListViewSetWordItem createFromParcel(Parcel in) {
            return new ListViewSetWordItem(in);
        }

        @Override
        public ListViewSetWordItem[] newArray(int size) {
            return new ListViewSetWordItem[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(wordA);
        dest.writeString(wordB);
        dest.writeString(hint);
        dest.writeParcelable(imgBit, flags);
    }
}
