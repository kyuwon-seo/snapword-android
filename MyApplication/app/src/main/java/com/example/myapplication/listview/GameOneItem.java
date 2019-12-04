package com.example.myapplication.listview;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class GameOneItem implements Parcelable {
    private String cardText ;
    private String hint;
    private Bitmap cardImg ;
    public GameOneItem(){}

    public void setCardText(String title) {
        cardText = title ;
    }
    public void setHint(String h) {hint = h; }
    public void setCardImg(Bitmap img) { cardImg = img; }

    public String getCardText() {
        return this.cardText ;
    }
    public String getHint() { return this.hint; }
    public Bitmap getCardImg() { return this.cardImg ; }
////////////////
    protected GameOneItem(Parcel in) {
        cardText = in.readString();
        hint = in.readString();
        cardImg = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<GameOneItem> CREATOR = new Creator<GameOneItem>() {
        @Override
        public GameOneItem createFromParcel(Parcel in) {
            return new GameOneItem(in);
        }

        @Override
        public GameOneItem[] newArray(int size) {
            return new GameOneItem[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cardText);
        dest.writeString(hint);
        dest.writeParcelable(cardImg, flags);
    }
}