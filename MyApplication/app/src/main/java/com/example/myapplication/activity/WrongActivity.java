package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.listview.GameOneItem;
import com.example.myapplication.listview.ListViewSetWordAdapter;
import com.example.myapplication.listview.ListViewWrongAdapter;

import java.util.ArrayList;

public class WrongActivity extends AppCompatActivity {

    private ArrayList<GameOneItem> CopyList = new ArrayList<>();
    private ArrayList<GameOneItem> CopyBackList = new ArrayList<>();
    private RecyclerView rv;
    private LinearLayoutManager llm;
    private ListViewWrongAdapter adapter;
    private Button reGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong);

        Intent intent = getIntent();
        CopyList = (ArrayList<GameOneItem>)intent.getSerializableExtra("CopyList");
        CopyBackList = (ArrayList<GameOneItem>)intent.getSerializableExtra("CopyBackList");

        rv = findViewById(R.id.wrongWord_list);
        llm = new LinearLayoutManager(getApplicationContext());
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);

        adapter = new ListViewWrongAdapter(CopyList, CopyBackList);
        rv.setAdapter(adapter);

        reGame = findViewById(R.id.wrongBtn);
        reGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GameReActivity.class);
                intent.putExtra("CopyList", CopyList);
                intent.putExtra("CopyBackList", CopyBackList);
                startActivity(intent);
                finish();
            }
        });
    }

}
