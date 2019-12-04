package com.example.myapplication.connectserver;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddSetRequest{

        public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        private String res = null;

        public String request(String url, JSONObject j){

            String json = j.toString();
            Log.d("jsonServer is : ", json);

            //Request Body에 서버에 보낼 데이터 작성
            RequestBody requestBody = RequestBody.create(JSON, json);

            //작성한 Request Body와 데이터를 보낼 url을 Request에 붙임
            final Request request = new Request.Builder().url(url).post(requestBody).build();

            //request를 Client에 세팅하고 Server로 부터 온 Response를 처리할 Callback 작성
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
                        Log.d("asyncAddSetRes", "asyncAddSetRes is " + res);
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
        }//setMakeRequest 요청 메소드 끝

}
