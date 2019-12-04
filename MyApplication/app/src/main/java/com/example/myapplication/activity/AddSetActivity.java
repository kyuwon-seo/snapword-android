package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.JsonPass;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.connectserver.ConnectServer;
import com.example.myapplication.listview.ListViewAddSetAdapter;
import com.example.myapplication.listview.ListViewSetAdapter;
import com.example.myapplication.listview.ListViewSetItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.myapplication.MainActivity.homeBar;
import static com.example.myapplication.MainActivity.loginId;
import static com.example.myapplication.MainActivity.urlAddSet;
import static com.example.myapplication.MainActivity.urlSetAllList;
import static com.example.myapplication.MainActivity.urlSetList;

public class AddSetActivity extends AppCompatActivity {

    ImageButton AddSetBtn;
    private int folder_no;
    private RecyclerView rv;
    private LinearLayoutManager llm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_set);

        View homeActionBar = getLayoutInflater().inflate(R.layout.actionbar_makeset, null);
        homeBar = getSupportActionBar();
        homeBar.setCustomView(homeActionBar);
        homeBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_HOME |
                ActionBar.DISPLAY_SHOW_CUSTOM);

        String user_id = loginId;
        Intent intent = getIntent();
        folder_no = intent.getIntExtra("folder_no", 1);
        Log.d("AddSetActivity", "folder_no is " + folder_no);

        ConnectServer connectServer = new ConnectServer();
        String SetAllList = connectServer.setAllRequest(urlSetAllList, user_id);

        ArrayList<ListViewSetItem> listSetItem = new ArrayList<ListViewSetItem>();
        //setList 뿌리기
        rv = findViewById(R.id.add_set_rv);
        llm = new LinearLayoutManager(getApplicationContext());
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);

        try {
            JSONArray jarray = new JSONArray(SetAllList);

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject jsonObject = jarray.getJSONObject(i);
                ListViewSetItem addSetItem = new ListViewSetItem();
                addSetItem.setSet_no(jsonObject.getInt("set_no"));
                addSetItem.setSet_Name(jsonObject.getString("set_name"));
                addSetItem.setOwner_id(jsonObject.getString("owner_id"));
                addSetItem.setSet_Click(0);
                addSetItem.setWord_cnt(jsonObject.getInt("word_cnt"));

                listSetItem.add(addSetItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ListViewAddSetAdapter adapter = new ListViewAddSetAdapter(listSetItem);
        rv.setAdapter(adapter);

        AddSetBtn = findViewById(R.id.makeSetBtn);
        AddSetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //세트 리스트받아서 인설트
                ArrayList<ListViewSetItem> itemList = adapter.getItemList();
                ArrayList<ListViewSetItem> clickList = new ArrayList<ListViewSetItem>();
                Log.d("ItemList", "ListCount is " + itemList.size());
                int cnt=0;
                for (int i = 0; i < itemList.size(); i++) {

                    if (itemList.get(i).getSet_Click() == 1) {  //사실 set_no만 알면됨 ㅇㅈ?
                        clickList.add(itemList.get(i));
                        cnt++;
                    }
                }
                Log.d("ItemList", "ListCheck is " + cnt);
                JsonPass j = new JsonPass();
                JSONObject jsonSet = j.jsonAddSetPass(clickList, folder_no);
                ConnectServer connectServer = new ConnectServer();
                connectServer.addSetRequest(urlAddSet, jsonSet);

                ((MainActivity)MainActivity.mContext).refreshHome();
                finish();

            }
        });

    }//end oncreate

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
                finish();
        }
        return (super.onOptionsItemSelected(item));
    }
}
