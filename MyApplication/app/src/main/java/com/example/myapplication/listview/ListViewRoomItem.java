package com.example.myapplication.listview;

import java.sql.Timestamp;
import java.util.Date;

public class ListViewRoomItem {

    private int room_no;
    private int set_no;
    private String room_name;
    private boolean room_check; //채팅방 입장가능 여부( 풀방? 게임중?)
    private int room_person;//채팅방 인원
    private String room_date;
    private String user_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public boolean isRoom_check() {
        return room_check;
    }

    public void setRoom_check(boolean room_check) {
        this.room_check = room_check;
    }

    public int getRoom_person() {
        return room_person;
    }

    public void setRoom_person(int room_person) {
        this.room_person = room_person;
    }

    public String getRoom_date() {
        return room_date;
    }

    public void setRoom_date(String room_date) {
        this.room_date = room_date;
    }

    public int getRoom_no() {
        return room_no;
    }

    public void setRoom_no(int room_no) {
        this.room_no = room_no;
    }

    public int getSet_no() {
        return set_no;
    }

    public void setSet_no(int set_no) {
        this.set_no = set_no;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }
}
