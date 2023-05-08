package com.example.programtest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper2 extends SQLiteOpenHelper {
    public static final String DB_NAME = "listdata.db";
    public static final String TABLE_NAME = "list";;
    public static final String COL_STORE = "storeName";;
    public static final String COL_ITEM = "itemName";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_PRICE = "price";


    public DBHelper2(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+TABLE_NAME+"(storeName TEXT PRIMARY KEY, itemName TEXT, quantity INTEGER, price REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+TABLE_NAME);
    }


    public boolean insertListData(String storeName, String itemName, String quantity, String price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_STORE, storeName);
        contentValues.put(COL_ITEM, itemName);;
        contentValues.put(COL_QUANTITY, quantity);
        contentValues.put(COL_PRICE, price);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result==-1){
            return false;
        }else {
            return true;
        }
    }

}
