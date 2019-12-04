package com.example.myapplication.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.fragment.FragmentPerson;

import java.util.ArrayList;
import java.util.Date;

public class ListViewRoomAdapter extends RecyclerView.Adapter<ListViewRoomAdapter.CustomViewHolder> {

    private ArrayList<ListViewRoomItem> listViewItemList = new ArrayList<ListViewRoomItem>();
    private FragmentPerson fp;

    //생성자에서 데이터 리스트 객체를 전달받음
    public ListViewRoomAdapter(ArrayList<ListViewRoomItem> list, FragmentPerson p) {

        listViewItemList = list;
        fp = p;
    }

    public boolean isList(int no){
        boolean isRoom=false;
        for(int i=0; i<listViewItemList.size(); i++){
            if(listViewItemList.get(i).getRoom_no() == no){
                isRoom = true;
            }
        }
        return isRoom;
    }

    //아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴
    @Override
    public ListViewRoomAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.listview_roomitem, parent, false);
        ListViewRoomAdapter.CustomViewHolder viewHolder = new ListViewRoomAdapter.CustomViewHolder(view);

        return viewHolder;
    }

    //position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(ListViewRoomAdapter.CustomViewHolder holder, int position) {

        final int pos = position;
        holder.room_pos = position;
        //여기서 룸layout에 표시될것은? room_name, date, person
        holder.room_name.setText(listViewItemList.get(position).getRoom_name());
        holder.room_date.setText(listViewItemList.get(position).getRoom_date().toString());
        holder.room_person.setText(listViewItemList.get(position).getRoom_person() + "/3명");
        holder.room_check = listViewItemList.get(position).isRoom_check();
        holder.room_no = listViewItemList.get(position).getRoom_no();
        holder.set_no = listViewItemList.get(position).getSet_no();
        holder.king = listViewItemList.get(position).getUser_id();
        holder.room_max = 3;
        holder.person=listViewItemList.get(position).getRoom_person();

        if(holder.room_check == false) {
            holder.room_insert.setText("게임중");
            holder.room_insert.setEnabled(false);
        }else{
            holder.room_insert.setText("입장하기 >");
            holder.room_insert.setEnabled(true);
        }
    }
    @Override
    public int getItemCount() { return this.listViewItemList.size(); }

    public void addItem(ListViewRoomItem item) {
        if(item.getRoom_person()==0) item.setRoom_person(1);

        listViewItemList.add(item);
        notifyDataSetChanged();
    }
    public void removeItem(int no){
        for(int i=0; i<listViewItemList.size(); i++){
            if(listViewItemList.get(i).getRoom_no() == no){
                listViewItemList.remove(i);
            }
        }
        notifyDataSetChanged();
    }
    public void setPerson(int no, int person, boolean chk){
        //listViewItemList.get(pos).setRoom_person(person);
        for(int i=0; i<listViewItemList.size(); i++) {
            if(listViewItemList.get(i).getRoom_no() == no) {
                listViewItemList.get(i).setRoom_person(person);
                listViewItemList.get(i).setRoom_check(chk);
                notifyDataSetChanged();
            }
        }
    }
    public void setUpdate(int rno, int no){
        for(int i=0; i<listViewItemList.size(); i++){
            if((listViewItemList.get(i).getRoom_no() == rno) && (listViewItemList.get(i).getSet_no() != no)){
                listViewItemList.get(i).setSet_no(no);
                notifyDataSetChanged();
            }
        }
    }

    public int getPerson(int pos){
        return listViewItemList.get(pos).getRoom_person();
    }

    //아이템 뷰를 저장하는 뷰홀더 클래스
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView room_name;
        TextView room_date;
        TextView room_person;
        Button room_insert;

        int room_no;
        int set_no;
        boolean room_check; //채팅방 입장가능 여부( 풀방? 게임중?)
        int person;//채팅방 인원
        int room_max;
        int room_pos;
        String king;

        public CustomViewHolder(final View itemView) {
            super(itemView);
            //뷰 객체에 대한 참조
            room_name = itemView.findViewById(R.id.room_name);
            room_date = itemView.findViewById(R.id.room_date);
            room_person = itemView.findViewById(R.id.room_person);
            room_insert = itemView.findViewById(R.id.room_insert);

            room_insert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(person < room_max) {
                        fp.gameTwoGo(room_no, room_name.getText().toString(), room_pos, set_no, king, person);
                    }else{
                        // 인원꽉찼다고 보고.
                    }
                }//onClick
            });//setClick
        }
    }
}

