package com.example.myapplication.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class GameTwoChatAdapter extends RecyclerView.Adapter<GameTwoChatAdapter.CustomViewHolder> {

    private ArrayList<GameTwoChatItem> listViewItemList = new ArrayList<GameTwoChatItem>() ;

    //생성자에서 데이터 리스트 객체를 전달받음
    public GameTwoChatAdapter(ArrayList<GameTwoChatItem> list) {
        listViewItemList = list;
    }
    //아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴
    @Override
    public GameTwoChatAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.listview_chatitem, parent, false);
        GameTwoChatAdapter.CustomViewHolder viewHolder = new GameTwoChatAdapter.CustomViewHolder(view);

        return viewHolder;
    }
    //position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(GameTwoChatAdapter.CustomViewHolder holder, int position) {

        final int pos = position;

        holder.user_id.setText(listViewItemList.get(position).getUser_id());
        holder.user_txt.setText(listViewItemList.get(position).getUser_txt());

    }
    public ArrayList<GameTwoChatItem> getItemList() { return listViewItemList; }

    @Override
    public int getItemCount() { return this.listViewItemList.size(); }

    public int addItem(GameTwoChatItem item){
        listViewItemList.add(item);
        int pos = listViewItemList.size();
        notifyDataSetChanged();
        return pos;
    }

    //아이템 뷰를 저장하는 뷰홀더 클래스
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView user_id;
        TextView user_txt;

        public CustomViewHolder(final View itemView) {
            super(itemView);
            //뷰 객체에 대한 참조
            user_id = itemView.findViewById(R.id.chatuser_id);
            user_txt = itemView.findViewById(R.id.chatuser_txt);

        }
    }
}

