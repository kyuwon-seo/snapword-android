package com.example.myapplication.connectserver;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConnectServer extends AppCompatActivity {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    private String res = null;

    //사진파일 서버에 저장
    public void DoFileUpload(String apiUrl, String absolutePath) {
        HttpFileUpload httpFileUpload = new HttpFileUpload();
        httpFileUpload.request(apiUrl, "", absolutePath);
    }
    public String roomList(String url){
        RoomList roomList = new RoomList();
        res = roomList.request(url);

        return res;
    }
    //채팅방 세트 변경
    public String updateRoom(String url, String room_no, String no){
        RoomUpdate roomUpdate = new RoomUpdate();
        res = roomUpdate.request(url, room_no, no);
        return res;
    }
    //채팅방 생성
    public String addRoom(String url, String no, String name, String id){
        AddRoom addRoom = new AddRoom();
        res = addRoom.request(url, no, name, id);
        return res;
    }
    //채팅방 삭제
    public String delRoom(String url, String no){
        DelRoom delRoom = new DelRoom();
        res = delRoom.request(url, no);
        return res;
    }
    //세트 삭제
    public String delSet(String url, String no){
        DelSet delSet = new DelSet();
        res = delSet.request(url, no);
        return res;
    }
    //폴더 삭제
    public String delFolder(String url, String no){
        DelFolder delFolder = new DelFolder();
        res = delFolder.request(url, no);
        return res;
    }
    //채팅방 get
    public String getRoom(String url, String no){
        GetRoom getRoom = new GetRoom();
        res = getRoom.request(url, no);
        return res;
    }
    //채팅인원 추가
    public String addPerson(String url, int no, int person){
        AddPerson addPerson = new AddPerson();
        res = addPerson.request(url, no, person);
        return res;
    }
    //게임 상태 변경
    public String addCheck(String url, int no, String chk){
        AddCheck addCheck = new AddCheck();
        res = addCheck.request(url, no, chk);
        return res;
    }
    //AddSetActivity 세트추가
    public void addSetRequest(String url, JSONObject j){
        AddSetRequest addSetRequest = new AddSetRequest();
        addSetRequest.request(url,j);
    }
    //세트추가 세트리스트
    public String setAllRequest(String url, String user_id){

        SetAllRequest setAllRequest = new SetAllRequest();
        res = setAllRequest.request(url,user_id);

        return res;
    }
    //세트로 보기
    public String setAllReq(String url, String user_id){

        SetAllReq setAllReq = new SetAllReq();
        res = setAllReq.request(url,user_id);

        return res;
    }
    //선택 폴더의 세트리스트
    public String setRequestPost(String url, int folder_no){

        SetRequestPost setRequest = new SetRequestPost();
        res = setRequest.request(url,folder_no);

        return res;
    }
    //세트 만들고 세트의 정보요청
    public String setMakeRequest(String url, JSONObject j){

        SetMakeRequest setMakeRequest = new SetMakeRequest();
        res = setMakeRequest.request(url,j);

        return res;
    }
    //세트 만들고 세트의 정보요청
    public String setUpdate(String url, JSONObject j){

        SetUpdate setUpdate = new SetUpdate();
        res = setUpdate.request(url,j);

        return res;
    }
    //선택 세트의 word리스트
    public String setWordRequest(String url, String no){

        SetWordRequest setWordRequest = new SetWordRequest();
        res = setWordRequest.request(url, no);

        return res;
    }
    //검색 폴더 리스트
    public String searchFoldRequest(String url, String text){

        SearchFoldRequest searchFoldRequest = new SearchFoldRequest();
        res = searchFoldRequest.request(url, text);

        return res;
    }
    //검색 세트 리스트
    public String searchSetRequest(String url, String text){

        SearchSetRequest searchSetRequest = new SearchSetRequest();
        res = searchSetRequest.request(url, text);

        return res;
    }
    //검색 유저 리스트
    public String searchUserRequest(String url, String text){

        SearchUserRequest searchUserRequest = new SearchUserRequest();
        res = searchUserRequest.request(url, text);

        return res;
    }

    public String requestPost(String url, String id, String password){

        //Request Body에 서버에 보낼 데이터 작성
        RequestBody requestBody = new FormBody.Builder().add("userId", id).add("userPassword", password).build();

        //작성한 Request Body와 데이터를 보낼 url을 Request에 붙임
        Request request = new Request.Builder().url(url).post(requestBody).build();

        //request를 Client에 세팅하고 Server로 부터 온 Response를 처리할 Callback 작성
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("error", "Connect Server Error is " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("success", "Response Body is " + response.body().string());
                res = response.body().string();
            }
        });
        return res;
    }
    //login 요청 메소드
    public String loginRequestPost(String url, String id, String password) {

        //Request Body에 서버에 보낼 데이터 작성
        RequestBody requestBody = new FormBody.Builder().add("user_id", id).add("user_passwd", password).build();

        //작성한 Request Body와 데이터를 보낼 url을 Request에 붙임
        final Request request = new Request.Builder().url(url).post(requestBody).build();

        AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    Response response = client.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        return null;
                    }
                    return response.body().string();
                } catch(Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s != null) {
                    res = s;
                    Log.d("asyncLoginRes", "asyncLoginRes is " + res);
                }
            }
    };
        try {
            res = asyncTask.execute().get();
        }catch (InterruptedException e){
            e.printStackTrace();
        }catch(ExecutionException e){
            e.printStackTrace();
        }
        return res;
    }//login 메소드 끝

    //folder 요청 메소드
    public String foldRequestPost(String url, String id) {

        //Request Body에 서버에 보낼 데이터 작성
        RequestBody requestBody = new FormBody.Builder().add("user_id", id).build();
        Log.d("foldRes","user_id is "+ id);

        //작성한 Request Body와 데이터를 보낼 url을 Request에 붙임
        final Request request = new Request.Builder().url(url).post(requestBody).build();

        AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    Response response = client.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        return null;
                    }
                    return response.body().string();
                } catch(Exception e)
                {
                    e.printStackTrace();
                    return null;
                }
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s != null) {
                    res = s;
                    Log.d("asyncFoldRes", "asyncFoldRes is " + res);
                }
            }
        };
        try {
            res = asyncTask.execute().get();
        }catch (InterruptedException e){
            e.printStackTrace();
        }catch(ExecutionException e){
            e.printStackTrace();
        }
        return res;
    }//folder 요청 메소드 끝

    //folder 만들기 요청 메소드
    public void foldMakeRequest(String url, String id, String name){

        //Request Body에 서버에 보낼 데이터 작성
        RequestBody requestBody = new FormBody.Builder().add("user_id", id).add("folder_name", name).build();

        //작성한 Request Body와 데이터를 보낼 url을 Request에 붙임
        Request request = new Request.Builder().url(url).post(requestBody).build();

        //request를 Client에 세팅하고 Server로 부터 온 Response를 처리할 Callback 작성
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("error", "Connect Server Error is " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("success", "MakeFoldResponse Body is " + response.body().string().trim());
            }
        });
    }//폴더 요청 끝



}
