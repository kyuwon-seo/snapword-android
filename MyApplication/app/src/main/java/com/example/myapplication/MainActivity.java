package com.example.myapplication;

import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;

import com.example.myapplication.activity.LoadingActivity;
import com.example.myapplication.activity.LoginActivity;
import com.example.myapplication.activity.SearchFoldActivity;
import com.example.myapplication.activity.SetInfoActivity;
import com.example.myapplication.fragment.FragmentHome;
import com.example.myapplication.fragment.FragmentPerson;
import com.example.myapplication.fragment.FragmentSearch;
import com.example.myapplication.fragment.FragmentSearchFolder;
import com.example.myapplication.fragment.FragmentSearchSet;
import com.example.myapplication.fragment.FragmentSearchUser;
import com.example.myapplication.fragment.FragmentSetAll;
import com.example.myapplication.fragment.FragmentSetlist;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements FragmentHome.Command, FragmentSetlist.SetCommand,
        FragmentSearchFolder.Command, FragmentSearchSet.Command, FragmentSearchUser.Command, FragmentSetAll.SetCommand{

    public static final String url = "http://18.216.244.70:8080/testServer/";
    //public static final String url = "http://172.30.1.54:8080/controller/";
    public static final String urlRoomList = url + "roomList";
    //public static final String urlPicasso = url+"resources/image/";
    public static final String urlPicasso = "http://18.216.244.70:8080/img/";
    public static final String urlLogin = url + "loginUser";
    public static final String urlFolder = url + "foldList";
    public static final String urlFoldMake = url + "foldMake";
    public static final String urlSetMake = url + "setMake";
    public static final String urlSetList = url + "setList";
    public static final String urlSetAllList = url + "setAllList";
    public static final String urlSetAllReq = url + "setAllReq";
    public static final String urlSetWordList = url + "setWordList";
    public static final String urlSearchFolder = url + "searchFoldList";
    public static final String urlSearchSet = url + "searchSetList";
    public static final String urlSearchUser = url + "searchUserList";
    public static final String urlAddSet = url + "addSet";
    public static final String urlAddToFold = url + "addToFold";
    public static final String urlFileUpload = url + "camera";
    public static final String urlRoomUpdate = url + "updateRoom";
    public static final String urlAddRoom = url + "addRoom";
    public static final String urlDelRoom = url + "delRoom";
    public static final String urlDelSet = url + "delSet";
    public static final String urlDelFolder = url + "delFolder";
    public static final String urlGetRoom = url + "getRoom";
    public static final String urlAddPerson = url + "addPerson";
    public static final String urlAddCheck = url + "addCheck";

    public static String loginId, loginPwd;
    public static Context mContext;

    public FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentHome fragmentHome = new FragmentHome();
    private FragmentSearch fragmentSearch = new FragmentSearch();
    private FragmentPerson fragmentPerson = new FragmentPerson();
    private FragmentSetlist fragmentSetlist = new FragmentSetlist();
    private FragmentSetAll fragmentSetAll = new FragmentSetAll();

    private BottomNavigationView bottomNavigationView;
    private BackPressCloseHandler backPressCloseHandler;

    public static ActionBar homeBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent loading = new Intent(this, LoadingActivity.class);
        startActivity(loading);

        mContext = this;
        backPressCloseHandler = new BackPressCloseHandler(this);

        //내장 xml id,pwd 저장하기
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        loginId = auto.getString("inputId", null);
        loginPwd = auto.getString("inputPwd", null);
        //아이디 저장 ㄴㄴ면 로그인 화면으로 이동
        if (loginId == null && loginPwd == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        //첫 화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentHome).commitAllowingStateLoss();

        //하단 메뉴바 선택시 동작
        bottomNavigationView = findViewById(R.id.navigationView);

        BottomNavigationHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

    }

    //하단 메뉴바 선택시 동작
    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.homeItem:
                    transaction.replace(R.id.frameLayout, fragmentHome, "Home").commitAllowingStateLoss();
                    break;
                case R.id.searchItem:
                    transaction.replace(R.id.frameLayout, fragmentSearch, "Search").commitAllowingStateLoss();
                    break;
                case R.id.addItem:
                    BottomSheetDialog bottomSheetDialog = BottomSheetDialog.getInstance();
                    bottomSheetDialog.show(getSupportFragmentManager(), "bottomSheet");
                    return false; //클릭효과 안보이게 false!! ㅎㅎ
                case R.id.personItem:
                    transaction.replace(R.id.frameLayout, fragmentPerson, "Person").commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }

    //fragment 화면 홈으로refresh하는 메소드
    public void refreshHome() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
        //홈이면 어답트리프래쉬. 아니면 리프래쉬

        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment.isVisible()) {
                if (fragment instanceof FragmentHome) {
                    fragmentTransaction.detach(fragment).attach(fragment).commit();
                    Log.d("frag is : ", "Home!");
                } else {
                    fragmentTransaction.detach(fragmentHome).attach(fragmentHome).commitAllowingStateLoss();
                    //navi 홈으로 선택
                    bottomNavigationView = findViewById(R.id.navigationView);
                    bottomNavigationView.setSelectedItemId(R.id.homeItem);
                }
            }
        }
    }
    public void refreshPerson() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment.isVisible()) {
                if (fragment instanceof FragmentPerson) {
                    fragmentTransaction.detach(fragment).attach(fragment).commit();
                    Log.d("frag is : ", "Person!");
                } else {
                    fragmentTransaction.detach(fragmentPerson).attach(fragmentPerson).commitAllowingStateLoss();
                    //navi 홈으로 선택
                    bottomNavigationView = findViewById(R.id.navigationView);
                    bottomNavigationView.setSelectedItemId(R.id.personItem);
                }
            }
        }
    }

    //searchFold 클릭시 세트 엑티비티 ㄱㄱ
    @Override
    public void searchFoldClicked(String title, int no, String id) {
        Intent intent = new Intent(getApplicationContext(), SearchFoldActivity.class);
        intent.putExtra("folder_no", no);
        intent.putExtra("folder_name", title);
        intent.putExtra("user_id", id);
        startActivity(intent);
    }
    //searchSet 클릭시 setInfo 엑티비티 ㄱㄱ
    @Override
    public void searchSetClicked(int no, String name, String cnt, String id, String user_id) {
        Intent intent = new Intent(getApplicationContext(), SetInfoActivity.class);
        intent.putExtra("set_no", no);
        intent.putExtra("set_name", name);
        intent.putExtra("word_cnt", cnt);
        intent.putExtra("owner_id", id);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
    }//searchUser 클릭시 ㄱㄱ
    @Override
    public void searchUserClicked(String user_id) {

    }
    //fragmentHome -> foldItem 클릭시 세트리스트 프래그먼트 ㄱㄱ
    @Override
    public void onClicked(String title, int no, String id) {

        Bundle bundle = new Bundle(3); //넘길값 2개~~
        bundle.putString("folder_name", title);
        bundle.putInt("folder_no", no);
        bundle.putString("user_id", id);
        fragmentSetlist.setArguments(bundle);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentSetlist).commitAllowingStateLoss();
    }
    //세트로 보기 클릭시
    @Override
    public void onSetAll(String id){
        Bundle bundle = new Bundle(1);
        bundle.putString("user_id", id);
        fragmentSetAll.setArguments(bundle);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentSetAll).commitAllowingStateLoss();
    }
    //setItem 클릭시 세트정보로 이동
    @Override
    public void onClicked(int no, String name, String cnt, String id, String user_id) {

        if (cnt.equals("homeback")) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frameLayout, fragmentHome).commitAllowingStateLoss();
            //transaction.replace(R.id.frameLayout, fragmentHome).commitAllowingStateLoss();
        } else {
            Log.d("setClickInfo", no + name + user_id);
            Intent intent = new Intent(getApplicationContext(), SetInfoActivity.class);
            intent.putExtra("set_no", no);
            intent.putExtra("set_name", name);
            intent.putExtra("word_cnt", cnt);
            intent.putExtra("owner_id", id);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
        }
    }
    //뒤로가기 2번 누르면 앱 종료
    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (fragment == fragmentHome) {
            backPressCloseHandler.onBackPressed();
        } else {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frameLayout, fragmentHome).commitAllowingStateLoss();
            bottomNavigationView.setSelectedItemId(R.id.homeItem);
        }
    }

}
