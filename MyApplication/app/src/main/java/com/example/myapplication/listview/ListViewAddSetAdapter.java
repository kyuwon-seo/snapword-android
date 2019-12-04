package com.example.myapplication.listview;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.fragment.FragmentSetlist;

import java.util.ArrayList;

import static com.example.myapplication.MainActivity.loginId;

public class ListViewAddSetAdapter extends RecyclerView.Adapter<ListViewAddSetAdapter.CustomViewHolder>{

    private ArrayList<ListViewSetItem> listViewItemList = new ArrayList<ListViewSetItem>() ;
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);

    //생성자에서 데이터 리스트 객체를 전달받음
    public ListViewAddSetAdapter(ArrayList<ListViewSetItem> list) {
        listViewItemList = list;
    }
    //아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴
    @Override
    public ListViewAddSetAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.lisetview_setitem, parent, false);
        ListViewAddSetAdapter.CustomViewHolder viewHolder = new ListViewAddSetAdapter.CustomViewHolder(view);

        return viewHolder;
    }
    //position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(ListViewAddSetAdapter.CustomViewHolder holder, int position) {

        final int pos = position;

        holder.set_name.setText(listViewItemList.get(position).getSet_Name());
        holder.word_cnt.setText(listViewItemList.get(position).getWord_cnt()+"단어");
        holder.owner_id.setText(listViewItemList.get(position).getOwner_id());
        holder.set_no=listViewItemList.get(position).getSet_no();
        holder.set_Click=listViewItemList.get(position).getSet_Click();

        if(mSelectedItems.get(position, false)){
            holder.itemView.setBackgroundResource(R.color.yellow);
        }else {
            holder.itemView.setBackgroundColor(000000);
        }

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
        int set_Click;

        public CustomViewHolder(final View itemView) {
            super(itemView);
            //뷰 객체에 대한 참조
            set_name = itemView.findViewById(R.id.set_name);
            word_cnt = itemView.findViewById(R.id.word_cnt);
            owner_id = itemView.findViewById(R.id.owner_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(mSelectedItems.get(position, false)) {
                        mSelectedItems.put(position, false);
                        listViewItemList.get(position).setSet_Click(0);
                        v.setBackgroundColor(000000);
                    }else {
                        mSelectedItems.put(position, true);
                        listViewItemList.get(position).setSet_Click(1);
                        v.setBackgroundResource(R.color.yellow);
                    }
                }
            });
        }
    }
}

