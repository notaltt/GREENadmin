package com.example.testprogram.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.testprogram.PricelistAdapter;
import com.example.testprogram.model.PriceList;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static String DB_NAME ;
    public static String DB_TABLE ;
    public static final String DB_LOCATION = "/data/data/"+ PricelistAdapter.context.getPackageName()+"/databases/";
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public DBHelper(@Nullable Context context, String dbName) {
        super(context, DB_NAME, null, 1);
        DB_NAME = dbName+".db";
        DB_TABLE = "priceList";
        mContext = context;
    }

    public DBHelper(Context context){
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {}

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}

    public void OpenDatabase(){
        String DBLocation = mContext.getDatabasePath(DB_NAME).getPath();
        if(mDatabase!=null && mDatabase.isOpen()){
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(DBLocation, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void CloseDatabase(){
        if(mDatabase != null){
            mDatabase.close();
        }
    }

    public ArrayList<PriceList> getPriceList(){
        PriceList priceList = null;
        ArrayList<PriceList> arrayList = new ArrayList<PriceList>();
        OpenDatabase();
        Cursor cursor = mDatabase.rawQuery("select * from "+ DB_TABLE, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            priceList = new PriceList(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
            arrayList.add(priceList);
            cursor.moveToNext();
        }
        cursor.close();
        CloseDatabase();
        return arrayList;
    }

    public ArrayList<PriceList> search(String search){
        ArrayList<PriceList> priceLists = null;
        try{
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select * from "+"priceList"+" where "+"serialNumber"+" like ?", new String[] { "%" + search + "%"});
            if(cursor.moveToFirst()){
                priceLists = new ArrayList<PriceList>();
                do{
                    PriceList priceList = new PriceList();
                    priceList.setSerialNumber(cursor.getString(0));
                    priceList.setStoreName(cursor.getString(1));
                    priceList.setCategory(cursor.getString(2));
                    priceList.setItemName(cursor.getString(3));
                    priceList.setNetWeight(cursor.getString(4));
                    priceList.setQuantity(cursor.getString(5));
                    priceList.setPrice(cursor.getString(6));
                }while (cursor.moveToNext());
            }
        }catch (Exception e){
            priceLists = null;
        }
        return priceLists;
    }
}
