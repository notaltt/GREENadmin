package com.example.testprogram.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.testprogram.database.DBHelper;

public class DBAdapter {
    Context context;
    SQLiteDatabase sqLiteDatabase;
    DBHelper helper;

    public DBAdapter(Context context){
        this.context = context;
        helper = new DBHelper(context);
    }

    public void openDB(){
        try{
            sqLiteDatabase =helper.getWritableDatabase();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void closeDB(){
        try{
            helper.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

}
