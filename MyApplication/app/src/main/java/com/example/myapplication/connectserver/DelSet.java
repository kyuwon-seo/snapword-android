package com.example.myapplication.connectserver;

import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.ExecutionException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DelSet {

    OkHttpClient client = new OkHttpClient();
    private String res=null;

    public String request(String url, String no){

        //Request Body에 서버에 보낼 데이터 작성
        RequestBody requestBody = new FormBody.Builder().add("set_no", no).build();

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
                    res = s; //res 비어있으면 [] 라고나옴
                    Log.d("asyncDelSetRes", "asyncDelSetRes is " + res);
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
    }
}