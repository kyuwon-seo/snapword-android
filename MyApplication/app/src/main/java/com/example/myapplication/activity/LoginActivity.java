package com.example.myapplication.activity;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.connectserver.ConnectServer;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import static com.example.myapplication.MainActivity.urlLogin;

public class LoginActivity extends AppCompatActivity {

    private EditText etId;
    private EditText etPassword;
    private TextView loginTxtInfo;
    private Button btnLogin;
    private String loginRes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        etId = findViewById(R.id.etId);
        etPassword = findViewById(R.id.etPassword);
        loginTxtInfo = findViewById(R.id.login_txtinfo);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String etIdTxt = etId.getText().toString();
                String etPasswordTxt = etPassword.getText().toString();

                ConnectServer connectServer = new ConnectServer();
                loginRes = connectServer.loginRequestPost(urlLogin, etIdTxt, etPasswordTxt);
                Log.d("loginRes", "loginRes is " + loginRes);

                if(loginRes != null) {
/*
                    try {
                        JSONArray jarray = new JSONArray(loginRes);
                        JSONObject jsonObject = jarray.getJSONObject(0);
                        String x = jsonObject.getString("user_id");
                        String x2 = jsonObject.getString("user_passwd");
                        Log.d("jsonRes", "jsonRes is " + x + x2 );
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
*/

                    SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                    //아이디가 '부르곰'이고 비밀번호가 '네이버'일 경우 SharedPreferences.Editor를 통해
                    //auto의 loginId와 loginPwd에 값을 저장해 줍니다.
                    SharedPreferences.Editor autoLogin = auto.edit();
                    autoLogin.putString("inputId", etIdTxt);
                    autoLogin.putString("inputPwd", etPasswordTxt);
                    //꼭 commit()을 해줘야 값이 저장됩니다 ㅎㅎ
                    autoLogin.commit();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}