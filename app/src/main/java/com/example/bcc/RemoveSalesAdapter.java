package com.example.bcc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class RemoveSalesAdapter extends ArrayAdapter<RemoveSalesRecyclerHelper> {

    private Context mcontext;
    private int mResource;

    public RemoveSalesAdapter(@NonNull Context context, int resource, @NonNull ArrayList<RemoveSalesRecyclerHelper> objects) {
        super(context, resource, objects);
        mcontext = context;
        mResource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String srno = getItem(position).getSrno();
        String date = getItem(position).getDate();
        String time = getItem(position).getTime();
        String pType = getItem(position).getpType();
        String qty = getItem(position).getQty();
        String amt = getItem(position).getAmount();

        LayoutInflater inflater = LayoutInflater.from(mcontext);
        convertView = inflater.inflate(mResource,parent,false);

        TextView tvSrno = (TextView)convertView.findViewById(R.id.lvSrno);
        TextView tvDate = (TextView)convertView.findViewById(R.id.lvDate);
        TextView tvTime = (TextView)convertView.findViewById(R.id.lvTime);
        TextView tvPType = (TextView)convertView.findViewById(R.id.lvPType);
        TextView tvQty = (TextView)convertView.findViewById(R.id.lvQuantity);
        TextView tvAmt = (TextView)convertView.findViewById(R.id.lvAmt);

        tvSrno.setText(srno);
        tvDate.setText(date);
        tvTime.setText(time);
        tvPType.setText(pType);
        tvQty.setText(qty);
        tvAmt.setText(amt);

        return  convertView;

    }

}
