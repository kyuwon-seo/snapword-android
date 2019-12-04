package com.example.myapplication.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.connectserver.ConnectServer;
import com.example.myapplication.listview.ListViewSetWordAdapter;
import com.example.myapplication.listview.ListViewSetWordItem;
import com.example.myapplication.listview.RoomPersonItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.myapplication.MainActivity.homeBar;
import static com.example.myapplication.MainActivity.loginId;
import static com.example.myapplication.MainActivity.urlAddRoom;
import static com.example.myapplication.MainActivity.urlDelSet;
import static com.example.myapplication.MainActivity.urlFoldMake;
import static com.example.myapplication.MainActivity.urlPicasso;
import static com.example.myapplication.MainActivity.urlSetWordList;

public class SetInfoActivity extends AppCompatActivity {

    private RecyclerView rv;
    private LinearLayoutManager llm;
    private ListViewSetWordAdapter adapter;
    private CardView game_one, game_two;
    ArrayList<ListViewSetWordItem> listSetWordItem=null;
    TextView setName, wordcnt, setOwner_id;
    public String user_id, set_no, set_name, owner_id, word_cnt;
    private Bitmap bit = null;
    private int no, room_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_info);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        getSupportActionBar().show();
        homeBar = getSupportActionBar();
        homeBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_HOME);

        Intent intent = getIntent();

        set_no = Integer.toString(intent.getIntExtra("set_no", 1));
        //set_no = intent.getStringExtra("set_no");
        word_cnt = intent.getStringExtra("word_cnt");
        set_name = intent.getStringExtra("set_name");
        owner_id = intent.getStringExtra("owner_id");
        user_id = intent.getStringExtra("user_id");
        Log.d("SetInfoActivity", "set_no is : " + set_no);

        wordcnt = findViewById(R.id.word_cnt);
        setName = findViewById(R.id.set_name);
        wordcnt.setText(word_cnt+"  |");
        setName.setText(set_name);
        setOwner_id = findViewById(R.id.owner_id);
        setOwner_id.setText(owner_id);

        final ConnectServer connectServer = new ConnectServer();
        String wordList = connectServer.setWordRequest(urlSetWordList, set_no);
        Log.d("SetInfoActivity", wordList);
        listSetWordItem = new ArrayList<ListViewSetWordItem>();

        rv = findViewById(R.id.setWord_list);
        llm = new LinearLayoutManager(getApplicationContext());
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);
        if (wordList != null) {
            try {
                JSONArray jarray = new JSONArray(wordList);

                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject jsonObject = jarray.getJSONObject(i);
                    final ListViewSetWordItem wordItem = new ListViewSetWordItem();

                    wordItem.setWordA(jsonObject.getString("word_a"));
                    wordItem.setWordB(jsonObject.getString("word_b"));
                    wordItem.setHint(jsonObject.getString("hint"));
                    if (!jsonObject.getString("img_name").equals("null")) {
                        pica(jsonObject.getString("img_name"));
                        wordItem.setImgBit(bit);
                    }
                    listSetWordItem.add(wordItem);

                    /*if (!jsonObject.getString("img_name").equals("null")) {
                        Log.d("Img_name", "Img_name is " + jsonObject.getString("img_name"));
                        pica(i, jsonObject.getString("img_name"));
                    }*/
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter = new ListViewSetWordAdapter(listSetWordItem);
        //adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);

        game_one = findViewById(R.id.game_one);
        game_two = findViewById(R.id.game_two);
        game_one.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("gameOne", "GAME ONE CLICK : " + set_no);
                ArrayList<ListViewSetWordItem> itemlist = new ArrayList<ListViewSetWordItem>();
                itemlist = adapter.getListViewItemList();
                Log.d("SetInfo", "itemBit is " + itemlist.get(0).getImgBit());

                Intent intent = new Intent(getApplicationContext(), GameOneActivity.class);
                intent.putExtra("set_no", set_no);
                intent.putExtra("itemlist", itemlist);
                startActivity(intent);
            }
        });
        game_two.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //room_name 적는 다이얼로그생성
                makeRoomShow(SetInfoActivity.this);
            }
        });

    }

    public void makeRoomShow(Context context) {
        final Context con = context;
        final EditText edittext = new EditText(con);
        edittext.setHint("제목입력");

        AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setTitle("방 만들기");
        builder.setView(edittext);
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //방만들기 동작
                        ConnectServer connectServer = new ConnectServer();
                        String roomRes = connectServer.addRoom(urlAddRoom, set_no, edittext.getText().toString(), loginId);
                        Log.d("ROOM RES", "ROOM RES IS " + roomRes.trim());
                        room_no = Integer.parseInt(roomRes.trim());
                        makePerson(Integer.parseInt(set_no));
                        Intent intent = new Intent(getApplicationContext(), GameTwoActivity.class);
                        intent.putExtra("set_no", Integer.parseInt(set_no));
                        intent.putExtra("user_id", loginId);
                        intent.putExtra("room_person", 0);
                        intent.putExtra("room_name", edittext.getText().toString());
                        intent.putExtra("room_no", room_no);
                        intent.putExtra("room_pos", 0);
                        ((MainActivity)MainActivity.mContext).refreshPerson();
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
    }

    public void makePerson(int no) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Integer.toString(room_no));
        String key = myRef.child("person").getKey();

        RoomPersonItem item = new RoomPersonItem();
        item.setRoom_no(room_no);
        item.setPerson(1);
        item.setSet_no(no);
        item.setRoom_check(true);

        Map<String, Object> postValues = item.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/person/" + key, postValues);

        myRef.updateChildren(childUpdates);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        Log.d("SetInfoActivity", "user_id is " + user_id);
        if (user_id.equals(loginId)) {
            inflater.inflate(R.menu.set, menu);
        } else {
            inflater.inflate(R.menu.set_other, menu);
        }
        return true;
    }

    //액션바 메뉴
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        /*if (item.getItemId() == R.id.add_to_folder) {

        }
        if (item.getItemId() == R.id.set_update) {

        }*/

        if (item.getItemId() == R.id.set_del) {
            ConnectServer connectServer = new ConnectServer();
            connectServer.delSet(urlDelSet, set_no);

            AlertDialog.Builder builder = new AlertDialog.Builder(SetInfoActivity.this);
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    ((MainActivity)MainActivity.mContext).refreshHome(); //홈으로 이동
                    //dialog.dismiss();     //닫기
                }
            });
            builder.setMessage("삭제 완료!");
            builder.show();
        }
        if (item.getItemId() == R.id.get_set) {//세트 가져오기
            Intent intent = new Intent(getApplicationContext(), MakeSetActivity.class);
            ArrayList<ListViewSetWordItem> itemlist = new ArrayList<ListViewSetWordItem>();
            itemlist = adapter.getListViewItemList();
            intent.putExtra("set_no", set_no);
            intent.putExtra("set_name", set_name);
            intent.putExtra("itemlist", itemlist);
            startActivity(intent);
            finish();
        }

        return (super.onOptionsItemSelected(item));
    }
    public void pica(String name){
        Picasso.with(getApplicationContext())
                .load(urlPicasso + name)
                .resize(250, 260).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                bit = bitmap;
            }
            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
    }
    /*public void pica(int o, String name) {
        no = o;
        Picasso.with(getApplicationContext())
                .load(urlPicasso + name)
                .resize(200, 200).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                adapter.setItemImg(no, bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //adapter.ImgRecycle();
    }

}