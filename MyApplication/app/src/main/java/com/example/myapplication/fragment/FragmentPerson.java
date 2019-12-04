package com.example.myapplication.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.activity.GameTwoActivity;
import com.example.myapplication.activity.UpdateRoomActivity;
import com.example.myapplication.connectserver.ConnectServer;
import com.example.myapplication.firebase.DataRef;
import com.example.myapplication.listview.ListViewRoomAdapter;
import com.example.myapplication.listview.ListViewRoomItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.myapplication.MainActivity.loginId;
import static com.example.myapplication.MainActivity.urlAddRoom;
import static com.example.myapplication.MainActivity.urlGetRoom;
import static com.example.myapplication.MainActivity.urlRoomList;

public class FragmentPerson extends Fragment {

    private RecyclerView rv;
    private LinearLayoutManager llm;
    public ListViewRoomAdapter adapter;
    private ArrayList<ListViewRoomItem> listRoomItem;
    private DataRef dataRef;
    private Button makeRoomBtn;
    public static FragmentPerson fragPerson;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_person, container, false);
        fragPerson = this;
        //액션바 없애기
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        ConnectServer connectServer = new ConnectServer();
        String roomList = connectServer.roomList(urlRoomList);
        listRoomItem = new ArrayList<ListViewRoomItem>();

        rv = view.findViewById(R.id.person_rv);
        llm = new LinearLayoutManager(view.getContext());
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        dataRef = new DataRef(myRef);
        if(roomList != null) {
            try {
                JSONArray jarray = new JSONArray(roomList);

                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject jsonObject = jarray.getJSONObject(i);
                    ListViewRoomItem roomItem = new ListViewRoomItem();
                    roomItem.setRoom_no(jsonObject.getInt("room_no"));
                    roomItem.setRoom_name(jsonObject.getString("room_name"));
                    roomItem.setSet_no(jsonObject.getInt("set_no"));
                    roomItem.setRoom_check(jsonObject.getBoolean("room_check"));
                    roomItem.setRoom_person(jsonObject.getInt("room_person"));
                    roomItem.setUser_id(jsonObject.getString("user_id"));
                    String mdate = new SimpleDateFormat("MM/dd/yyyy").format(jsonObject.get("room_date"));
                    roomItem.setRoom_date(mdate);
                    //각 room_no 마다 ref연결
                    dataRef.personRef(this, Integer.toString(roomItem.getRoom_no()));
                    listRoomItem.add(roomItem);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter = new ListViewRoomAdapter(listRoomItem, this);
        rv.setAdapter(adapter);

        makeRoomBtn = view.findViewById(R.id.makeRoomBtn);
        makeRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UpdateRoomActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });

        dataRef.addRoomRef();
        return view;
    }
    public void addRoomList(int no){
        if(adapter.isList(no)==false) {

            ConnectServer connectServer = new ConnectServer();
            String room = connectServer.getRoom(urlGetRoom, Integer.toString(no));
            try {
                JSONObject jsonObject = new JSONObject(room);

                ListViewRoomItem roomItem = new ListViewRoomItem();
                roomItem.setRoom_no(jsonObject.getInt("room_no"));
                roomItem.setRoom_name(jsonObject.getString("room_name"));
                roomItem.setSet_no(jsonObject.getInt("set_no"));
                roomItem.setRoom_check(jsonObject.getBoolean("room_check"));
                roomItem.setRoom_person(jsonObject.getInt("room_person"));
                roomItem.setUser_id(jsonObject.getString("user_id"));
                String mdate = new SimpleDateFormat("MM/dd/yyyy").format(jsonObject.get("room_date"));
                roomItem.setRoom_date(mdate);
                dataRef.personRef(this, Integer.toString(roomItem.getRoom_no()));

                adapter.addItem(roomItem);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Log.d("personAdd되었음", " nono room is check");
        }

    }
    public void removeRoom(int no){
        adapter.removeItem(no);
    }

    //해당 채팅방으로 이동하는 메소드
    //set_no, room_no,
    public void gameTwoGo(int r_no, String r_name, int r_pos, int s_no, String id, int r_p){
        Intent intent = new Intent(getContext(), GameTwoActivity.class);
        intent.putExtra("room_no", r_no);
        intent.putExtra("room_name", r_name);
        intent.putExtra("room_pos", r_pos);
        intent.putExtra("set_no", s_no);
        intent.putExtra("user_id", id);
        intent.putExtra("room_person", r_p);

        startActivity(intent);
    }

}