package com.example.bcc;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class createCreditBill extends AppCompatActivity {

    private EditText cName, cPhone;
    private Button btAdd,btClear,btSave,btCustom;
    private TextView tvTotal, tvQuantity;
    private Spinner productSpinner, priceSpinner, qtySpinner;
    private ArrayAdapter<CharSequence> paymentAdapter,priceAdapter,productAdapter,qtyAdapter;
    private DBHelper DB;
    private ProductListAdapter adapter;
    public ArrayList<ListViewRecyclerHelper> arrayList;
    private final int[] srno = {1};
    private ListView lvBill;
    private String product,price,qty;
    private int ntotal = 0, ncnty = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_credit_bill);

        DB = new DBHelper(this);

        lvBill = (ListView) findViewById(R.id.lvCreditBill);


        cName = (EditText)findViewById(R.id.editTextCreditName);
        cPhone = (EditText)findViewById(R.id.etCreditPhone);

        btAdd = (Button)findViewById(R.id.creditAddid);
        btClear = (Button)findViewById(R.id.creditClearid);
        btCustom =  (Button)findViewById(R.id.creditCustomProductId);
        btSave = (Button)findViewById(R.id.creditSaveID);

        tvTotal = (TextView)findViewById(R.id.creditTotalCnt);
        tvQuantity = (TextView)findViewById(R.id.creditQtyCnt);

        //spinner initialization

            priceSpinner = (Spinner)findViewById(R.id.creditPriceSpinner);
            productSpinner = (Spinner)findViewById(R.id.creditProductSpinner);
            qtySpinner = (Spinner)findViewById(R.id.creditQtySpinner);

            priceAdapter = ArrayAdapter.createFromResource(this, R.array.Price, android.R.layout.simple_spinner_item);
            productAdapter = ArrayAdapter.createFromResource(this, R.array.Product, android.R.layout.simple_spinner_item);
            qtyAdapter = ArrayAdapter.createFromResource(this, R.array.Quantity, android.R.layout.simple_spinner_item);

            priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            qtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            priceSpinner.setAdapter(priceAdapter);
            productSpinner.setAdapter(productAdapter);
            qtySpinner.setAdapter(qtyAdapter);

        //end of spinner initialization
        arrayList = new ArrayList<ListViewRecyclerHelper>();
        adapter = new ProductListAdapter(this, R.layout.list_view_recycler,arrayList);
        lvBill.setAdapter(adapter);
        initialize();

        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);


        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddItem();
            }
        });

        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialize();
            }
        });

        btCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCustomProduct();
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


        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cName.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(createCreditBill.this, "Enter Name!", Toast.LENGTH_SHORT).show();
                }
                else if(cPhone.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(createCreditBill.this, "Enter Phone Number!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    save(cName.getText().toString().trim(),cPhone.getText().toString().trim());
                }
            }
        });
    }


    private void initialize()
    {
        DB.truncate();
        arrayList.clear();

        adapter.notifyDataSetChanged();
        srno[0] = 1;
        tvTotal.setText("0/-");
        tvQuantity.setText("0");

        priceSpinner.setSelection(0);
        productSpinner.setSelection(0);
        qtySpinner.setSelection(0);
        cName.setText("");
        cPhone.setText("");
        ntotal = 0;
        ncnty = 0;

    }

    private void AddItem()
    {
        price = String.valueOf(priceSpinner.getSelectedItem());
        product = String.valueOf(productSpinner.getSelectedItem());
        qty = String.valueOf(qtySpinner.getSelectedItem());

        if (!price.contentEquals("--SELECT--") && !product.contentEquals("--SELECT--") && !qty.contentEquals("--SELECT--")) {
            Boolean checkInsertData = DB.insertUserData(srno[0], product, price, qty, (Integer.valueOf(price) * Integer.valueOf(qty)));
            srno[0]++;

            if (!checkInsertData) {
                Toast.makeText(createCreditBill.this, "Error in Inserting Item into DATABASE", Toast.LENGTH_SHORT).show();
            } else {
                String a, b, c, d, e;
                ListViewRecyclerHelper f;
                Cursor cursor = DB.getData();
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

                        arrayList.add(f);
                        setTotalQuantity();

                    }
                    adapter.notifyDataSetChanged();
                }
            }
            qtySpinner.setSelection(0);
            priceSpinner.setSelection(0);
            productSpinner.setSelection(0);

        }
        else
        {
            Toast.makeText(createCreditBill.this,"Please Fill All The Fields!",Toast.LENGTH_SHORT).show();
        }
    }

    private void setTotalQuantity()
    {
        ntotal = 0;
        ncnty = 0;
        String a,b;
        Cursor cursor = DB.getData();
        int count = cursor.getCount();
        if (count != 0) {
            while (cursor.moveToNext()) {
                a = cursor.getString(3);
                b = String.valueOf(cursor.getInt(4));
                ntotal = ntotal + Integer.valueOf(b);
                ncnty = ncnty + Integer.valueOf(a);
                tvTotal.setText(String.valueOf(ntotal)+"/-");
                tvQuantity.setText(String.valueOf(ncnty));
            }
        }
    }

    private void addCustomProduct() {

        final String[] cName = new String[1];
        final String[] cPrice  = new String[1];
        final String[] cQuantity  = new String[1];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Custom Product");
        LayoutInflater inflater = getLayoutInflater();
        final View dialogueView = inflater.inflate(R.layout.custom_product, null);
        builder.setView(dialogueView);
        final Button productAddId = (Button) dialogueView.findViewById(R.id.productAddId);
        final Button productCancelId = (Button) dialogueView.findViewById(R.id.productCancelId);

        final EditText productNameId = (EditText)dialogueView.findViewById(R.id.productNameId);
        final EditText productPriceId = (EditText)dialogueView.findViewById(R.id.productPriceId);
        final EditText productQuantityId = (EditText)dialogueView.findViewById(R.id.productQuantityId);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        productAddId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!productNameId.getText().toString().isEmpty() && !productPriceId.getText().toString().isEmpty() && !productQuantityId.getText().toString().isEmpty()) {
                    cName[0] = productNameId.getText().toString();
                    cPrice[0] = productPriceId.getText().toString();
                    cQuantity[0] = productQuantityId.getText().toString();
                    AddCustomItem(cName[0],cPrice[0],cQuantity[0]);
                    alertDialog.cancel();
                }
            }
        });
        productCancelId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.cancel();
            }
        });
    }

    private void AddCustomItem(String product, String price, String qty)
    {

        Boolean checkInsertData = DB.insertUserData(srno[0], product, price, qty, (Integer.valueOf(price) * Integer.valueOf(qty)));
        srno[0]++;

        if (!checkInsertData) {
            Toast.makeText(createCreditBill.this, "Error in Inserting Item into DATABASE", Toast.LENGTH_SHORT).show();
        } else {
            String a, b, c, d, e;
            ListViewRecyclerHelper f;
            Cursor cursor = DB.getData();
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
                    //f = a +"                "+ b;

                    //f += StringUtils.repeat(" ",27);

                    //f += c +"                "+ d +"                "+ e;
                    arrayList.add(f);
                    setTotalQuantity();

                }
                adapter.notifyDataSetChanged();
            }
        }

    }


    private void openDialogue(int i, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(createCreditBill.this);

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

                if(DB.deleteUserData(i)) {
                    Toast.makeText(createCreditBill.this, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                    setTotalQuantity();
                    arrayList.remove(position);
                    adapter.notifyDataSetChanged();
                }
                else
                    Toast.makeText(createCreditBill.this, "Delete Unsuccessful!", Toast.LENGTH_SHORT).show();
                dialog.cancel();

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,
                                int which)
            {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void save(String name, String phoneNo)
    {
        String tableName = name + "_" + phoneNo;
        DB.createCreditUserBillTable(tableName);
        DB.addCredit(cName.getText().toString().trim(),cPhone.getText().toString().trim() ,ntotal,tableName);
        initialize();
    }

}