package com.app.youwei.myapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by YouWei on 2016/7/29.
 */
public class ItemDatabaseHelper extends SQLiteOpenHelper {
    private Context mContext;

    public static final String CREATE_ITEM = "create table Item ("
            + "id integer primary key autoincrement, "
            + "name text, "
            + "detail text, "
            + "type integer, "
            + "begin_time integer, "
            + "end_time integer, "
            + "color integer, "
            + "begin_week integer, "
            + "end_week integer, "
            + "notification integer)";

    public ItemDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ITEM);
        //Toast.makeText(mContext, "Create ItemDB succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
