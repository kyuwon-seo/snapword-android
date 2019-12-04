package com.example.myapplication.listview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activity.SetInfoActivity;
import com.example.myapplication.fragment.FragmentHome;
import com.example.myapplication.fragment.FragmentSearchFolder;

import java.util.ArrayList;

public class ListViewFolderSearchAdapter extends RecyclerView.Adapter<ListViewFolderSearchAdapter.CustomViewHolder>{

    private ArrayList<ListViewFoldItem> listViewItemList = new ArrayList<ListViewFoldItem>() ;
    private FragmentSearchFolder.Command command;

    //생성자에서 데이터 리스트 객체를 전달받음
    public ListViewFolderSearchAdapter(ArrayList<ListViewFoldItem> list, FragmentSearchFolder.Command con) {
        listViewItemList = list;
        command = con;
    }

    //아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴
    @Override
    public ListViewFolderSearchAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.listview_folditem, parent, false);
        ListViewFolderSearchAdapter.CustomViewHolder viewHolder = new ListViewFolderSearchAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    //position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(ListViewFolderSearchAdapter.CustomViewHolder holder, int position) {
        holder.iconImageView.setImageDrawable(listViewItemList.get(position).getIcon());
        holder.titleTextView.setText(listViewItemList.get(position).getTitle());
        holder.descTextView.setText(listViewItemList.get(position).getDesc());
        holder.folder_no = listViewItemList.get(position).getFolder_no();
    }

    @Override
    public int getItemCount() {
        return this.listViewItemList.size();
    }

    //아이템 뷰를 저장하는 뷰홀더 클래스
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImageView;
        TextView titleTextView;
        TextView descTextView;
        int folder_no;

        public CustomViewHolder(View itemView) {
            super(itemView);
            //뷰 객체에 대한 참조
            iconImageView = itemView.findViewById(R.id.imageView1) ;
            titleTextView = itemView.findViewById(R.id.textView1) ;
            descTextView = itemView.findViewById(R.id.textView2) ;

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    command.searchFoldClicked((String)titleTextView.getText(), folder_no, (String)descTextView.getText());
                }
            });
            //롱클릭시 폴더리스트 삭제요청
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //mLongListener.onItemLongSelected(v, getAdapterPosition());
                    return false;
                }
            });
        }
    }

}
