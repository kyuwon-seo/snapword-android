package com.example.myapplication.fragment;

import androidx.appcompat.app.ActionBar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.connectserver.ConnectServer;
import com.example.myapplication.listview.ListViewFoldItem;
import com.example.myapplication.R;
import com.example.myapplication.listview.ListViewFolderAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.myapplication.MainActivity.homeBar;
import static com.example.myapplication.MainActivity.loginId;
import static com.example.myapplication.MainActivity.urlFolder;

public class FragmentHome extends Fragment {

    private RecyclerView rv;
    private LinearLayoutManager llm;
    private ListViewFolderAdapter adapter;
    public Command homeInterface;
    ArrayList<ListViewFoldItem> listFoldItem;
    TextView setBtn;
    Button roomBtn;

    public interface Command {
        void onClicked(String title, int no, String id);
        void onSetAll(String id);
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        homeInterface = (Command) context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.pang);
        ((ImageView) view.findViewById(R.id.fragHOME)).setImageBitmap(bitmap);

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        View homeActionBar = getLayoutInflater().inflate(R.layout.actionbar_home, null);
        homeBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        homeBar.setCustomView(homeActionBar);
        homeBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        setHasOptionsMenu(true);

        //폴더 리스트 요청 json배열 저장하기
        ConnectServer connectServer = new ConnectServer();
        String folderList = connectServer.foldRequestPost(urlFolder, loginId);
        Log.d("foldList","foldList is " + folderList);

        //listview 대신 recycler로 변경!
        listFoldItem = new ArrayList<ListViewFoldItem>();
        rv = view.findViewById(R.id.folder_list);
        llm = new LinearLayoutManager(view.getContext());
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);
        if(folderList != null) {
            try {
                JSONArray jarray = new JSONArray(folderList);
                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject jsonObject = jarray.getJSONObject(i);
                    ListViewFoldItem foldItem = new ListViewFoldItem();
                    foldItem.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_folder_black_24dp));
                    foldItem.setTitle(jsonObject.getString("folder_name"));
                    foldItem.setFolder_no(jsonObject.getInt("folder_no"));
                    foldItem.setDesc(loginId);

                    listFoldItem.add(foldItem);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter = new ListViewFolderAdapter(listFoldItem, homeInterface);
        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);

        //세트로 보기 버튼 클릭시 모든세트 보여주는 프래그먼트로 ㄱ
        setBtn = view.findViewById(R.id.setshowbtn);
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("setBTN", "SetBtn CLick!!");
                homeInterface.onSetAll(loginId);
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.logout, menu);
    }
    //뒤로가기 버튼 클릭
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId()==R.id.logout) {
            SharedPreferences auto = getActivity().getSharedPreferences("auto", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = auto.edit();
            //editor.clear()는 auto에 들어있는 모든 정보를 기기에서 지웁니다.
            editor.clear();
            editor.commit();
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        return (super.onOptionsItemSelected(item));
    }
}