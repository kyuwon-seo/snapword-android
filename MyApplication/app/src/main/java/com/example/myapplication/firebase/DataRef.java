package com.example.myapplication.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.MainActivity;
import com.example.myapplication.activity.GameTwoActivity;
import com.example.myapplication.fragment.FragmentPerson;
import com.example.myapplication.listview.GameTwoChatItem;
import com.example.myapplication.listview.ListViewRoomItem;
import com.example.myapplication.listview.RoomPersonItem;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.myapplication.GameTwo.gameTwo;
import static com.example.myapplication.activity.GameTwoActivity.gameTwoActivity;
import static com.example.myapplication.activity.GameTwoActivity.percnt;
import static com.example.myapplication.activity.GameTwoActivity.roomPerson;
import static com.example.myapplication.fragment.FragmentPerson.fragPerson;

public class DataRef {

    DatabaseReference database;

    public DataRef(DatabaseReference base){
        this.database = base;
    }

    public DatabaseReference chatRef(GameTwoActivity act){
        DatabaseReference chatRef = database.child("chat");
        final GameTwoActivity activity = act;

        chatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                GameTwoChatItem item = dataSnapshot.getValue(GameTwoChatItem.class);
                int po = activity.adapter.addItem(item);
                activity.rv.smoothScrollToPosition(po-1);
                activity.adapter.notifyDataSetChanged();

                if((gameTwo != null) && (gameTwo.Q_res != null)){
                    if(item.getUser_txt().equals(gameTwo.Q_res)){   //정답이면
                        gameTwo.res_id = item.getUser_id();
                        //즉석에서 정답데이터 색깔로 구분하게 만들어야됨.(미완성)
                    }
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        return chatRef;
    }

    public DatabaseReference personRef(FragmentPerson fragPerson, String no){
        DatabaseReference personRef = FirebaseDatabase.getInstance().getReference(no+"/person");
        final FragmentPerson fragmentPerson = fragPerson;
        final int rno = Integer.parseInt(no);

        personRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //게임시작되어 false상태되면 gametwo 같은방 인원 게임 전체적으로 시작해줘야됨.
                RoomPersonItem c = dataSnapshot.getValue(RoomPersonItem.class);
                if(gameTwoActivity==null){ //채팅방에 안 들어온 사람은 null
                    Log.d("gameTwoActivity", "gameTwoActivity is null!!");
                }else {
                    roomPerson.setText(""+c.getPerson()); //채팅방 인원표시
                    if( (c.isRoom_check() == false) && (gameTwoActivity.room_no == c.getRoom_no()) &&
                            (gameTwoActivity.gameflag == false)){ //게임중 상태 + 동일채팅방접속자면
                        gameTwoActivity.gameGO();
                    }
                    if( (c.getSet_no() != gameTwoActivity.set_no) ){ //세트변경되면 적용해라
                        gameTwoActivity.set_no = c.getSet_no();
                    }
                }
                fragmentPerson.adapter.setPerson(rno, c.getPerson(), c.isRoom_check()); //roomList 업데이트
                fragmentPerson.adapter.setUpdate(rno, c.getSet_no());

                Log.d("personCHANGED", "room is "+c.getRoom_no() +
                        " chk is "+ c.isRoom_check() + " VALUE is "+ c.getPerson());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return personRef;
    }

    public DatabaseReference addRoomRef(){
        DatabaseReference addRoomRef = database;

        addRoomRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                int no = Integer.parseInt(dataSnapshot.getKey());
                fragPerson.addRoomList(no);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                int no = Integer.parseInt(dataSnapshot.getKey());
                fragPerson.removeRoom(no);
                Log.d("personRemove되었음", "notify datachanged 했음");
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        return addRoomRef;
    }
}
