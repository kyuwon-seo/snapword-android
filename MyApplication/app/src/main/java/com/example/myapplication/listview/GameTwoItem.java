package com.example.myapplication.listview;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class GameTwoItem {
    private String res;
    private boolean status;

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        res = res;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("res", res);
        result.put("status", status);

        return result;
    }
}
