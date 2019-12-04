/*
package com.example.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.JsonPass;
import com.example.myapplication.R;
import com.example.myapplication.SwipeDismissListViewTouchListener;
import com.example.myapplication.connectserver.ConnectServer;
import com.example.myapplication.listview.ListViewSetWordItem;
import com.example.myapplication.listview.ListViewWordAdapter;
import com.example.myapplication.listview.ListViewWordItem;
import com.example.myapplication.listview.ListViewWordTwoAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.myapplication.MainActivity.homeBar;
import static com.example.myapplication.MainActivity.loginId;
import static com.example.myapplication.MainActivity.urlFileUpload;
import static com.example.myapplication.MainActivity.urlSetMake;
import static com.example.myapplication.MainActivity.urlSetUpdate;

public class UpdateSetActivity extends AppCompatActivity {
    private final int REQ_CODE_SELECT_IMAGE = 100;
    private String img_path = new String();
    private Bitmap image_bitmap_copy = null;
    private Bitmap image_bitmap = null;
    private String imageName = null;
    private int imgPos;

    private ArrayList<ListViewSetWordItem> listSetWordItem;
    ListViewWordTwoAdapter adapter;
    ImageButton wordAddBtn;
    ImageButton makeSetBtn;
    EditText setName;
    private int num;
    private String setRes, set_name, set_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_set);

        ///////////////////
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());
        //////////////////

        num = 0;

        View homeActionBar = getLayoutInflater().inflate(R.layout.actionbar_makeset, null);
        homeBar = getSupportActionBar();
        homeBar.setCustomView(homeActionBar);
        homeBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_HOME |
                ActionBar.DISPLAY_SHOW_CUSTOM);

        ListView listview;

        // Adapter 생성
        adapter = new ListViewWordTwoAdapter(this);
        setName = findViewById(R.id.editSetName);
        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.wordListView);
        listview.setItemsCanFocus(true);
        listview.setAdapter(adapter);

        Intent intent = getIntent();
        set_no = intent.getStringExtra("set_no");
        set_name = intent.getStringExtra("set_name");
        listSetWordItem = (ArrayList<ListViewSetWordItem>) intent.getSerializableExtra("itemlist");

        setName.setText(set_name);
        if (listSetWordItem != null) {
            for (int i = 0; i < listSetWordItem.size(); i++) {
                adapter.addItem(listSetWordItem.get(i).getWordA(), listSetWordItem.get(i).getWordB(), num);
                adapter.notifyDataSetChanged();
                num++;
            }
        }

        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(listview,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    adapter.remove(position);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
        listview.setOnTouchListener(touchListener);
        listview.setOnScrollListener(touchListener.makeScrollListener());

        // (+) 버튼 클릭시 word리스트 동적 추가
        wordAddBtn = findViewById(R.id.wordAddBtn);
        wordAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.addItem("", "", num);
                adapter.notifyDataSetChanged();
                num++;
            }
        });

        // ㄴ 버튼 클릭시 word리스트 받아서 세트 업데이트
        makeSetBtn = findViewById(R.id.makeSetBtn);
        makeSetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //setName = findViewById(R.id.editSetName);
                String name = setName.getText().toString();
                ArrayList<ListViewWordItem> totalSetList = (ArrayList) adapter.getSetList();
                boolean result = true;

                if (name.equals("")) {
                    //name이 비어있다.
                    Toast.makeText(getApplicationContext(), "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (!name.equals("")) {

                    for (int i = 0; i < totalSetList.size(); i++) {
                        //word가 비어있다.
                        if (totalSetList.get(i).getWordA().equals("") || totalSetList.get(i).getWordB().equals("")) {
                            Toast.makeText(getApplicationContext(), "단어를 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                            result = false;
                        }
                    }
                    if (result == true) {
                        JsonPass j = new JsonPass();
                        JSONObject jsonSet = j.jsonSetUpdatePass(totalSetList, name, loginId, set_no);
                        ConnectServer connectServer = new ConnectServer();
                        setRes = connectServer.setUpdate(urlSetUpdate, jsonSet);
                        for (int i = 0; i < totalSetList.size(); i++) {
                            if (totalSetList.get(i).getImgPath() != null) {
                                Log.d("MakeSetActivity", "imgName is " + totalSetList.get(i).getImgName());
                                connectServer.DoFileUpload(urlFileUpload, totalSetList.get(i).getImgPath());
                            }
                        }
                        //res는 성공하면 set_no 실패하면 x (제목이 겹친다.)
                        if (!setRes.equals("x")) {
                            int no = Integer.parseInt(setRes.trim());
                            Log.d("makeSet", "set_no is " + no);
                            Intent intent = new Intent(getApplicationContext(), SetInfoActivity.class);
                            intent.putExtra("set_no", no);
                            intent.putExtra("set_name", name);
                            intent.putExtra("owner_id", loginId);
                            intent.putExtra("user_id", loginId);
                            intent.putExtra("word_cnt", "단어");
                            startActivity(intent);
                            finish();
                        } else if (setRes.equals("x")) {
                            Toast.makeText(getApplicationContext(), "제목이 중복되었습니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }//onCreat 끝

    public void cameraON(int position) {
        Log.d("BUTTON", "BUTTON CLICK " + position);
        this.imgPos = position;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Toast.makeText(getBaseContext(), "resultCode : " + data, Toast.LENGTH_SHORT).show();

        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    img_path = getImagePathToUri(data.getData()); //이미지의 URI를 얻어 경로값으로 반환.
                    Toast.makeText(getBaseContext(), "img_path : " + img_path, Toast.LENGTH_SHORT).show();
                    //이미지를 비트맵형식으로 반환
                    image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    //사용자 단말기의 width , height 값 반환
                    int reWidth = (int) (getWindowManager().getDefaultDisplay().getWidth());
                    int reHeight = (int) (getWindowManager().getDefaultDisplay().getHeight());

                    //image_bitmap 으로 받아온 이미지의 사이즈를 임의적으로 조절함. width: 400 , height: 300
                    image_bitmap_copy = Bitmap.createScaledBitmap(image_bitmap, 300, 300, true);
                    //ImageView image = (ImageView) findViewById(R.id.imageView);  //이미지를 띄울 위젯 ID값
                    //image.setImageBitmap(image_bitmap_copy);
                    adapter.setItem(imgPos, image_bitmap_copy, img_path, imageName);
                    adapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }//end of onActivityResult()

    public String getImagePathToUri(Uri data) {
        //사용자가 선택한 이미지의 정보를 받아옴
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        //이미지의 경로 값
        String imgPath = cursor.getString(column_index);
        Log.d("test", imgPath);

        //이미지의 이름 값
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);
        Toast.makeText(this, "이미지 이름 : " + imgName, Toast.LENGTH_SHORT).show();
        imageName = imgName;

        return imgPath;
    }//end of getImagePathToUri()

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d("kkk", "goodgood");
                finish();
        }
        return (super.onOptionsItemSelected(item));
    }
}
*/
