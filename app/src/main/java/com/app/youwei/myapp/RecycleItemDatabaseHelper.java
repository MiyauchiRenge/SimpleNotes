package com.app.youwei.myapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by Winky on 2016/8/12.
 */
public class RecycleItemDatabaseHelper extends SQLiteOpenHelper {
    private Context mContext;

    public static final String CREATE_ITEM = "create table RecycleItem ("
            + "id integer primary key autoincrement, "
            + "name text, "
            + "detail text, "
            + "type integer, "
            + "time integer, "
            + "color integer, "
            + "recycle text, "
            + "notification integer)";
    public RecycleItemDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ITEM);
       // Toast.makeText(mContext, "Create recycleDB succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
