package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.activity.GameTwoActivity;
import com.example.myapplication.connectserver.ConnectServer;
import com.example.myapplication.listview.GameTwoChatItem;
import com.example.myapplication.listview.GameTwoItem;
import com.example.myapplication.listview.GameTwoScoreItem;
import com.example.myapplication.listview.ListViewSetWordItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.myapplication.MainActivity.urlPicasso;
import static com.example.myapplication.MainActivity.urlSetWordList;
import static com.example.myapplication.activity.GameTwoActivity.q_cntTxt;

public class GameTwo extends Thread {

    private ArrayList<ListViewSetWordItem> gameList;
    private ArrayList<GameTwoScoreItem> scoreList;
    public static GameTwo gameTwo;
    public static String Q_res, res_id;
    private String set_no, room_no;
    private DatabaseReference resRef;
    private GameTwoActivity activity;
    private int q_cnt, max, game_no, timeCnt;
    private Random random;

    public void start(GameTwoActivity act, int s_no, int r_no) {
        gameTwo = this;
        set_no = Integer.toString(s_no);
        room_no = Integer.toString(r_no);
        resRef = FirebaseDatabase.getInstance().getReference(room_no);
        activity = act;
        //activity.gameTxt.setText("Game Start!!");

        ConnectServer connectServer = new ConnectServer();
        String wordList = connectServer.setWordRequest(urlSetWordList, set_no);
        gameList = new ArrayList<ListViewSetWordItem>();
        scoreList = new ArrayList<GameTwoScoreItem>();

        if (wordList != null) {
            getGameList(wordList); // 문제List 구해오기
            q_cnt = 0;
            max = gameList.size();
            Q_resStart();
        }

    }

    public void Q_resStart() {
        random = new Random();
        //일정시간 타이머후 게임시작
        timeCnt = 10;
        q_cnt++;
        CountDownTimer tt = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {
                Log.d("timeCnt", "timeCnt is " + timeCnt);
                timeCnt--;
                if (gameList.size() > 0) {
                    if ((timeCnt > 0) && (timeCnt < 6)) {
                        q_cntTxt.setText(timeCnt + "");
                    } else {
                        q_cntTxt.setText("");
                    }
                } else {
                    if ((timeCnt == 1)) {
                        activity.q_countTxt.setText("총 "+ q_cnt + "문제");
                        activity.gameTxt.setText("퀴즈종료\n\n"+getScore());
                        activity.gameTwoEnd(Integer.parseInt(room_no));
                        this.cancel();
                    }
                }
            }

            @Override
            public void onFinish() {
                activity.gameCard.setVisibility(View.VISIBLE);
                activity.q_countTxt.setText("Quiz" + q_cnt);
                if (timeCnt == 0) {
                    //game_no = random.nextInt(gameList.size()); //총 문제중 랜덤 1개 추출
                    if(gameList.size()>0) game_no = gameList.size()-1;

                    if (gameList.get(game_no).getImgBit() == null) {
                        activity.gameTxt.setVisibility(View.VISIBLE);
                        activity.gameImg.setVisibility(View.INVISIBLE);
                        activity.gameTxt.setText("정답을 맞춰보세요.\n\n" + gameList.get(game_no).getWordA());
                    } else {
                        activity.gameTxt.setVisibility(View.INVISIBLE);
                        activity.gameImg.setVisibility(View.VISIBLE);
                        activity.gameImg.setImageBitmap(gameList.get(game_no).getImgBit());
                    }
                    Q_res = gameList.get(game_no).getWordB();
                    //문제보여줬으니 제한시간동안 채팅 확인하고 정답자 체크하자
                    countDown();
                    this.cancel();
                }
            }
        }.start();
    }

    public void countDown() {
        Log.d("countDown", "countDown Start");

        CountDownTimer timer = new CountDownTimer(10000, 500) {
            @Override
            public void onTick(long l) {
                if (res_id != null) {
                    activity.gameTxt.setVisibility(View.VISIBLE);
                    activity.gameImg.setVisibility(View.INVISIBLE);
                    activity.gameTxt.setText(res_id + " 님이 맞추셨습니다.\n\n정답 : " + Q_res);

                    /*GameTwoChatItem item = new GameTwoChatItem();
                    item.setUser_id("----------정답자[" + res_id + "]----------");
                    item.setUser_txt("");
                    resRef.child("chat").push().setValue(item);*/

                    setScore(res_id);
                    Q_res = null;
                    res_id = null;
                    gameList.remove(game_no);
                    Q_resStart();
                    this.cancel();
                    Log.d("countDown", "countDown END");
                }
            }

            @Override
            public void onFinish() {
                if (res_id == null) {
                    activity.gameTxt.setVisibility(View.VISIBLE);
                    activity.gameImg.setVisibility(View.INVISIBLE);
                    activity.gameTxt.setText("땡~ 시간초과!\n\n정답 : " + Q_res);
                    gameList.remove(game_no);
                    Q_resStart();
                    this.cancel();
                    Log.d("countDown", "countDown END");
                }
            }
        }.start();
    }

    public void setScore(String id){
        GameTwoScoreItem item = new GameTwoScoreItem();
        if(scoreList != null){
            for(int i=0; i<scoreList.size(); i++){
                if(scoreList.get(i).getUser_id() == id){ //리스트에 정답자 id 있으면
                    int sc = scoreList.get(i).getScore();
                    scoreList.get(i).setScore(sc+1);
                    item.setUser_id(scoreList.get(i).getUser_id());
                }
            }
            if(item.getUser_id() == null){
                item.setUser_id(id); item.setScore(1);
                scoreList.add(item);
            }
        }else{
            item.setUser_id(id); item.setScore(1);
            scoreList.add(item);
        }
    }

    public String getScore(){
        String score = "정답자 없음";
        if(scoreList.size() > 0){
            score = "정답자\n";
            for(int i=0; i<scoreList.size(); i++){
                score += scoreList.get(i).getUser_id();
                score += " "+scoreList.get(i).getScore()+"개\n";
            }
        }

        return score;
    }

    public void getGameList(String list) {
        try {
            JSONArray jarray = new JSONArray(list);

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject jsonObject = jarray.getJSONObject(i);
                final ListViewSetWordItem wordItem = new ListViewSetWordItem();

                wordItem.setWordA(jsonObject.getString("word_a"));
                wordItem.setWordB(jsonObject.getString("word_b"));
                if (!jsonObject.getString("img_name").equals("null")) {
                    Log.d("Img_name", "Img_name is " + jsonObject.getString("img_name"));
                    Picasso.with((activity).getApplicationContext())
                            .load(urlPicasso + jsonObject.getString("img_name"))
                            .resize(300, 310).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            //adapter.setItemImg(pica_no, bitmap);
                            wordItem.setImgBit(bitmap);
                        }
                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {}
                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {}
                    });
                }
                gameList.add(wordItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
