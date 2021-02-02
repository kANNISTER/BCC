package com.example.bcc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
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

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
;import static java.util.logging.Logger.global;

public class Billing extends AppCompatActivity{

    private Spinner productSpinner, priceSpinner, paymentSpinner, qtySpinner;
    private ArrayAdapter<CharSequence> paymentAdapter,priceAdapter,productAdapter,qtyAdapter;



    //public ArrayAdapter<String> adapter;
    //public ArrayList<String> arrayList;
    private ProductListAdapter adapter;

    public ArrayList<ListViewRecyclerHelper> arrayList;


    private Button addID,printId, clearId,customProductId;
    private String product,price,payment,qty;
    private ListView lvBill;
    private Bitmap bmp, scalebmp;
    private Date date;
    public int sr = 110, id = 430, pr = 930, qtny = 755, t = 1128, l, flag = 0, cQnty = 0;
    private DateFormat dateFormat;
    private DBHelper DB;
    private final int[] srno = {1};
    private EditText nameId, phoneId;
    private String GSTno = "GSTIN: 27AEZPR3374K2ZG";
    private TextView totalCnt, qtyCnt, TVInvoiceId;

    public MainActivity mainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        lvBill = (ListView) findViewById(R.id.lvBill);

        DB = new DBHelper(this);
        Cursor cursor = DB.getData();

        mainActivity = new MainActivity();

        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.bccinvoice1);
        scalebmp = Bitmap.createScaledBitmap(bmp,1246,1748,false);


        addID = (Button)findViewById(R.id.addid);
        printId = (Button)findViewById(R.id.printid);
        clearId = (Button) findViewById(R.id.clearid);
        nameId = (EditText)findViewById(R.id.editTextTextPersonName2);
        phoneId = (EditText)findViewById(R.id.etPhoneId);
        totalCnt = (TextView)findViewById(R.id.totalCnt);
        qtyCnt = (TextView)findViewById(R.id.qtyCnt);
        TVInvoiceId = (TextView)findViewById(R.id.TVInvoiceId);

        //arrayList = new ArrayList<String>();
        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);

        arrayList = new ArrayList<ListViewRecyclerHelper>();
        adapter = new ProductListAdapter(this, R.layout.list_view_recycler,arrayList);

        lvBill.setAdapter(adapter);



        paymentSpinner = (Spinner)findViewById(R.id.paymentSpinner);
        priceSpinner = (Spinner)findViewById(R.id.priceSpinner);
        productSpinner = (Spinner)findViewById(R.id.productSpinner);
        qtySpinner = (Spinner)findViewById(R.id.qtySpinner);

        paymentAdapter = ArrayAdapter.createFromResource(this, R.array.Payment, android.R.layout.simple_spinner_item);
        priceAdapter = ArrayAdapter.createFromResource(this, R.array.Price, android.R.layout.simple_spinner_item);
        productAdapter = ArrayAdapter.createFromResource(this, R.array.Product, android.R.layout.simple_spinner_item);
        qtyAdapter = ArrayAdapter.createFromResource(this, R.array.Quantity, android.R.layout.simple_spinner_item);

        paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        paymentSpinner.setAdapter(paymentAdapter);
        priceSpinner.setAdapter(priceAdapter);
        productSpinner.setAdapter(productAdapter);
        qtySpinner.setAdapter(qtyAdapter);



        customProductId = (Button)findViewById(R.id.customProductId);

        tabMob();
        customProductId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCustomProduct();
            }
        });


        initialize();
        addID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddItem();
            }

        });
        printId.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                payment = String.valueOf(paymentSpinner.getSelectedItem());

                if(payment.contentEquals("--SELECT--"))
                {
                   Toast.makeText(Billing.this,"Select Payment Type",Toast.LENGTH_SHORT).show();
                }
                else if(DB.getData().getCount() == 0){
                    Toast.makeText(Billing.this,"No Products Found!",Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        createPDF(payment);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    initialize();


                   /* paymentSpinner.setSelection(1);
                    priceSpinner.setSelection(0);
                    productSpinner.setSelection(0);
                    qtySpinner.setSelection(0);
                    nameId.setText("");
                    phoneId.setText("");*/
                }


            }
        });

        clearId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialize();
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

    }




    public void createPDF(String paymentType) throws IOException {
        int total = 0;
        cQnty = 0;
        l = 570;
        date = new Date();
        int srCounter = 1, inc=50;
        String b, c, d, f, cName, cPhone, gstMsg = "GST is inclusive of all prices";
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint titlePaint = new Paint();
        File newFile;

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1246, 1748, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        canvas.drawBitmap(scalebmp, 0, 0, paint);

        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        titlePaint.setTextSize(50);



        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(30f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);


        canvas.drawRect(20, 450, 1246 - 20, 1500, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setStyle(Paint.Style.FILL);

        cName = "Customer Name: " + nameId.getText().toString().trim();
        cPhone = "Customer Phone No.: " + phoneId.getText().toString().trim();

        canvas.drawText(cName,40,330,paint);
        canvas.drawText(cPhone,40,370,paint);
        if (!paymentType.contentEquals("Shop UPI"))
        {
            canvas.drawText("Payment Type: " + paymentType,40,410,paint);
        }
        else
        {
            canvas.drawText("Payment Type: " + "UPI",40,410,paint);
        }

        dateFormat = new SimpleDateFormat("dd/MM/yy");
        canvas.drawText("Date:  " + dateFormat.format(date), 1246 - 240, 370, paint);

        dateFormat = new SimpleDateFormat("HH:mm:ss");
        canvas.drawText("Time:  " + dateFormat.format(date), 1246 - 240, 410, paint);

        int a = 500;
        canvas.drawText("Sr.No.", 70, a, paint);
        canvas.drawText("Item Description", 320, a, paint);
        canvas.drawText("Qty.", 740, a, paint);
        canvas.drawText("Price", 890, a, paint);
        canvas.drawText("Amount", 1080, a, paint);


        canvas.drawLine(20, 530, 1246 - 20, 530, paint);
        canvas.drawLine(20, 1400, 1246 - 20, 1400, paint);


        int x = 450, y = 1400;
        canvas.drawLine(180, x, 180, y, paint);
        canvas.drawLine(680, x, 680, y+100, paint);
        canvas.drawLine(830, x, 830, y+100, paint);
        canvas.drawLine(1030, x, 1030, y + 100, paint);

        DBHelper DB = new DBHelper(this);

        paint.setTextAlign(Paint.Align.CENTER);
        Cursor cursor = DB.getData();

        int count = cursor.getCount();
        if (count != 0) {
            while (cursor.moveToNext()) {
                b = cursor.getString(1);
                c = cursor.getString(2);
                d = cursor.getString(3);
                f = String.valueOf(cursor.getInt(4));

                canvas.drawText(String.valueOf(srCounter), sr, l, paint);
                canvas.drawText(b, id, l, paint);
                canvas.drawText(d, qtny, l, paint);
                canvas.drawText(c + "/-", pr, l, paint);
                canvas.drawText(f + "/-", t, l, paint);
                srCounter++;
                l = l + inc;
                cQnty = cQnty + Integer.valueOf(cursor.getString(3));
                total = total + cursor.getInt(4);

            }
        }
        Boolean flag;

        newFile = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM), "/invoice_1234.pdf");

        if(paymentType.contentEquals("Card") || paymentType.contentEquals("Shop UPI"))
        {
            if(!DB.addAllSalesTable(paymentType,cQnty ,total,String.valueOf(mainActivity.invoiceNo)))                                                            //add data in sales table
            {
                Toast.makeText(Billing.this,"Error Storing In All Sales",Toast.LENGTH_SHORT).show();
                flag = false;
            }
            else
            {
                flag = true;
            }
        }
        else
        {
            if(!DB.addAllSalesTable(paymentType,cQnty ,total,"0000"))                                                             //add data in sales table
            {
                Toast.makeText(Billing.this,"Error Storing In All Sales",Toast.LENGTH_SHORT).show();
                flag = false;
            }
            else
            {
                flag = true;
            }
        }

        if(flag)                                                             //add data in sales table
        {
            if (paymentType.contentEquals("Card") || paymentType.contentEquals("Shop UPI")) {


                    newFile = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM), "/Invoices/invoice_" + String.valueOf(mainActivity.invoiceNo) + ".pdf");
                    paint.setTextAlign(Paint.Align.LEFT);
                    canvas.drawText(GSTno, 20, 1535, paint);
                    canvas.drawText("Invoice No: " + String.valueOf(mainActivity.invoiceNo), 1246 - 240, 330, paint);

                    paint.setTextAlign(Paint.Align.RIGHT);
                    paint.setTextSize(30f);
                    canvas.drawText(gstMsg, 1246 - 20, 1748 - 20, paint);

                    paint.setTextAlign(Paint.Align.CENTER);
                    mainActivity.invoiceNo++;
            }
            else {
                newFile = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM), "/invoice_1234.pdf");
            }
        }

            paint.setTextSize(40f);
            paint.setTextAlign(Paint.Align.LEFT);
            //canvas.drawText("Invoice", 40, 330,paint);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(String.valueOf(cQnty),qtny,1460,paint);
            canvas.drawText("Total:", pr, 1460, paint);
            canvas.drawText(String.valueOf(total) + "/-", t, 1460, paint);

            pdfDocument.finishPage(page);


            newFile.getParentFile().mkdirs();
            try {
                FileOutputStream out = new FileOutputStream(newFile);
                pdfDocument.writeTo(out);
                out.flush();
                out.close();
                Log.d("NEWFILE", newFile.getAbsolutePath());
                printPDF(newFile.getAbsolutePath());
                //Toast.makeText(Billing.this, newFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
            }
            catch (IOException e) {
                String err = String.valueOf(e);
                Toast.makeText(Billing.this, err, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }


    }

    private void initialize()
    {
        DB.truncate();
        arrayList.clear();

        adapter.notifyDataSetChanged();
        srno[0] = 1;
        totalCnt.setText("0/-");
        qtyCnt.setText("0");
        TVInvoiceId.setText(String.valueOf(mainActivity.invoiceNo));

        paymentSpinner.setSelection(1);
        priceSpinner.setSelection(0);
        productSpinner.setSelection(0);
        qtySpinner.setSelection(0);
        nameId.setText("");
        phoneId.setText("");
    }

    private void openDialogue(int i, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Billing.this);

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
                                    Toast.makeText(Billing.this, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                                    setTotalQuantity();
                                    arrayList.remove(position);
                                    adapter.notifyDataSetChanged();
                                }
                                else
                                    Toast.makeText(Billing.this, "Delete Unsuccessful!", Toast.LENGTH_SHORT).show();
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

    private void printPDF(String path)
    {
        PrintManager printManager = (PrintManager)getSystemService(Context.PRINT_SERVICE);
        try {
            PdfDocumentAdapter pdfDocumentAdapter = new PdfDocumentAdapter(Billing.this, path);
            printManager.print("INVOICE", pdfDocumentAdapter, new PrintAttributes.Builder().setMediaSize(PrintAttributes.MediaSize.ISO_A5).build());
        }catch (Exception e)
        {
            String err = String.valueOf(e);
            Toast.makeText(Billing.this, err, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void setTotalQuantity()
    {
        String a,b;
        int ntotal = 0, ncnty = 0;
        Cursor cursor = DB.getData();
        int count = cursor.getCount();
        if (count != 0) {
            while (cursor.moveToNext()) {
                a = cursor.getString(3);
                b = String.valueOf(cursor.getInt(4));
                ntotal = ntotal + Integer.valueOf(b);
                ncnty = ncnty + Integer.valueOf(a);
                totalCnt.setText(String.valueOf(ntotal)+"/-");
                qtyCnt.setText(String.valueOf(ncnty));
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

    private void AddItem()
    {
        price = String.valueOf(priceSpinner.getSelectedItem());
        product = String.valueOf(productSpinner.getSelectedItem());
        qty = String.valueOf(qtySpinner.getSelectedItem());

        if (!price.contentEquals("--SELECT--") && !product.contentEquals("--SELECT--") && !qty.contentEquals("--SELECT--")) {
            Boolean checkInsertData = DB.insertUserData(srno[0], product, price, qty, (Integer.valueOf(price) * Integer.valueOf(qty)));
            srno[0]++;

            if (!checkInsertData) {
                Toast.makeText(Billing.this, "Error in Inserting Item into DATABASE", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(Billing.this,"Please Fill All The Fields!",Toast.LENGTH_SHORT).show();
        }
    }


    private void AddCustomItem(String product, String price, String qty)
    {

            Boolean checkInsertData = DB.insertUserData(srno[0], product, price, qty, (Integer.valueOf(price) * Integer.valueOf(qty)));
            srno[0]++;

            if (!checkInsertData) {
                Toast.makeText(Billing.this, "Error in Inserting Item into DATABASE", Toast.LENGTH_SHORT).show();
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

    private boolean checkOrientation()
    {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return true;
        } else {
            return false;
        }
    }

    private void tabMob()
    {

        /*if(manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE){
            manager.getPhoneType()
        }else{
            return "Mobile";
        }*/
    }



}







