package com.example.testprogram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.testprogram.adapter.Adapter;
import com.example.testprogram.adapter.DBAdapter;
import com.example.testprogram.database.DBHelper;
import com.example.testprogram.model.PriceList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    ArrayList<PriceList> arrayList = new ArrayList<PriceList>();
    Adapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       listView = findViewById(R.id.listView);

        DBHelper mDBHELPER = new DBHelper(MainActivity.this, "PriceList");
        File database = getApplicationContext().getDatabasePath(DBHelper.DB_NAME);

        if(database.exists() == false){
            mDBHELPER.getReadableDatabase();
            if(!copyDatabase(MainActivity.this)){
                return;
            }
        }

        arrayList = mDBHELPER.getPriceList();
        adapter = new Adapter(arrayList);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

    }

    public boolean copyDatabase(Context context){
        try {
            InputStream inputStream =context.getAssets().open(DBHelper.DB_NAME);
            String OutFileName = DBHelper.DB_LOCATION+ DBHelper.DB_NAME;
            File f = new File(OutFileName);
            f.getParentFile().mkdirs();
            OutputStream outputStream = new FileOutputStream(OutFileName);

            byte[] buffer = new byte[1024];
            int length = 0;
            while((length = inputStream.read(buffer))>0){
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            return  true;
        } catch (IOException e){
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        SearchManager searchManager =(SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void searchList(String search) {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        ArrayList<PriceList> priceLists = dbHelper.search(search);
        if(priceLists != null){
            listView.setAdapter(new Adapter(getApplicationContext(),arrayList));
        }
    }
}