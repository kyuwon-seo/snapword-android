package com.example.myapplication.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.connectserver.ConnectServer;
import com.example.myapplication.listview.GameOneAdapter;
import com.example.myapplication.listview.GameOneItem;
import com.example.myapplication.listview.ListViewSetWordItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.arjsna.swipecardlib.SwipeCardView;

import static com.example.myapplication.MainActivity.urlSetWordList;

public class GameReActivity extends AppCompatActivity {

    private SwipeCardView swipeCardView;
    private ArrayList<GameOneItem> OneList = new ArrayList<>();
    private ArrayList<GameOneItem> BackList = new ArrayList<>();
    private ArrayList<GameOneItem> CopyList = new ArrayList<>();
    private ArrayList<GameOneItem> CopyBackList = new ArrayList<>();

    GameOneItem gameOneItem;
    public static GameOneAdapter adapter;
    public TextView t;
    public ImageView im;
    public int cardCnt=0;
    private Button hintBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameone);

        Intent intent = getIntent();

        OneList = (ArrayList<GameOneItem>)intent.getSerializableExtra("CopyList");
        BackList = (ArrayList<GameOneItem>)intent.getSerializableExtra("CopyBackList");

        adapter = new GameOneAdapter();
        swipeCardView = findViewById(R.id.frame);
        swipeCardView.setMinStackInAdapter(0);
        swipeCardView.setAdapter(adapter);

        adapter.addList(OneList);

        swipeCardView.setOnItemClickListener(new SwipeCardView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {

                gameOneItem = new GameOneItem();
                gameOneItem = (GameOneItem)adapter.getItem(cardCnt);

                Log.d("TAG", cardCnt + " 번째 CLICK : "+ gameOneItem.getCardText());

                final ObjectAnimator oa1 = ObjectAnimator.ofFloat(swipeCardView, "scaleX", 1f, 0f);
                final ObjectAnimator oa2 = ObjectAnimator.ofFloat(swipeCardView, "scaleX", 0f, 1f);
                oa1.setInterpolator(new DecelerateInterpolator());
                oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                oa1.setDuration(125);
                oa2.setDuration(125);
                oa1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        String flipText;
                        t = swipeCardView.getSelectedView().findViewById(R.id.cardText);
                        im = swipeCardView.getSelectedView().findViewById(R.id.cardImg);
                        if (t.getText().equals(((GameOneItem) adapter.getItem(cardCnt)).getCardText())) {//앞면이면
                            flipText = BackList.get(cardCnt).getCardText();
                            t.setText(flipText);
                            t.setVisibility(View.VISIBLE);
                            im.setImageBitmap(null);
                        } else if(t.getText().equals(BackList.get(cardCnt).getCardText())){//뒷면이면
                            flipText = ((GameOneItem) adapter.getItem(cardCnt)).getCardText();
                            t.setText(flipText);//성공
                            if(((GameOneItem) adapter.getItem(cardCnt)).getCardImg() != null) {
                                t.setVisibility(View.INVISIBLE);
                                im.setImageBitmap(((GameOneItem) adapter.getItem(cardCnt)).getCardImg());
                            }
                        }
                        oa2.start();
                    }
                });
                oa1.start();
            }
        });

        swipeCardView.setFlingListener(new SwipeCardView.OnCardFlingListener() {
            @Override
            public void onCardExitLeft(Object dataObject) {

                Log.d("TAG", cardCnt + " 번째 LEFT EXiT!!!");
                CopyList.add((GameOneItem)adapter.getItem(cardCnt));
                CopyBackList.add(BackList.get(cardCnt));
                //Toast.makeText(getApplicationContext(), "모르겠어요", Toast.LENGTH_LONG).show();
                cardCnt++;
            }

            @Override
            public void onCardExitRight(Object dataObject) {

                Log.d("TAG", cardCnt + " 번째 RIGHT EXiT!!!");
                //Toast.makeText(getApplicationContext(), "완벽해요", Toast.LENGTH_LONG).show();
                cardCnt++;
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

                for(int i=0; i<CopyList.size(); i++) {
                    Log.d("endGame", CopyList.get(i).getCardText());
                }
                /*if(CopyList.size()>0) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(GameOneActivity.this);
                    alert.setPositiveButton("계속하기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(getApplicationContext(), GameReActivity.class);
                            intent.putExtra("CopyList", CopyList);
                            intent.putExtra("CopyBackList", CopyBackList);
                            startActivity(intent);
                            finish();
                            dialog.dismiss();
                        }
                    });
                    alert.setNegativeButton("다시하기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            GameOneActivity.this.recreate();
                            BackList = CopyBackList;
                            dialog.dismiss();
                        }
                    });
                    alert.setMessage("이어서 하시겠습니까?");
                    alert.show();
                }*/
                if(CopyList.size()>0) {
                    Intent intent = new Intent(getApplicationContext(), WrongActivity.class);
                    intent.putExtra("CopyList", CopyList);
                    intent.putExtra("CopyBackList", CopyBackList);
                    startActivity(intent);
                    finish();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(GameReActivity.this);

                    builder.setMessage("다 외우셨네요!\n계속해서 복습해보세요^^");
                    builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                    builder.show();
                }
            }
            @Override
            public void onScroll(float scrollProgressPercent) { }
            @Override
            public void onCardExitTop(Object dataObject) { }
            @Override
            public void onCardExitBottom(Object dataObject) { }
        });
        hintBtn = findViewById(R.id.hintBtn);
        hintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adapter != null) {
                    String h = ((GameOneItem) adapter.getItem(cardCnt)).getHint();
                    Toast.makeText(getApplicationContext(), h, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}