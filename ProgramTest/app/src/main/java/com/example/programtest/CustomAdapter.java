package com.example.programtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;


public class CustomAdapter extends ArrayAdapter<PriceList> {
    public CustomAdapter(@NonNull Context context, int resource, @NonNull List<PriceList> priceListsAdapter) {
        super(context, resource, priceListsAdapter);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PriceList priceList = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_layout, parent, false);
        }
        TextView itemView = (TextView) convertView.findViewById(R.id.item_name);
        TextView storeView = (TextView) convertView.findViewById(R.id.store_name);
        TextView netView = (TextView) convertView.findViewById(R.id.net_weight);
        TextView quantView = (TextView) convertView.findViewById(R.id.quantity2);
        TextView priceView = (TextView) convertView.findViewById(R.id.price2);

        itemView.setText(priceList.getItemName());
        storeView.setText(priceList.getStoreName());
        netView.setText(priceList.getNetWeight());
        quantView.setText(priceList.getNetWeight());
        priceView.setText(priceList.getPrice());

        return super.getView(position, convertView, parent);
    }
}
