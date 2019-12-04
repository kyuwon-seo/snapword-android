package com.example.myapplication.activity;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.connectserver.ConnectServer;
import com.example.myapplication.listview.ListViewSetItem;
import com.example.myapplication.listview.ListViewSetUserAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.myapplication.MainActivity.homeBar;
import static com.example.myapplication.MainActivity.loginId;
import static com.example.myapplication.MainActivity.urlSetList;

public class SearchFoldActivity extends AppCompatActivity {

    private int folder_no;
    private String folder_name;
    private String owner_id;
    private RecyclerView rv;
    private LinearLayoutManager llm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_fold);

        homeBar = getSupportActionBar();
        homeBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_HOME);

        //| ActionBar.DISPLAY_SHOW_CUSTOM);
        //세트리스트 나열 > 세트클릭시 세트정보 엑티비티로 이동시키자
        //actionBar 점검 > 뒤로가기 버튼만 만들자 -> finish()
        Intent intent = getIntent();
        folder_no = intent.getIntExtra("folder_no", 1);
        folder_name = intent.getStringExtra("folder_name");
        owner_id = intent.getStringExtra("user_id");

        TextView user_name = findViewById(R.id.setFoldUser);
        TextView folderName = findViewById(R.id.setFoldName);
        user_name.setText(owner_id);
        folderName.setText(folder_name);

        ConnectServer connectServer = new ConnectServer();
        String setList = connectServer.setRequestPost(urlSetList, folder_no);

        ArrayList<ListViewSetItem> listSetItem = new ArrayList<ListViewSetItem>();

        //이제 folder_no + user_id 의 세트리스트 뿌리기 시작해야됨. cardview로 만들어보자. recycleview.
        rv = findViewById(R.id.main_rv);
        llm = new LinearLayoutManager(getApplicationContext());
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);

        try {
            JSONArray jarray = new JSONArray(setList);

            for(int i=0; i<jarray.length(); i++){
                JSONObject jsonObject = jarray.getJSONObject(i);
                ListViewSetItem setItem = new ListViewSetItem();
                setItem.setSet_no(jsonObject.getInt("set_no"));
                setItem.setSet_Name(jsonObject.getString("set_name"));
                setItem.setOwner_id(jsonObject.getString("owner_id"));
                setItem.setUser_id(jsonObject.getString("user_id"));
                setItem.setWord_cnt(jsonObject.getInt("word_cnt"));

                listSetItem.add(setItem);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }

        ListViewSetUserAdapter adapter = new ListViewSetUserAdapter(listSetItem, getApplication());
        rv.setAdapter(adapter);

    }
    //뒤로가기 버튼 클릭
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return (super.onOptionsItemSelected(item));
    }

}
