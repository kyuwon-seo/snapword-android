package com.example.myapplication.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.JsonPass;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.connectserver.ConnectServer;
import com.example.myapplication.listview.ListViewAddSetAdapter;
import com.example.myapplication.listview.ListViewSetItem;
import com.example.myapplication.listview.ListViewUpdateRoomAdapter;
import com.example.myapplication.listview.RoomPersonItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.myapplication.MainActivity.homeBar;
import static com.example.myapplication.MainActivity.loginId;
import static com.example.myapplication.MainActivity.urlAddRoom;
import static com.example.myapplication.MainActivity.urlAddSet;
import static com.example.myapplication.MainActivity.urlSetAllList;
import static com.example.myapplication.MainActivity.urlSetAllReq;
import static com.example.myapplication.activity.GameTwoActivity.gameTwoActivity;

public class UpdateRoomActivity extends AppCompatActivity {

    private RecyclerView rv;
    private LinearLayoutManager llm;
    private String user_id;
    public int type;
    private int update_room_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_room);

        getSupportActionBar().show();
        homeBar = getSupportActionBar();
        homeBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_HOME);

        Intent intent = getIntent();
        type = intent.getIntExtra("type", 1);
        user_id = loginId;
        //세트클릭시 다이얼로그만 분할해서 띄우면 됨.
        //type 1 -> 방 만들기, 2 -> 세트변경

        ConnectServer connectServer = new ConnectServer();
        String SetAllList = connectServer.setAllReq(urlSetAllReq, user_id);

        ArrayList<ListViewSetItem> listSetItem = new ArrayList<ListViewSetItem>();
        //setList 뿌리기
        rv = findViewById(R.id.update_room_rv);
        llm = new LinearLayoutManager(getApplicationContext());
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);

        if(SetAllList != null) {
            try {
                JSONArray jarray = new JSONArray(SetAllList);

                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject jsonObject = jarray.getJSONObject(i);
                    ListViewSetItem addSetItem = new ListViewSetItem();
                    addSetItem.setSet_no(jsonObject.getInt("set_no"));
                    addSetItem.setSet_Name(jsonObject.getString("set_name"));
                    addSetItem.setOwner_id(jsonObject.getString("owner_id"));
                    addSetItem.setWord_cnt(jsonObject.getInt("word_cnt"));

                    listSetItem.add(addSetItem);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        final ListViewUpdateRoomAdapter adapter = new ListViewUpdateRoomAdapter(listSetItem, this); ///고쳐야됨
        rv.setAdapter(adapter);


    }//end oncreate

    public void makeRoomShow(int no) {
        //final Context con = context;
        final EditText edittext = new EditText(UpdateRoomActivity.this);
        final String set_no = Integer.toString(no);

        edittext.setHint("제목입력");

        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateRoomActivity.this);
        builder.setTitle("방 만들기");
        builder.setView(edittext);
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //방만들기 동작
                        ConnectServer connectServer = new ConnectServer();
                        String roomRes = connectServer.addRoom(urlAddRoom, set_no, edittext.getText().toString(), loginId);
                        Log.d("ROOM RES", "ROOM RES IS " + roomRes.trim());

                        update_room_no = Integer.parseInt(roomRes.trim());
                        makePerson(Integer.parseInt(set_no));
                        Intent intent = new Intent(UpdateRoomActivity.this, GameTwoActivity.class);
                        intent.putExtra("set_no", Integer.parseInt(set_no));
                        intent.putExtra("user_id", loginId);
                        intent.putExtra("room_person", 0);
                        intent.putExtra("room_name", edittext.getText().toString());
                        intent.putExtra("room_no", update_room_no);
                        intent.putExtra("room_pos", 0);
                        startActivity(intent);
                        finish();
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }//dialog end

    public void makePerson(int no) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Integer.toString(update_room_no));
        String key = myRef.child("person").getKey();

        RoomPersonItem item = new RoomPersonItem();
        item.setRoom_no(update_room_no);
        item.setPerson(1);
        item.setSet_no(no);
        item.setRoom_check(true);

        Map<String, Object> postValues = item.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/person/" + key, postValues);

        myRef.updateChildren(childUpdates);
    }

    public void updateRoomDialog(int no) {
        final int set_no = no;

        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateRoomActivity.this);

        builder.setMessage("세트가 변경되었습니다.");
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        gameTwoActivity.updateSet(set_no);
                        finish();
                    }
                });
        builder.show();
    }//dialog end

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return (super.onOptionsItemSelected(item));
    }

}
