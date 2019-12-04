package com.example.myapplication.listview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.media.Image;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.myapplication.R;
import com.example.myapplication.activity.CameraActivity;
import com.example.myapplication.activity.MakeSetActivity;

import java.io.File;
import java.util.ArrayList;

public class ListViewWordAdapter extends BaseAdapter {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    public ArrayList<ListViewWordItem> listViewWordItemList = new ArrayList<ListViewWordItem>() ;
    private ArrayList<ListViewWordItem> filteredItemList = listViewWordItemList;
    public static String totalWordA, totalWordB;
    MakeSetActivity m;

    // ListViewAdapter의 생성자
    public ListViewWordAdapter(MakeSetActivity ma) {
        this.m = ma;
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return filteredItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //final int pos = position;
        final int pos = listViewWordItemList.get(position).getNum();
        final Context context = parent.getContext();
        final WordViewHolder holder;

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            holder = new WordViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_worditem, parent, false);

            holder.editWordA = (EditText)convertView.findViewById(R.id.editWordA);
            holder.editWordB = (EditText)convertView.findViewById(R.id.editWordB);
            holder.wordImage = (ImageView)convertView.findViewById(R.id.wordImage);
            holder.editHint = (EditText)convertView.findViewById(R.id.editHint);
            holder.wordImageBtn = (ImageButton)convertView.findViewById(R.id.wordImageBtn);

            convertView.setTag(holder);
        }else{
            holder = (WordViewHolder)convertView.getTag();
        }
        holder.ref = position;

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        final EditText e = (EditText)convertView.findViewById(R.id.editWordA);
        final EditText e2 = (EditText)convertView.findViewById(R.id.editWordB);
        final ImageView iv = (ImageView)convertView.findViewById(R.id.wordImage);
        final ImageButton ib = (ImageButton)convertView.findViewById(R.id.wordImageBtn);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        //ListViewWordItem listViewItem = listViewWordItemList.get(position);
        final ListViewWordItem listViewItem = filteredItemList.get(position);

        holder.editWordA.setText(listViewItem.getWordA());
        holder.editWordB.setText(listViewItem.getWordB());
        holder.editHint.setText(listViewItem.getHint());
        holder.wordImage.setImageBitmap(listViewItem.getImage());

        totalWordA+=holder.editWordA.getText()+"#";
        totalWordB+=holder.editWordB.getText()+"#";

        // 아이템 내 각 위젯에 데이터 반영
        //e.setText(listViewItem.getWordA());
        //e2.setText(listViewItem.getWordB());

        holder.wordImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m.cameraON(holder.ref);
            }
        });

        holder.editWordA.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("TAG", "afterChanged: " +s);
                listViewWordItemList.get(holder.ref).setWordA(s.toString());
            }
        });

        holder.editWordB.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                listViewWordItemList.get(holder.ref).setWordB(s.toString());
            }
        });
        holder.editHint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                listViewWordItemList.get(holder.ref).setHint(s.toString());
            }
        });

        return convertView;
    }


    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return filteredItemList.get(position) ;
    }

    public Object getSetList() {return filteredItemList; }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String wordA, String wordB, int num, String hint){
      ListViewWordItem item = new ListViewWordItem();
      item.setWordA(wordA);
      item.setWordB(wordB);
      item.setHint(hint);
      item.setNum(num);

      listViewWordItemList.add(item);
    }
    public void setItem(int position, Bitmap bit, String path, String name){
        ListViewWordItem item = listViewWordItemList.get(position);
        item.setImage(bit);
        item.setImgPath(path);
        item.setImgName(name);
    }

    public void remove(int position){
        listViewWordItemList.remove(position);
    }

}