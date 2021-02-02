package com.example.bcc;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class removeSale extends AppCompatActivity {

    private ListView lvRemoveSale;
    private RemoveSalesAdapter adapter;
    public ArrayList<RemoveSalesRecyclerHelper> arrayList;
    private DBHelper DB;
    private Date date;
    private DateFormat dateFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_sale);

        lvRemoveSale = (ListView)findViewById(R.id.lvRemoveSale);

        arrayList = new ArrayList<RemoveSalesRecyclerHelper>();
        adapter = new RemoveSalesAdapter(this, R.layout.list_view_recycler_remove_sales,arrayList);

        lvRemoveSale.setAdapter(adapter);
        DB = new DBHelper(this);
        try{
            showSales();
        }catch (Exception e)
        {
            String ex = String.valueOf(e);
            Toast.makeText(removeSale.this,ex,Toast.LENGTH_SHORT).show();
        }

        lvRemoveSale.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                RemoveSalesRecyclerHelper product = (RemoveSalesRecyclerHelper) lvRemoveSale.getItemAtPosition(position);
                String pos = product.getSrno();
                String dt = product.getDate();
                String tm = product.getTime();
                openDialogue(dt,tm,pos);
                return false;
            }
        });

    }

    private void showSales()
    {
        date = new Date();
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        int i = 1;
        arrayList.clear();
        String a, b, c, d, e;
        RemoveSalesRecyclerHelper f;
        Cursor cursor = DB.retrieveAllSalesTable1(dateFormat.format(date));
        int count = cursor.getCount();
        if (count != 0) {
            arrayList.clear();
            while (cursor.moveToNext()) {
                a = cursor.getString(0);
                b = cursor.getString(1);
                c = cursor.getString(2);
                d = String.valueOf(cursor.getInt(3));
                e = String.valueOf(cursor.getInt(4));

                f = new RemoveSalesRecyclerHelper(String.valueOf(i++) + "  " + cursor.getString(5),a,b,c,d,e);
                arrayList.add(f);

            }
            adapter.notifyDataSetChanged();
        }
    }

    private void openDialogue(String d, String t, String pos) {

        AlertDialog.Builder builder = new AlertDialog.Builder(removeSale.this);

        builder.setMessage("Do you want to Delete Product " + pos +"?");

        builder.setTitle("Alert !");

        // Set Cancelable false
        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                // When the user click yes button
                // then app will close

                if(DB.deleteAllSales(d,t)) {
                    Toast.makeText(removeSale.this, "Deleted Successfully!", Toast.LENGTH_SHORT).show();

                   showSales();
                }
                else
                    Toast.makeText(removeSale.this, "Delete Unsuccessful!", Toast.LENGTH_SHORT).show();
                dialog.cancel();

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

}