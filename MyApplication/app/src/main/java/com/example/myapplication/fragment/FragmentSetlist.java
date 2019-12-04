package com.example.myapplication.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.activity.AddSetActivity;
import com.example.myapplication.connectserver.ConnectServer;
import com.example.myapplication.listview.ListViewSetAdapter;
import com.example.myapplication.listview.ListViewSetItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.myapplication.MainActivity.homeBar;
import static com.example.myapplication.MainActivity.loginId;
import static com.example.myapplication.MainActivity.urlDelFolder;
import static com.example.myapplication.MainActivity.urlDelRoom;
import static com.example.myapplication.MainActivity.urlDelSet;
import static com.example.myapplication.MainActivity.urlSetList;

public class FragmentSetlist extends Fragment{

    private RecyclerView rv;
    private LinearLayoutManager llm;
    private int i=0;
    public String user_id;

    public interface SetCommand {
        void onClicked(int no, String name, String cnt, String id, String user_id);
    }
    private SetCommand command;
    private int folder_no;

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        command = (SetCommand) context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_setlist, container, false);

        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.pang);
        ((ImageView) view.findViewById(R.id.fragSET)).setImageBitmap(bitmap);

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        homeBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        homeBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_HOME);
                //| ActionBar.DISPLAY_SHOW_CUSTOM);
        setHasOptionsMenu(true);

        folder_no = getArguments().getInt("folder_no"); //클릭된 폴더정보
        user_id = getArguments().getString("user_id");
        String folderName = getArguments().getString("folder_name");

        TextView user_name = view.findViewById(R.id.setFoldUser);
        TextView folder_name = view.findViewById(R.id.setFoldName);
        user_name.setText(user_id);
        folder_name.setText(folderName);

        ConnectServer connectServer = new ConnectServer();
        String setList = connectServer.setRequestPost(urlSetList, folder_no);

        ArrayList<ListViewSetItem> listSetItem = new ArrayList<ListViewSetItem>();

        //이제 folder_no + user_id 의 세트리스트 뿌리기 시작해야됨. cardview로 만들어보자. recycleview.
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

        ListViewSetAdapter adapter = new ListViewSetAdapter(listSetItem, command);
        rv.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(user_id == loginId) {
            inflater.inflate(R.menu.folder, menu);
        }
    }
    //뒤로가기 버튼 클릭
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==android.R.id.home){
            if(user_id == loginId) {
                command.onClicked(1, "", "homeback", "", "");
                Log.d("homehome", "home CLCIK!");
            }
        }
        if(item.getItemId()==R.id.add_set){
            Intent intent = new Intent(getContext(), AddSetActivity.class);
            intent.putExtra("folder_no", folder_no);
            startActivity(intent);
        }
        /*if(item.getItemId()==R.id.folder_update) {

        }//end folder_update*/

        if(item.getItemId()==R.id.folder_del){
            ConnectServer connectServer = new ConnectServer();
            connectServer.delFolder(urlDelFolder, Integer.toString(folder_no));

            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((MainActivity)MainActivity.mContext).refreshHome(); //홈으로 이동
                    //dialog.dismiss();     //닫기
                }
            });
            alert.setMessage("삭제 완료!");
            alert.show();
        }//end folder_del

        return (super.onOptionsItemSelected(item));
    }

}
