package com.example.myapplication.fragment;

import android.content.Context;
import android.drm.DrmStore;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView.OnEditorActionListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.myapplication.MainActivity.urlSearchFolder;
import static com.example.myapplication.fragment.FragmentSearch.searchActionBar;

public class FragmentSearchFolder extends Fragment {

    private RecyclerView rv;
    private LinearLayoutManager llm;
    private ListViewFolderSearchAdapter adapter;
    ArrayList<ListViewFoldItem> listFoldItem;

    EditText edit;
    TextView foldText;
    private View v;
    private String searchKey = "";
    private String resNO = "검색결과가 없습니다.";
    public Command foldBackInterface;

    public interface Command {
        void searchFoldClicked(String title, int no, String id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        foldBackInterface = (Command) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_search_folder, container, false);

        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.pang);
        ((ImageView) v.findViewById(R.id.fragSearchFolder)).setImageBitmap(bitmap);

        foldText = v.findViewById(R.id.search_folder);

        edit = searchActionBar.findViewById(R.id.search_info);
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i0, int i1, int i2) {
                Log.d("fold", edit.getText().toString());
                searchKey = edit.getText().toString();

                if (!searchKey.equals("")) { //검색값이 있다?
                    //서버열고 바로 폴더 검색
                    ConnectServer connectServer = new ConnectServer();
                    String folderList = connectServer.searchFoldRequest(urlSearchFolder, searchKey);

                    //리스트뷰 뿌리자 []면 비어있고 있으면 뿌리겠지 ??
                    listFoldItem = new ArrayList<ListViewFoldItem>();

                    rv = v.findViewById(R.id.search_folderList);
                    llm = new LinearLayoutManager(v.getContext());
                    rv.setHasFixedSize(true);
                    rv.setLayoutManager(llm);

                    try {
                        JSONArray jarray = new JSONArray(folderList);

                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jsonObject = jarray.getJSONObject(i);
                            ListViewFoldItem foldItem = new ListViewFoldItem();
                            foldItem.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_folder_black_24dp));
                            foldItem.setTitle(jsonObject.getString("folder_name"));
                            foldItem.setFolder_no(jsonObject.getInt("folder_no"));
                            foldItem.setDesc(jsonObject.getString("user_id"));

                            listFoldItem.add(foldItem);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    adapter = new ListViewFolderSearchAdapter(listFoldItem, foldBackInterface);
                    adapter.notifyDataSetChanged();
                    rv.setAdapter(adapter);
                    //여기까지 리스트뷰 뿌리기 완료.
                    if (folderList.equals("[]")) { //검색했더니 []다? 폴더가없다??
                        foldText.setText(resNO);
                        Log.d("foldList", "[]값 나왔어요");
                    } else { //검색했더니 폴더가 있다??
                        foldText.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            } //after 끝
        });

        return v;
    }

}