package com.app.youwei.myapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Winky on 2016/8/11.
 */
public class RecycleItem implements Serializable {
    private String name;
    private String detail;
    private int type;
    private Date begin_time;
    private String recycle_time;
    private int color;
    private int notification;

    public RecycleItem() {
        name = "SB TA";
        detail = "地点:A101 第13小组";
        type = 0;
        begin_time = Calendar.getInstance().getTime();
        color = -12627531;
        recycle_time="everyday";
        notification = 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setBeginTime(Date time) {
        this.begin_time = time;
    }

    public void setRecycle(String recycle) {
        this.recycle_time = recycle;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setNotification(int notification) {
        this.notification = notification;
    }

    public String getName() {
        return name;
    }

    public String getDetail() {
        return detail;
    }

    public int getType() {
        return type;
    }

    public Date getBeginTime() {
        //Log.d("get", String.valueOf(begin_time));
        return begin_time;
    }

    public String getRecycle() {
        return recycle_time;
    }

    public int getNotification() {
        return notification;
    }

    public int getColor() {
        return color;
    }

}
