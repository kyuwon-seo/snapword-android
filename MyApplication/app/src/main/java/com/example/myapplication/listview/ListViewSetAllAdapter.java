package com.example.myapplication.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.fragment.FragmentSetAll;
import com.example.myapplication.fragment.FragmentSetlist;

import java.util.ArrayList;

public class ListViewSetAllAdapter extends RecyclerView.Adapter<ListViewSetAllAdapter.CustomViewHolder>{

    private ArrayList<ListViewSetItem> listViewItemList = new ArrayList<ListViewSetItem>() ;
    private FragmentSetAll.SetCommand command;

    //생성자에서 데이터 리스트 객체를 전달받음
    public ListViewSetAllAdapter(ArrayList<ListViewSetItem> list, FragmentSetAll.SetCommand com) {
        listViewItemList = list;
        command = com;
    }
    //아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴
    @Override
    public ListViewSetAllAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.lisetview_setitem, parent, false);
        ListViewSetAllAdapter.CustomViewHolder viewHolder = new ListViewSetAllAdapter.CustomViewHolder(view);
        return viewHolder;
    }
    //position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(ListViewSetAllAdapter.CustomViewHolder holder, int position) {

        holder.set_name.setText(listViewItemList.get(position).getSet_Name());
        holder.wordcnt.setText(listViewItemList.get(position).getWord_cnt()+"단어");
        holder.owner_id.setText(listViewItemList.get(position).getOwner_id());
        holder.set_no=listViewItemList.get(position).getSet_no();
        holder.user_id=listViewItemList.get(position).getUser_id();
    }

    @Override
    public int getItemCount() {
        return this.listViewItemList.size();
    }

    //아이템 뷰를 저장하는 뷰홀더 클래스
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView set_name;
        TextView wordcnt;
        TextView owner_id;
        String user_id;
        int set_no;

        public CustomViewHolder(View itemView) {
            super(itemView);
            //뷰 객체에 대한 참조
            set_name = itemView.findViewById(R.id.set_name);
            wordcnt = itemView.findViewById(R.id.word_cnt);
            owner_id = itemView.findViewById(R.id.owner_id);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){

                    command.onClicked(set_no, (String)set_name.getText(),
                            (String)wordcnt.getText(), (String)owner_id.getText(), user_id);
                }
            });

        }
    }
}
