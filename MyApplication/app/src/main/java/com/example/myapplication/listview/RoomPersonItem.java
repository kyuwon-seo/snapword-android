package com.example.myapplication.listview;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class RoomPersonItem {

    private int room_no;
    private int person;
    private int set_no;
    private boolean room_check;

    public int getSet_no() { return set_no; }

    public void setSet_no(int set_no) { this.set_no = set_no; }

    public boolean isRoom_check() {
        return room_check;
    }

    public void setRoom_check(boolean room_check) {
        this.room_check = room_check;
    }

    public int getRoom_no() {
        return room_no;
    }

    public void setRoom_no(int room_no) {
        this.room_no = room_no;
    }

    public int getPerson() {
        return person;
    }

    public void setPerson(int person) {
        this.person = person;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("room_no", room_no);
        result.put("person", person);
        result.put("room_check", room_check);
        result.put("set_no", set_no);

        return result;
    }
}
