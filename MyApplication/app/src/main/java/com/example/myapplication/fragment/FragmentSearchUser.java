package com.example.myapplication.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.connectserver.ConnectServer;
import com.example.myapplication.listview.ListViewSetItem;
import com.example.myapplication.listview.ListViewSetSearchAdapter;
import com.example.myapplication.listview.ListViewUserItem;
import com.example.myapplication.listview.ListViewUserSearchAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.myapplication.MainActivity.urlSearchSet;
import static com.example.myapplication.MainActivity.urlSearchUser;
import static com.example.myapplication.fragment.FragmentSearch.searchActionBar;

public class FragmentSearchUser extends Fragment {

    private RecyclerView rv;
    private LinearLayoutManager llm;
    private ListViewUserSearchAdapter adapter;
    ArrayList<ListViewUserItem> listUserItem;

    EditText edit;
    TextView userText;
    private View v;
    private String searchKey = "";
    private String resNO = "검색결과가 없습니다.";

    public FragmentSearchUser.Command userBackInterface;

    public interface Command {
        void searchUserClicked(String user_id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        userBackInterface = (FragmentSearchUser.Command) context;
    }

    public FragmentSearchUser() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_search_user, container, false);
        userText = v.findViewById(R.id.search_user);

        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.pang);
        ((ImageView) v.findViewById(R.id.fragSearchUser)).setImageBitmap(bitmap);

        edit = searchActionBar.findViewById(R.id.search_info);
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i0, int i1, int i2) {

                searchKey = edit.getText().toString();

                if (!searchKey.equals("")) { //검색값이 있다?
                    //서버열고 바로 세트 검색
                    ConnectServer connectServer = new ConnectServer();
                    String userList = connectServer.searchUserRequest(urlSearchUser, searchKey);

                    //리스트뷰 뿌리자 []면 비어있고 있으면 뿌리겠지 ??
                    listUserItem = new ArrayList<ListViewUserItem>();

                    rv = v.findViewById(R.id.search_userList);
                    llm = new LinearLayoutManager(v.getContext());
                    rv.setHasFixedSize(true);
                    rv.setLayoutManager(llm);

                    try {
                        JSONArray jarray = new JSONArray(userList);

                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jsonObject = jarray.getJSONObject(i);
                            ListViewUserItem userItem = new ListViewUserItem();
                            userItem.setUser_id(jsonObject.getString("user_id"));

                            listUserItem.add(userItem);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    adapter = new ListViewUserSearchAdapter(listUserItem, userBackInterface);
                    adapter.notifyDataSetChanged();
                    rv.setAdapter(adapter);
                    //여기까지 리스트뷰 뿌리기 완료.
                    if (userList.equals("[]")) { //검색했더니 []다? 폴더가없다??
                        userText.setText(resNO);
                        Log.d("userList", "[]값 나왔어요");
                    } else { //검색했더니 폴더가 있다??
                        userText.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        return v;
    }

}