package com.example.myapplication.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.fragment.FragmentHome;

import java.util.ArrayList;

public class ListViewWrongAdapter extends RecyclerView.Adapter<ListViewWrongAdapter.CustomViewHolder>{

    private ArrayList<GameOneItem> CopyList = new ArrayList<>();
    private ArrayList<GameOneItem> CopyBackList = new ArrayList<>();
    //생성자에서 데이터 리스트 객체를 전달받음
    public ListViewWrongAdapter(ArrayList<GameOneItem> copy, ArrayList<GameOneItem> back) {
        CopyList = copy;
        CopyBackList = back;
    }

    //아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴
    @Override
    public ListViewWrongAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.listview_wrongitem, parent, false);
        ListViewWrongAdapter.CustomViewHolder viewHolder = new ListViewWrongAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    //position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(ListViewWrongAdapter.CustomViewHolder holder, int position) {
        holder.wordA.setText(CopyList.get(position).getCardText());
        holder.wordB.setText(CopyBackList.get(position).getCardText());
    }

    @Override
    public int getItemCount() {
        return this.CopyList.size();
    }

    //아이템 뷰를 저장하는 뷰홀더 클래스
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView wordA;
        TextView wordB;

        public CustomViewHolder(View itemView) {
            super(itemView);
            //뷰 객체에 대한 참조
            wordA = itemView.findViewById(R.id.wrongA) ;
            wordB = itemView.findViewById(R.id.wrongB) ;

        }
    }

}
