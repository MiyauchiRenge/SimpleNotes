package com.app.youwei.myapp;

import android.app.NotificationManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by YouWei on 2016/7/25.
 */
public class Item implements Parcelable {
    private String name;
    private String detail;
    private int type;
    private Date begin_time;
    private Date end_time;
    private int color;
    private int begin_week;
    private int end_week;
    private int notification;


    public Item() {
        name = "答辩";
        detail = "地点:A101 第13小组";
        type = 0;
        begin_time = Calendar.getInstance().getTime();
        end_time = Calendar.getInstance().getTime();
        color = -12627531;
        begin_week = 1;
        end_week = 1;
        notification = 0;
    }

    protected Item(Parcel in) {
        name = in.readString();
        detail = in.readString();
        type = in.readInt();
        color = in.readInt();
        begin_week = in.readInt();
        end_week = in.readInt();
         begin_time = new Date(in.readLong());
         end_time = new Date(in.readLong());
        notification = in.readInt();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(detail);
        dest.writeInt(type);
        dest.writeInt(color);
        dest.writeInt(begin_week);
        dest.writeInt(end_week);
        dest.writeLong(begin_time.getTime());
        dest.writeLong(end_time.getTime());
        dest.writeInt(notification);
    }


    public void setBegin_time(Date begin_time) {
        this.begin_time = begin_time;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setNotification(int notification) {
        this.notification = notification;
    }

    public void setBegin_week(int begin_week) {
        this.begin_week = begin_week;
    }

    public void setEnd_week(int end_week) {
        this.end_week = end_week;
    }


    public Date getBegin_time() {
        return begin_time;
    }

    public int getColor() {
        return color;
    }

    public String getDetail() {
        return detail;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public int getEnd_week() {
        return getWeekOfDate(end_time);
    }

    public int getBegin_week() {
        return getWeekOfDate(begin_time);
    }

    public int getNotification() {
        return notification;
    }

    public int getWeekOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        int w = calendar.get(Calendar.DAY_OF_WEEK) -1;
        return w;
    }

}
