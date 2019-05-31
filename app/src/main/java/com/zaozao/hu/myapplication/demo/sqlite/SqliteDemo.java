package com.zaozao.hu.myapplication.demo.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zaozao.hu.myapplication.utils.LogUtils;

import java.io.File;

public class SqliteDemo {


    private SQLiteDatabase db;
    private String tableName;
    private String TAG = "SqliteDemo";

    @SuppressLint("SdCardPath")
    public SqliteDemo(String tableName, Context context) {
        this.tableName = tableName;
        File file = context.getDatabasePath("stu.db");
        db = SQLiteDatabase.openOrCreateDatabase(file, null);
        createTable(tableName);
    }


    /**
     * 创建表
     *
     * @param tableName 表名
     * @return
     */
    private void createTable(String tableName) {

        String stu_table = "create table if not exists " + tableName + "(_id integer primary key autoincrement,s_name text,s_number text)";
        if (db == null) return;
        try {
            db.execSQL(stu_table);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入数据
     */
    public void insert(String name, String date) {
        if (db != null) {
            ContentValues values = new ContentValues();
            values.put("s_name", name);
            values.put("s_number", date);
            db.insert(tableName, null, values);
        }
    }


    /**
     * 删除数据
     *
     * @param id
     */
    public void delete(int id) {
        if (db != null) {
            String whereCause = "_id=?";
            String[] whereArgs = {String.valueOf(id)};
            db.delete(tableName, whereCause, whereArgs);
        }
    }


    /**
     * 更新数据
     *
     * @param id
     */
    public void update(int id) {
        if (db != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("s_name", "刘小凤");
            String whereCause = "_id=?";
            String[] whereArgs = {String.valueOf(id)};
            db.update(tableName, contentValues, whereCause, whereArgs);
        }
    }


    /**
     * 查询数据
     */
    public void query() {
        if (db != null) {
            Cursor cursor = db.query(tableName, null, null, null, null, null, null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String sName = cursor.getString(cursor.getColumnIndexOrThrow("s_name"));
                String sNumber = cursor.getString(cursor.getColumnIndexOrThrow("s_number"));
                LogUtils.i(TAG, "id = " + id + "---name = " + sName + "-----sNumber = " + sNumber);
            }
            cursor.close();
        }
    }

    public void close() {
        if (db != null) {
            db.close();
        }
    }
}
