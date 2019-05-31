package com.zaozao.hu.myapplication.demo.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zaozao.hu.myapplication.utils.LogUtils;

public class SqliteHelper extends SQLiteOpenHelper {


    private String tableName;

    private String TAG = "SqliteHelper";

    public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SqliteHelper(Context context, String name, String tableName, int version) {
        super(context, name, null, version);
        this.tableName = tableName;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_sql = "create table if not exists " + tableName + "(_id integer primary key autoincrement,s_name text,s_number text)";
        db.execSQL(create_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtils.d(TAG, "oldVersion---->" + oldVersion + "newVersion----->" + newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    public void insert(){
       SQLiteDatabase db =  getWritableDatabase();
       db.insert(tableName,null,null);


       db.close();
    }
}
