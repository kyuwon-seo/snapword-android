package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.myapplication.activity.MakeSetActivity;
import com.example.myapplication.connectserver.ConnectServer;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import static com.example.myapplication.MainActivity.loginId;
import static com.example.myapplication.MainActivity.urlFoldMake;

public class BottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    public static BottomSheetDialog getInstance() {
        return new BottomSheetDialog();
    }

    private LinearLayout makeSet;
    private LinearLayout makeFolder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_dialog, container,false);
        makeSet = (LinearLayout) view.findViewById(R.id.makeSet);
        makeFolder = (LinearLayout) view.findViewById(R.id.makeFolder);

        makeSet.setOnClickListener(this);
        makeFolder.setOnClickListener(this);
        return view;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.makeSet:
                Intent intent = new Intent(getContext(), MakeSetActivity.class);
                startActivity(intent);
                break;
            case R.id.makeFolder:
                makeFolderShow(getContext());
                break;
        }
        dismiss();
    }
    //폴더 만들기 dialog
    public void makeFolderShow(Context context)
    {
        final EditText edittext = new EditText(getContext());
        edittext.setHint("폴더 이름");
        final Context c = context;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("폴더 만들기");
        builder.setView(edittext);
        builder.setPositiveButton("만들기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ConnectServer connectServer = new ConnectServer();
                        connectServer.foldMakeRequest(urlFoldMake, loginId, edittext.getText().toString());
                        ((MainActivity)MainActivity.mContext).refreshHome();
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }


}
