package com.example.programtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    DBHelper dbHelper;
    DBHelper2 dbHelper2;
    ListView listView;
    ArrayList<PriceList> arrayList;
    ArrayList<PriceList> searchList;
    PriceAdapter adapter;
    String itemName, storeName;
    String quantity;
    String price;
    String selectedFilter;
    String currentSearchText = "";
    Button smButton, shopwiseButton;
    int white, darkGray, purple;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        dbHelper2 = new DBHelper2(this);
        listView = findViewById(R.id.listView);

        showPriceData();

        listView.setMultiChoiceModeListener(modeListener);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        smButton = (Button) findViewById(R.id.smButton);
        shopwiseButton = (Button) findViewById(R.id.shopwiseButton);
        white = ContextCompat.getColor(getApplicationContext(), R.color.white);
        darkGray = ContextCompat.getColor(getApplicationContext(), R.color.black);
        purple = ContextCompat.getColor(getApplicationContext(), R.color.purple_200);
        lookUnSelected(smButton);
        lookUnSelected(shopwiseButton);
    }

    private void unSelectedButton(){
        lookUnSelected(smButton);
        lookUnSelected(shopwiseButton);
    }

    private void lookSelected(Button parsedButton){
        parsedButton.setTextColor(white);
        parsedButton.setBackgroundColor(darkGray);
    }

    private void lookUnSelected(Button parsedButton){
        parsedButton.setTextColor(white);
        parsedButton.setBackgroundColor(purple);
    }

    private void showPriceData() {
        arrayList = dbHelper.getStudentData();
        adapter = new PriceAdapter(this, arrayList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.insert_menu, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView =(SearchView) MenuItemCompat.getActionView(search);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals("insert")){
            LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.insert_layout,null);
            EditText serialNumber = view.findViewById(R.id.serialNumber);
            EditText storeName = view.findViewById(R.id.storeName);
            EditText category = view.findViewById(R.id.category);
            EditText itemName = view.findViewById(R.id.itemName);
            EditText netWeight = view.findViewById(R.id.netWeight);
            EditText quantity = view.findViewById(R.id.quantity);
            EditText price = view.findViewById(R.id.price);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setView(view)
                    .setTitle("add product")
                    .setMessage("hi")
                    .setIcon(R.drawable.ic_insert)
                    .setPositiveButton("add new product", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int serial = Integer.parseInt(serialNumber.getText().toString());
                            String store = storeName.getText().toString();
                            String cat = category.getText().toString();
                            String item = itemName.getText().toString();
                            String net = netWeight.getText().toString();
                            int quan = Integer.parseInt(quantity.getText().toString());
                            double pri = Double.parseDouble(price.getText().toString());
                            boolean result = dbHelper.insertData(serial, store, cat, item, net, quan, pri);
                            if(result == true){
                                showPriceData();
                                Toast.makeText(MainActivity.this, "new product added", Toast.LENGTH_SHORT);
                            }else {
                                Toast.makeText(MainActivity.this, "new product not added", Toast.LENGTH_SHORT);
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
            builder.create().show();
        } else {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        currentSearchText = newText;
        newText = newText.toLowerCase();
        searchList = new ArrayList<>();
        for(PriceList priceList : arrayList){
            String serial = priceList.getSerialNumber().toLowerCase();
            String storeName = priceList.getStoreName().toLowerCase();
            String itemName = priceList.getItemName().toLowerCase();
            if(serial.contains(newText)){
                searchList.add(priceList);
            } else if(storeName.contains(newText)){
                searchList.add(priceList);
            } else if(itemName.contains(newText)){
                searchList.add(priceList);
            }
        }
        adapter.searchFilter(searchList);
        return true;
    }

    AbsListView.MultiChoiceModeListener modeListener = new AbsListView.MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
            PriceList priceList = arrayList.get(i);
            itemName = priceList.getItemName();
            storeName = priceList.getStoreName();
            quantity = priceList.getQuantity();
            price = priceList.getPrice();
            actionMode.setTitle(itemName);
        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            getMenuInflater().inflate(R.menu.abs_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            boolean result = dbHelper2.insertListData(storeName, itemName, quantity, price);
            if(result == true){
                Toast.makeText(MainActivity.this, itemName+" is added to list", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, itemName+" is already in list", Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {

        }
    };

    public void filterList(String status){
        selectedFilter = status;
        searchList = new ArrayList<>();
        for(PriceList priceList : arrayList){
            String storeName = priceList.getStoreName().toLowerCase();
            if(storeName.contains(status)){
                searchList.add(priceList);
            }
        }
        adapter.searchFilter(searchList);
    }

    public void smFilterButton(View view) {
        filterList("sm");
        unSelectedButton();
        lookSelected(smButton);
    }

    public void shopwiseFilterButton(View view) {
        filterList("shopwise");
        unSelectedButton();
        lookSelected(shopwiseButton);
    }

}