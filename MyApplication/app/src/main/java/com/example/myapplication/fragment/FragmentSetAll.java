package com.example.myapplication.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.R;
import com.example.myapplication.connectserver.ConnectServer;
import com.example.myapplication.listview.ListViewSetAdapter;
import com.example.myapplication.listview.ListViewSetAllAdapter;
import com.example.myapplication.listview.ListViewSetItem;
import com.example.myapplication.listview.SearchPageAdapter;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.myapplication.MainActivity.homeBar;
import static com.example.myapplication.MainActivity.loginId;
import static com.example.myapplication.MainActivity.urlSetAllList;
import static com.example.myapplication.MainActivity.urlSetAllReq;
import static com.example.myapplication.MainActivity.urlSetList;

public class FragmentSetAll extends Fragment {

    private RecyclerView rv;
    private LinearLayoutManager llm;
    public String user_id;

    public interface SetCommand {
        void onClicked(int no, String name, String cnt, String id, String user_id);
    }
    private SetCommand command;

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        command = (SetCommand) context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_setall, container, false);

        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.pang);
        ((ImageView) view.findViewById(R.id.fragSetAll)).setImageBitmap(bitmap);

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        homeBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        homeBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_HOME);
        setHasOptionsMenu(true);

        user_id = getArguments().getString("user_id");

        ConnectServer connectServer = new ConnectServer();
        String setList = connectServer.setAllReq(urlSetAllReq, user_id);

        ArrayList<ListViewSetItem> listSetItem = new ArrayList<ListViewSetItem>();

        rv = view.findViewById(R.id.main_rv);
        llm = new LinearLayoutManager(view.getContext());
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

        ListViewSetAllAdapter adapter = new ListViewSetAllAdapter(listSetItem, command);
        rv.setAdapter(adapter);

        return view;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (user_id == loginId) {
                command.onClicked(1, "", "homeback", "", "");
                Log.d("homehome", "home CLCIK!");
            }
        }
        return (super.onOptionsItemSelected(item));
    }

}