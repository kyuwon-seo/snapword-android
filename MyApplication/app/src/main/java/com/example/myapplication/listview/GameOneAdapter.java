package com.example.myapplication.listview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.myapplication.R;
import com.huxq17.swipecardsview.BaseCardAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GameOneAdapter extends BaseAdapter {
    private List<GameOneItem> OneList = new ArrayList<>();
    private int pos;

    public GameOneAdapter(){
    }
    @Override
    public Object getItem(int position) { return OneList.get(position); }
    @Override
    public long getItemId(int position) { return position; }
    @Override
    public int getCount(){
        return OneList.size();
    }
    public List<GameOneItem> getList() { return OneList; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d("TAG", ""+position);
        final GameOneItem gameOneItem = OneList.get(position);
        final Context context = parent.getContext();
        pos = position;

        final GameOneHolder holder;

        if(convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_cardone, parent, false);

            holder = new GameOneHolder();
            holder.cardText = convertView.findViewById(R.id.cardText);
            holder.cardImg = convertView.findViewById(R.id.cardImg);

            convertView.setTag(holder);
        }else{
            holder = (GameOneHolder) convertView.getTag();
        }
        holder.cardText.setText(gameOneItem.getCardText());
        if(gameOneItem.getCardImg()==null){
            holder.cardText.setVisibility(View.VISIBLE);
        }else{
            holder.cardImg.setImageBitmap(gameOneItem.getCardImg());
            holder.cardText.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public void addItem(String t, Bitmap bit, String h){
        GameOneItem item = new GameOneItem();
        item.setCardText(t);
        item.setCardImg(bit);
        item.setHint(h);

        OneList.add(item);
    }
    public void addList(List<GameOneItem>list){
        OneList = list;
        notifyDataSetChanged();
    }
}
