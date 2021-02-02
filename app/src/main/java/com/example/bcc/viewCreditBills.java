package com.example.bcc;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class viewCreditBills extends AppCompatActivity {

    private Button creditPrint, payInstallments;
    private ListView lvBill, lvPaymentHistory;
    private TextView remainingAmount;
    private Spinner creditTableName;
    private DBHelper DB;

    private ProductListAdapter adapter;
    public ArrayList<ListViewRecyclerHelper> arrayList;

    public ProductListAdapter adapter2;
    public ArrayList<ListViewRecyclerHelper> arrayList2;

    private int installmentAmount, paid = 0, total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_credit_bills);

        DB = new DBHelper(this);

        creditPrint = (Button)findViewById(R.id.btCreditPrint);
        payInstallments = (Button)findViewById(R.id.btPayInstallment);

        lvBill = (ListView)findViewById(R.id.lvCreditBillShow);
        lvPaymentHistory = (ListView)findViewById(R.id.lvCreditHistoryShow);

        remainingAmount = (TextView) findViewById(R.id.tvCreditRemainingAmount);

        creditTableName = (Spinner)findViewById(R.id.creditTableNameSpinner);

        arrayList = new ArrayList<ListViewRecyclerHelper>();
        adapter = new ProductListAdapter(this, R.layout.list_view_recycler,arrayList);
        lvBill.setAdapter(adapter);

        arrayList2 = new ArrayList<ListViewRecyclerHelper>();
        adapter2 = new ProductListAdapter(this, R.layout.list_view_recycler,arrayList2);
        lvPaymentHistory.setAdapter(adapter2);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        creditTableName.setAdapter(spinnerAdapter);
        spinnerAdapter.add("--SELECT--");
        Cursor cursor = DB.getCreditTableName();

        int count = cursor.getCount();
        if(count != 0)
        {
            while (cursor.moveToNext())
            {
                spinnerAdapter.add(cursor.getString(0));
            }
            spinnerAdapter.notifyDataSetChanged();
        }

        creditPrint.setEnabled(false);
        payInstallments.setEnabled(false);

        creditTableName.setSelection(0);

        creditTableName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position !=0)
                {
                    initialize();
                String tName = (String) creditTableName.getSelectedItem();
                setListViews(tName);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        payInstallments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogue((String) creditTableName.getSelectedItem());
            }
        });

        lvBill.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewRecyclerHelper product = (ListViewRecyclerHelper) lvBill.getItemAtPosition(position);
                int pos = Integer.valueOf(product.getSrno());
                openDialogue(pos,position);
                return false;
            }
        });

        lvPaymentHistory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewRecyclerHelper product = (ListViewRecyclerHelper) lvPaymentHistory.getItemAtPosition(position);

                String date = product.getProduct();
                String time = product.getQuantity();

                openDialogue(date,time,position);
                return false;
            }
        });


    }

    private void setListViews(String tName)
    {
        initialize();
        String a, b, c, d, e;
        ListViewRecyclerHelper f;
        Cursor cursor = DB.getCreditData(tName);
        int count = cursor.getCount();
        if (count != 0) {
            arrayList.clear();
            while (cursor.moveToNext()) {
                a = String.valueOf(cursor.getInt(0));
                b = cursor.getString(1);
                c = cursor.getString(2);
                d = cursor.getString(3);
                e = String.valueOf(cursor.getInt(4));

                f = new ListViewRecyclerHelper(a,b,c,d,e);
                total = total + cursor.getInt(4);
                arrayList.add(f);
            }
            adapter.notifyDataSetChanged();
        }

        try {
            cursor = DB.getCreditHistory(tName);
            count = cursor.getCount();
            ListViewRecyclerHelper info;

            int i = 1;
            if (count != 0) {
                arrayList2.clear();
                while (cursor.moveToNext()) {
                    a = cursor.getString(0);
                    b = cursor.getString(1);
                    c = cursor.getString(2);

                    info = new ListViewRecyclerHelper(String.valueOf(i),a,b,"",c+"/-");
                    i++;
                    arrayList2.add(info);
                    paid = paid + Integer.valueOf(c);
                }
                adapter2.notifyDataSetChanged();
            }

            remainingAmount.setText("Remaining Amount = " + String.valueOf(total) + " - " + String.valueOf(paid) + " = " + String.valueOf(total - paid));
        }
        catch(Exception ex)
        {
            Toast.makeText(viewCreditBills.this,String.valueOf(ex),Toast.LENGTH_SHORT).show();
        }
        if((total - paid) == 0)
        {
            creditPrint.setEnabled(true);
            payInstallments.setEnabled(false);
        }
        else
        {
            creditPrint.setEnabled(false);
            payInstallments.setEnabled(true);
        }

    }


    private void openDialogue(String tName)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pay Installment");
        LayoutInflater inflater = getLayoutInflater();
        final View dialogueView = inflater.inflate(R.layout.set_invoice_dialogue, null);
        builder.setView(dialogueView);
        final Button okId = (Button) dialogueView.findViewById(R.id.buttonSubmit);
        final Button cancelId = (Button) dialogueView.findViewById(R.id.buttonCancel);
        final EditText installAmt = (EditText)dialogueView.findViewById(R.id.invoiceNoId);
        final AlertDialog alertDialog = builder.create();
        installAmt.setHint("Enter Amount");
        alertDialog.show();
        okId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                installmentAmount = Integer.valueOf(installAmt.getText().toString().trim());

                if(!(installmentAmount > (total - paid))) {
                    initialize();
                    DB.addCreditHistory(tName, installmentAmount);
                    setListViews(tName);
                    alertDialog.cancel();
                }
                else
                {
                    Toast.makeText(viewCreditBills.this,"Enter Valid Amount!",Toast.LENGTH_SHORT).show();
                    alertDialog.cancel();
                }
            }
        });
        cancelId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
    }

    private void openDialogue(int i, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(viewCreditBills.this);

        builder.setMessage("Do you want to Delete Product " + String.valueOf(i).toString() +"?");

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

                if(DB.deleteCreditData(i,(String)creditTableName.getSelectedItem())) {
                    Toast.makeText(viewCreditBills.this, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                    arrayList.remove(position);
                    adapter.notifyDataSetChanged();
                    setListViews((String) creditTableName.getSelectedItem());
                }
                else
                    Toast.makeText(viewCreditBills.this, "Delete Unsuccessful!", Toast.LENGTH_SHORT).show();
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


    private void openDialogue(String iDate, String iTime, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(viewCreditBills.this);

        builder.setMessage("Do you want to Delete Installment " + String.valueOf(position+1).toString() +"?");

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

                if(DB.deleteCreditHistory(iDate,iTime)) {
                    Toast.makeText(viewCreditBills.this, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                    arrayList2.remove(position);
                    adapter2.notifyDataSetChanged();
                    setListViews((String) creditTableName.getSelectedItem());
                }
                else
                    Toast.makeText(viewCreditBills.this, "Delete Unsuccessful!", Toast.LENGTH_SHORT).show();
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



    private void initialize()
    {
        arrayList.clear();
        adapter.notifyDataSetChanged();
        arrayList2.clear();
        adapter2.notifyDataSetChanged();
        total = 0;
        paid = 0;
    }
}