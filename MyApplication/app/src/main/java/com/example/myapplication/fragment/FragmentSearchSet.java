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

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.connectserver.ConnectServer;
import com.example.myapplication.listview.ListViewFoldItem;
import com.example.myapplication.listview.ListViewFolderSearchAdapter;
import com.example.myapplication.listview.ListViewSetItem;
import com.example.myapplication.listview.ListViewSetSearchAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.myapplication.MainActivity.urlSearchFolder;
import static com.example.myapplication.MainActivity.urlSearchSet;
import static com.example.myapplication.fragment.FragmentSearch.searchActionBar;

public class FragmentSearchSet extends Fragment {

    private RecyclerView rv;
    private LinearLayoutManager llm;
    private ListViewSetSearchAdapter adapter;
    ArrayList<ListViewSetItem> listSetItem;

    EditText edit;
    TextView setText;
    private View v;
    private String searchKey = "";
    private String resNO = "검색결과가 없습니다.";
    public FragmentSearchSet.Command setBackInterface;

    public interface Command {
        void searchSetClicked(int no, String name, String cnt, String id, String user_id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setBackInterface = (FragmentSearchSet.Command) context;
    }

    public FragmentSearchSet() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_search_set, container, false);
        setText = v.findViewById(R.id.search_set);

        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.pang);
        ((ImageView) v.findViewById(R.id.fragSearchSet)).setImageBitmap(bitmap);

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
                    String setList = connectServer.searchSetRequest(urlSearchSet, searchKey);

                    //리스트뷰 뿌리자 []면 비어있고 있으면 뿌리겠지 ??
                    listSetItem = new ArrayList<ListViewSetItem>();

                    rv = v.findViewById(R.id.search_setList);
                    llm = new LinearLayoutManager(v.getContext());
                    rv.setHasFixedSize(true);
                    rv.setLayoutManager(llm);

                    try {
                        JSONArray jarray = new JSONArray(setList);

                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jsonObject = jarray.getJSONObject(i);
                            ListViewSetItem setItem = new ListViewSetItem();
                            setItem.setSet_no(jsonObject.getInt("set_no"));
                            setItem.setSet_Name(jsonObject.getString("set_name"));
                            setItem.setOwner_id(jsonObject.getString("owner_id"));
                            setItem.setUser_id(jsonObject.getString("user_id"));
                            setItem.setWord_cnt(jsonObject.getInt("word_cnt"));

                            listSetItem.add(setItem);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    adapter = new ListViewSetSearchAdapter(listSetItem, setBackInterface);
                    adapter.notifyDataSetChanged();
                    rv.setAdapter(adapter);
                    //여기까지 리스트뷰 뿌리기 완료.
                    if (setList.equals("[]")) { //검색했더니 []다? 폴더가없다??
                        setText.setText(resNO);
                        Log.d("setList", "[]값 나왔어요");
                    } else { //검색했더니 폴더가 있다??
                        setText.setText("");
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