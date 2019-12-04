package com.example.myapplication;

import com.example.myapplication.listview.ListViewSetItem;
import com.example.myapplication.listview.ListViewWordItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonPass {

    public JSONObject jsonSetPass(ArrayList<ListViewWordItem> setList, String name, String id) {

        JSONObject obj = new JSONObject();
        try {
            JSONArray jArray = new JSONArray();//배열이 필요할때
            for (int i = 0; i < setList.size(); i++)//배열
            {
                JSONObject Object = new JSONObject();//배열 내에 들어갈 json
                Object.put("wordA", setList.get(i).getWordA());
                Object.put("wordB", setList.get(i).getWordB());
                Object.put("imgName", setList.get(i).getImgName());
                Object.put("hint", setList.get(i).getHint());
                jArray.put(Object);
            }
            obj.put("set_name", name);
            obj.put("user_id", id);
            obj.put("item", jArray);//배열을 넣음

            System.out.println("jsonSetString is : "+obj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /*public JSONObject jsonSetUpdatePass(ArrayList<ListViewWordItem> setList, String name, String id, String no) {

        JSONObject obj = new JSONObject();
        try {
            JSONArray jArray = new JSONArray();//배열이 필요할때
            for (int i = 0; i < setList.size(); i++)//배열
            {
                JSONObject Object = new JSONObject();//배열 내에 들어갈 json
                Object.put("wordA", setList.get(i).getWordA());
                Object.put("wordB", setList.get(i).getWordB());
                Object.put("imgName", setList.get(i).getImgName());
                jArray.put(Object);
            }
            obj.put("set_name", name);
            obj.put("set_no", no);
            obj.put("user_id", id);
            obj.put("item", jArray);//배열을 넣음

            System.out.println("jsonSetString is : "+obj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }*/

    public JSONObject jsonAddSetPass(ArrayList<ListViewSetItem> setList, int folder_no) {

        JSONObject obj = new JSONObject();
        try {
            JSONArray jArray = new JSONArray();//배열이 필요할때
            for (int i = 0; i < setList.size(); i++)//배열
            {
                JSONObject Object = new JSONObject();//배열 내에 들어갈 json
                Object.put("set_no", setList.get(i).getSet_no());
                jArray.put(Object);
            }
            obj.put("folder_no", folder_no);
            obj.put("item", jArray);//배열을 넣음

            System.out.println("jsonAddSetString is : "+obj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
