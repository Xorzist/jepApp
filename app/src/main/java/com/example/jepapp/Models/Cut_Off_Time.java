package com.example.jepapp.Models;

import java.sql.Time;

public class Cut_Off_Time {
   private String time;
   private String type;


    public Cut_Off_Time() {
    }

    public Cut_Off_Time(String type, String time) {
        this.time = time;
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
