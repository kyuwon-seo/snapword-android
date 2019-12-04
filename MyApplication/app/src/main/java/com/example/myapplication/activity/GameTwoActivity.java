package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.GameTwo;
import com.example.myapplication.R;
import com.example.myapplication.connectserver.ConnectServer;
import com.example.myapplication.firebase.DataRef;
import com.example.myapplication.fragment.FragmentPerson;
import com.example.myapplication.listview.GameTwoChatAdapter;
import com.example.myapplication.listview.GameTwoChatItem;
import com.example.myapplication.listview.RoomPersonItem;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.myapplication.MainActivity.homeBar;
import static com.example.myapplication.MainActivity.loginId;
import static com.example.myapplication.MainActivity.urlAddCheck;
import static com.example.myapplication.MainActivity.urlAddPerson;
import static com.example.myapplication.MainActivity.urlDelRoom;
import static com.example.myapplication.MainActivity.urlRoomUpdate;

public class GameTwoActivity extends AppCompatActivity {

    private DatabaseReference myRef;
    public RecyclerView rv;
    private LinearLayoutManager llm;
    public GameTwoChatAdapter adapter;
    private ArrayList<GameTwoChatItem> listChatItem;
    public static TextView roomPerson, gameTxt, q_cntTxt, q_countTxt;
    public static ImageView gameImg;
    EditText chatEdit;
    Button chatBtn;
    private int room_pos;
    public int room_no, set_no;
    private String room_name, king;
    public static int percnt;
    public static GameTwoActivity gameTwoActivity;
    public static CardView gameCard;
    public DataRef dataRef;
    public boolean gameflag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gametwo);
        gameTwoActivity = this;

        View homeActionBar = getLayoutInflater().inflate(R.layout.actionbar_gametwo, null);
        homeBar = getSupportActionBar();
        homeBar.setCustomView(homeActionBar);
        homeBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_CUSTOM);

        Intent intent = getIntent();
        room_no = intent.getIntExtra("room_no", 1); //db상의 no
        room_name = intent.getStringExtra("room_name");
        room_pos = intent.getIntExtra("room_pos", 1); //리스트 position
        set_no = intent.getIntExtra("set_no",1);
        king = intent.getStringExtra("user_id");
        percnt = (intent.getIntExtra("room_person", 1)+1);
        Log.d("GameTwoLOG", "intent is "+ room_no + " " + room_pos + " " +  set_no + " " + king);

        chatEdit = findViewById(R.id.chatEdit);
        chatBtn = findViewById(R.id.chatSend);
        gameTxt = findViewById(R.id.gameTwoTxt);
        gameImg = findViewById(R.id.gameTwoImg);
        q_cntTxt = findViewById(R.id.q_cnt);
        q_countTxt = findViewById(R.id.q_count);
        roomPerson = findViewById(R.id.personBar);
        roomPerson.setText(""+percnt);
        gameCard = findViewById(R.id.gameTwoCard);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference(Integer.toString(room_no));
        dataRef = new DataRef(myRef);

        addPerson(room_no);

        listChatItem = new ArrayList<GameTwoChatItem>();

        rv = findViewById(R.id.chat_rv);
        llm = new LinearLayoutManager(getApplicationContext());
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);

        adapter = new GameTwoChatAdapter(listChatItem);
        rv.setAdapter(adapter);

        dataRef.chatRef(this);

        //send버튼 클릭
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = chatEdit.getText().toString();
                if(msg != null) {
                    chatEdit.setText("");
                    GameTwoChatItem item = new GameTwoChatItem();
                    item.setUser_id(loginId);
                    item.setUser_txt(msg);
                    myRef.child("chat").push().setValue(item);
                }
            }
        });
    }
    public void update(RoomPersonItem item) {

        String key = myRef.child("person").getKey();

        Map<String, Object> postValues = item.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/person/" + key, postValues);

        myRef.updateChildren(childUpdates);
    }
    public void updateSet(int no){
        ConnectServer connectServer = new ConnectServer();
        connectServer.updateRoom(urlRoomUpdate, Integer.toString(room_no), Integer.toString(set_no));

        RoomPersonItem personItem = new RoomPersonItem();
        personItem.setRoom_no(room_no);
        personItem.setPerson(percnt);
        personItem.setSet_no(no);
        personItem.setRoom_check(true);
        update(personItem);
    }

    public void addPerson(int no) {
        ConnectServer connectServer = new ConnectServer();
        connectServer.addPerson(urlAddPerson, no, percnt);

        RoomPersonItem personItem = new RoomPersonItem();
        personItem.setRoom_no(room_no);
        personItem.setPerson(percnt);
        personItem.setSet_no(set_no);
        personItem.setRoom_check(true);
        update(personItem);

        GameTwoChatItem item = new GameTwoChatItem();
        item.setUser_id("["+loginId+"]");
        item.setUser_txt("님이 입장했습니다.");
        myRef.child("chat").push().setValue(item);
    }
    public void delPerson(int no){
        percnt = percnt-1;
        ConnectServer connectServer = new ConnectServer();
        connectServer.addPerson(urlAddPerson, no, percnt);

        RoomPersonItem personItem = new RoomPersonItem();
        personItem.setRoom_no(room_no);
        personItem.setPerson(percnt);
        personItem.setSet_no(set_no);
        personItem.setRoom_check(true);
        update(personItem);

        GameTwoChatItem item = new GameTwoChatItem();
        item.setUser_id("["+loginId+"]");
        item.setUser_txt("님이 퇴장했습니다.");
        myRef.child("chat").push().setValue(item);
    }
    public void gameTwoStart(int no){
        //db내용도 변경해야됨
        ConnectServer connectServer = new ConnectServer();
        connectServer.addCheck(urlAddCheck, no, "false");

        RoomPersonItem personItem = new RoomPersonItem();
        personItem.setRoom_no(room_no);
        personItem.setPerson(percnt);
        personItem.setSet_no(set_no);
        personItem.setRoom_check(false);
        update(personItem);
    }
    public void gameTwoEnd(int no){
        gameflag = false;
        ConnectServer connectServer = new ConnectServer();
        connectServer.addCheck(urlAddCheck, no, "true");

        RoomPersonItem personItem = new RoomPersonItem();
        personItem.setRoom_no(room_no);
        personItem.setPerson(percnt);
        personItem.setSet_no(set_no);
        personItem.setRoom_check(true);
        update(personItem);
    }
    //set-Array 이용 txtView에 게임보여주기 본격 게임실행 class 호출
    public void gameGO(){
        gameflag = true;
        GameTwo gameTwo = new GameTwo();
        gameTwo.start(gameTwoActivity, set_no, room_no);
        Log.d("GameTwo", "GameTwo.START");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(king.equals(loginId)) {
            if(gameflag==false) { //게임중 아니면~
                inflater.inflate(R.menu.king, menu);
            }

        }
        return true;
    }
    //메뉴아이템 - (게임시작, 세트변경)
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId()==R.id.changeSet) {
            Intent intent = new Intent(GameTwoActivity.this, UpdateRoomActivity.class);
            intent.putExtra("type", 2);
            startActivity(intent);
        }
        if(item.getItemId()==R.id.startGame) {
            gameTwoStart(room_no);
        }

        return (super.onOptionsItemSelected(item));
    }
    //뒤로가기 누르면 게임중? x
    @Override
    public void onBackPressed() {
        if(gameflag == true) { //게임중 상태
            Toast.makeText(getApplicationContext(), "게임중에는 나갈수 없어요", Toast.LENGTH_LONG).show();
            Log.d("GameTwoUSER", "게임중 퇴장불가 "+gameflag);
        }else{
            finish();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(percnt == 1){
            //percnt 0이므로 방삭제
            ConnectServer connectServer = new ConnectServer();
            connectServer.delRoom(urlDelRoom, Integer.toString(room_no));
            myRef.removeValue();
        }else {
            delPerson(room_no);
        }
        Log.d("GameTwoUSER", "User 퇴장 처리");
    }
}
