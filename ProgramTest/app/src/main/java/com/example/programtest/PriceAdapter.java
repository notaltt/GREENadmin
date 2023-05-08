package com.example.programtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PriceAdapter extends BaseAdapter {
    Context  context;
    ArrayList<PriceList> arrayList;

    public PriceAdapter(Context context, ArrayList<PriceList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.listview_layout, null);
        TextView item_name = convertView.findViewById(R.id.item_name);
        TextView store_name = convertView.findViewById(R.id.store_name);
        TextView net_weight = convertView.findViewById(R.id.net_weight);
        TextView quantity2 = convertView.findViewById(R.id.quantity2);
        TextView price2 = convertView.findViewById(R.id.price2);
        PriceList priceList = arrayList.get(position);
        String item1 = priceList.getItemName();
        String store1 = priceList.getStoreName();
        String net1 = priceList.getNetWeight();
        String quan1 = priceList.getQuantity();
        String price1 = priceList.getPrice();
        item_name.setText(item1);
        store_name.setText(store1);
        net_weight.setText(net1);
        quantity2.setText(quan1);
        price2.setText(price1);

        return convertView;
    }

    public void searchFilter(ArrayList<PriceList> searchList) {
        arrayList = new ArrayList<>();
        arrayList.addAll(searchList);
        notifyDataSetChanged();
    }
}
