package com.example.myapplication.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activity.UpdateRoomActivity;

import java.util.ArrayList;

public class ListViewUpdateRoomAdapter extends RecyclerView.Adapter<ListViewUpdateRoomAdapter.CustomViewHolder>{

    private ArrayList<ListViewSetItem> listViewItemList = new ArrayList<ListViewSetItem>() ;
    private UpdateRoomActivity activity;

    //생성자에서 데이터 리스트 객체를 전달받음
    public ListViewUpdateRoomAdapter(ArrayList<ListViewSetItem> list, UpdateRoomActivity act) {
        listViewItemList = list;
        activity = act;
    }
    //아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴
    @Override
    public ListViewUpdateRoomAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.lisetview_setitem, parent, false);
        ListViewUpdateRoomAdapter.CustomViewHolder viewHolder = new ListViewUpdateRoomAdapter.CustomViewHolder(view);

        return viewHolder;
    }
    //position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(ListViewUpdateRoomAdapter.CustomViewHolder holder, int position) {

        final int pos = position;

        holder.set_name.setText(listViewItemList.get(position).getSet_Name());
        holder.word_cnt.setText(listViewItemList.get(position).getWord_cnt()+"단어");
        holder.owner_id.setText(listViewItemList.get(position).getOwner_id());
        holder.set_no=listViewItemList.get(position).getSet_no();

    }
    public ArrayList<ListViewSetItem> getItemList() { return listViewItemList; }

    @Override
    public int getItemCount() { return this.listViewItemList.size(); }

    //아이템 뷰를 저장하는 뷰홀더 클래스
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView set_name;
        TextView word_cnt;
        TextView owner_id;

        int set_no;

        public CustomViewHolder(final View itemView) {
            super(itemView);
            //뷰 객체에 대한 참조
            set_name = itemView.findViewById(R.id.set_name);
            word_cnt = itemView.findViewById(R.id.word_cnt);
            owner_id = itemView.findViewById(R.id.owner_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(activity.type==1){ //방 만들기
                        activity.makeRoomShow(set_no);
                    }
                    if(activity.type==2){ //세트 변경
                        activity.updateRoomDialog(set_no);
                    }
                }
            });
        }
    }

}
