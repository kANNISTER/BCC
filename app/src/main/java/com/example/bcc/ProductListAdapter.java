package com.example.bcc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends ArrayAdapter<ListViewRecyclerHelper> {

    private Context mcontext;
    private int mResource;

    public ProductListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ListViewRecyclerHelper> objects) {
        super(context, resource, objects);
        mcontext = context;
        mResource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String srno = getItem(position).getSrno();
        String product = getItem(position).getProduct();
        String quantity = getItem(position).getQuantity();
        String price = getItem(position).getPrice();
        String amount = getItem(position).getAmount();

        LayoutInflater inflater = LayoutInflater.from(mcontext);
        convertView = inflater.inflate(mResource,parent,false);

        TextView srnoId = (TextView)convertView.findViewById(R.id.lvSrnoId);
        TextView productId = (TextView)convertView.findViewById(R.id.lvProductId);
        TextView quantityId = (TextView)convertView.findViewById(R.id.lvQuantityId);
        TextView priceId = (TextView)convertView.findViewById(R.id.lvPriceId);
        TextView amountId = (TextView)convertView.findViewById(R.id.lvAmountId);

        srnoId.setText(srno);
        productId.setText(product);
        quantityId.setText(quantity);
        priceId.setText(price);
        amountId.setText(amount);

        return  convertView;

    }


}
