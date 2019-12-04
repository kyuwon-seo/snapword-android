package com.example.myapplication.listview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListViewSetWordAdapter extends RecyclerView.Adapter<ListViewSetWordAdapter.CustomViewHolder>{

    private ArrayList<ListViewSetWordItem> listViewItemList = new ArrayList<ListViewSetWordItem>() ;

    //생성자에서 데이터 리스트 객체를 전달받음
    public ListViewSetWordAdapter(ArrayList<ListViewSetWordItem> list) {
        listViewItemList = list;
    }

    //아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴
    @Override
    public ListViewSetWordAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.listview_setworditem, parent, false);
        ListViewSetWordAdapter.CustomViewHolder viewHolder = new ListViewSetWordAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    //position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(ListViewSetWordAdapter.CustomViewHolder holder, int position) {

        holder.word_A_TextView.setText(listViewItemList.get(position).getWordA());
        holder.word_B_TextView.setText(listViewItemList.get(position).getWordB());
        if(listViewItemList.get(position).getImgBit() != null) {
            holder.img.setImageBitmap(listViewItemList.get(position).getImgBit());
            holder.word_A_TextView.setVisibility(View.INVISIBLE);
        }else{
            holder.word_A_TextView.setVisibility(View.VISIBLE);
        }
    }
    public void setItemImg(int pos, Bitmap bit){
        if( (bit != null) && (listViewItemList.get(pos).getImgBit()==null) ) {
            listViewItemList.get(pos).setImgBit(bit);
            notifyDataSetChanged();
        }
    }
    /*public void ImgRecycle(){
        for(int i=0; i<listViewItemList.size(); i++){
            if (listViewItemList.get(i).getImgBit() != null) {
                listViewItemList.get(i).getImgBit().recycle();
            }
            Log.d("삭제했음", "삭제완료");
        }
    }*/
    @Override
    public int getItemCount() {
        return this.listViewItemList.size();
    }

    public ArrayList<ListViewSetWordItem> getListViewItemList() {
        return listViewItemList;
    }

    //아이템 뷰를 저장하는 뷰홀더 클래스
    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView word_A_TextView;
        TextView word_B_TextView;
        ImageView img;

        public CustomViewHolder(View itemView) {
            super(itemView);
            //뷰 객체에 대한 참조
            img = itemView.findViewById(R.id.setimg);
            word_A_TextView = itemView.findViewById(R.id.setword_A) ;
            word_B_TextView = itemView.findViewById(R.id.setword_B) ;
        }
    }

}
