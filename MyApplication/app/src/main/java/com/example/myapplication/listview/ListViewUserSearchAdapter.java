package com.example.myapplication.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.fragment.FragmentSearchSet;
import com.example.myapplication.fragment.FragmentSearchUser;

import java.util.ArrayList;

import static com.example.myapplication.MainActivity.loginId;

public class ListViewUserSearchAdapter extends RecyclerView.Adapter<ListViewUserSearchAdapter.CustomViewHolder> {

    private ArrayList<ListViewUserItem> listViewItemList = new ArrayList<ListViewUserItem>();
    private FragmentSearchUser.Command command;

    //생성자에서 데이터 리스트 객체를 전달받음
    public ListViewUserSearchAdapter(ArrayList<ListViewUserItem> list, FragmentSearchUser.Command com) {
        listViewItemList = list;
        command = com;
    }

    //아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴
    @Override
    public ListViewUserSearchAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.listview_useritem, parent, false);
        ListViewUserSearchAdapter.CustomViewHolder viewHolder = new ListViewUserSearchAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    //position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(ListViewUserSearchAdapter.CustomViewHolder holder, int position) {

        holder.user_id.setText(listViewItemList.get(position).getUser_id());
    }

    @Override
    public int getItemCount() {
        return this.listViewItemList.size();
    }

    //아이템 뷰를 저장하는 뷰홀더 클래스
    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView user_id;

        public CustomViewHolder(View itemView) {
            super(itemView);
            //뷰 객체에 대한 참조
            user_id = itemView.findViewById(R.id.search_userId);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    command.searchUserClicked((String) user_id.getText());
                }
            });
            ;
        }
    }
}
