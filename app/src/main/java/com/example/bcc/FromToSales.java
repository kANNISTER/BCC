package com.example.bcc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompatExtras;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FromToSales extends AppCompatActivity {

    private ListView lvFromToSale;
    private RemoveSalesAdapter adapter;
    public ArrayList<RemoveSalesRecyclerHelper> arrayList;
    private DBHelper DB;
    private int total=0, quantity=0;
    private TextView tvTotal,tvQuantity;
    private String fDate , tDate ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from_to_sales);

        Bundle bundle = getIntent().getExtras();
        fDate =  bundle.getString("fDate");
        tDate = bundle.getString("tDate");

        lvFromToSale = (ListView)findViewById(R.id.lvFromToSale);
        tvQuantity = (TextView)findViewById(R.id.tvQtyCnt);
        tvTotal = (TextView)findViewById(R.id.tvTotalCnt);

        arrayList = new ArrayList<RemoveSalesRecyclerHelper>();
        adapter = new RemoveSalesAdapter(this, R.layout.list_view_recycler_remove_sales,arrayList);

        lvFromToSale.setAdapter(adapter);

        DB = new DBHelper(this);
        try{
            showSales();
        }catch (Exception e)
        {
            String ex = String.valueOf(e);
            Toast.makeText(FromToSales.this,ex,Toast.LENGTH_SHORT).show();
        }

    }
    private void showSales()
    {
        int i = 1;
        arrayList.clear();
        String a, b, c, d, e;
        RemoveSalesRecyclerHelper f;
        Cursor cursor = DB.retrieveSalesFromTo(fDate,tDate);
        int count = cursor.getCount();
        if (count != 0) {
            arrayList.clear();
            while (cursor.moveToNext()) {
                a = cursor.getString(0);
                b = String.valueOf(cursor.getInt(1));
                c = String.valueOf(cursor.getInt(2));

                f = new RemoveSalesRecyclerHelper(String.valueOf(i++),a,"",b,"",c);
                quantity += cursor.getInt(1);
                total += cursor.getInt(2);
                arrayList.add(f);

            }


            adapter.notifyDataSetChanged();
            tvQuantity.setText(String.valueOf(quantity));
            tvTotal.setText(String.valueOf(total) + "/-");

        }
        else
        {
            Toast.makeText(FromToSales.this,"No Sales Found!",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}