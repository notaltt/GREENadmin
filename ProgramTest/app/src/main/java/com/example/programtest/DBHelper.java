package com.example.programtest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "pricedata.db";
    public static final String TABLE_NAME = "pricelist";
    public static final String COL_SERIAL = "serialNumber";
    public static final String COL_STORE = "storeName";
    public static final String COL_CATEGORY = "category";
    public static final String COL_ITEM = "itemName";
    public static final String COL_NET = "netWeight";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_PRICE = "price";


    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+TABLE_NAME+"(serialNumber INTEGER PRIMARY KEY, storeName TEXT, category TEXT, " +
                "itemName TEXT, netWeight TEXT, quantity INTEGER, price REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+TABLE_NAME);
    }

    public boolean insertData(int serialNumber, String storeName, String category, String itemName, String netWeight, int quantity, double price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_SERIAL, serialNumber);
        contentValues.put(COL_STORE, storeName);
        contentValues.put(COL_CATEGORY, category);
        contentValues.put(COL_ITEM, itemName);
        contentValues.put(COL_NET, netWeight);
        contentValues.put(COL_QUANTITY, quantity);
        contentValues.put(COL_PRICE, price);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result==-1){
            return false;
        }else {
            return true;
        }
    }

    public ArrayList<PriceList> getStudentData(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<PriceList> arrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from "+TABLE_NAME, null);
        while(cursor.moveToNext()){
            String serialNumber = cursor.getString(0);
            String storeName = cursor.getString(1);
            String category = cursor.getString(2);
            String itemName = cursor.getString(3);
            String netWeight = cursor.getString(4);
            String quantity = cursor.getString(5);
            String price = cursor.getString(6);
            PriceList priceList = new PriceList(serialNumber, storeName, category, itemName, netWeight, quantity, price);
            arrayList.add(priceList);
        }
        return arrayList;
    }
}
