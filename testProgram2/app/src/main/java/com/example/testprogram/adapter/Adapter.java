package com.example.testprogram.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.testprogram.PricelistAdapter;
import com.example.testprogram.R;
import com.example.testprogram.model.PriceList;

import java.util.ArrayList;

public class Adapter extends ArrayAdapter<PriceList> {

    private ArrayList<PriceList> arrayList;

    public Adapter(ArrayList<PriceList> arrayList) {
        super(PricelistAdapter.context, R.layout.listview_layout,arrayList);
    }

    public Adapter(Context context,ArrayList<PriceList> arrayList) {
        super(PricelistAdapter.context, R.layout.listview_layout,arrayList);
    }


    public static class ViewHolder{
        public TextView serialNumber, storeName, category, itemName, netWeight, quantity, price;

        public ViewHolder(View view) {
            serialNumber = view.findViewById(R.id.serialNumber);
            itemName = view.findViewById(R.id.itemName);
            storeName = view.findViewById(R.id.storeName);
            category = view.findViewById(R.id.category);
            quantity = view.findViewById(R.id.quantity);
            netWeight = view.findViewById(R.id.netWeight);
            price = view.findViewById(R.id.price);
        }

        public void fill(final ArrayAdapter<PriceList> adapter, final PriceList item, final int position){
            serialNumber.setText(item.getSerialNumber());
            itemName.setText(item.getItemName());
            storeName.setText(item.getStoreName());
            category.setText(item.getCategory());
            quantity.setText(item.getQuantity());
            netWeight.setText(item.getNetWeight());
            price.setText(item.getPrice());
        }
    }

    public View getView(int position, View convertview, ViewGroup parent){
        ViewHolder holder;
        PriceList item = getItem(position);
        if(convertview == null){
            convertview = PricelistAdapter.inflater.inflate(R.layout.listview_layout, parent, false);
            holder = new ViewHolder(convertview);
            convertview.setTag(holder);
        } else {
            holder = (ViewHolder) convertview.getTag();
        }
        holder.fill(this,item,position);
        return convertview;
    }
}