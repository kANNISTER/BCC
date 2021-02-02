 package com.example.bcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.os.Environment.*;

public class MainActivity extends AppCompatActivity {

    private Button createInvoice, getTodaySales, setInvoiceNo, getTodayCardUpiSale, btGetInvoiceList, btRemoveSale, btFromToSale, btCredit;
    public Date date;
    private DateFormat dateFormat;
    private int n = -1;
    public static int invoiceNo;
    private DatePickerDialog.OnDateSetListener mDateSetListener1,mDateSetListener2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getInvoiceNo();
        createInvoice = (Button)findViewById(R.id.createInvoiceID);
        getTodaySales = (Button)findViewById(R.id.getTodaySalesId);
        setInvoiceNo = (Button)findViewById(R.id.setInvoiceId);
        getTodayCardUpiSale = (Button)findViewById(R.id.getTodayCardUPISales);
        btGetInvoiceList = (Button)findViewById(R.id.btOpenPdfList);
        btRemoveSale = (Button)findViewById(R.id.btRemoveSale);
        btFromToSale = (Button)findViewById(R.id.btFromToSale);
        btCredit = (Button)findViewById(R.id.btCredit);


        btCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, creditActivity.class);
                startActivity(intent);
            }
        });


        createInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Billing.class);
                startActivity(intent);
            }
        });

        getTodaySales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                getTodaySale();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                gotoApplicationSettings();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        setInvoiceNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInvoiceNo();
                //Toast.makeText(MainActivity.this,"0",Toast.LENGTH_SHORT).show();

            }
        });

        getTodayCardUpiSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getTodayCardUpisales();
                }catch (Exception e)
                {
                    Toast.makeText(MainActivity.this,String.valueOf(e),Toast.LENGTH_SHORT).show();
                }
            }
        });

        btGetInvoiceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPdfList();
            }
        });

        btRemoveSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRemoveSales();

            }
        });

        btFromToSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FromToSale();
            }
        });

    }
    

    private void gotoApplicationSettings() {

        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);

    }

    public void getTodaySale()
    {
        String b,c,d,f,g,x;
        int total = 0, cQnty = 0;
        StringBuilder data = new StringBuilder();

        date = new Date();
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        data.append("Date,Time,Payment Type,Quantity,Sale");

        DBHelper DB = new DBHelper(this);
        Cursor cursor = DB.retrieveAllSalesTable(dateFormat.format(date));

        int count = cursor.getCount();
        if (count != 0) {
            while (cursor.moveToNext()) {
                b = cursor.getString(0).toString();                     //date
                x = b.replace("/",".");
                c = cursor.getString(1);                                //time
                d = cursor.getString(2);
                f = String.valueOf(cursor.getInt(3));                   //quantity
                g = String.valueOf(cursor.getInt(4));                   //sale

                data.append("\n" + x + "," + c + "," + d + "," + f + "," + g);
                total = total + cursor.getInt(4);
                cQnty = cQnty + cursor.getInt(3);
            }
            data.append("\n" + " " + "," + " " + "," + " " + "," + " " + "," + " ");
            data.append("\n" + "," + ",Total: ,"+ String.valueOf(cQnty)+"," + String.valueOf(total));


            dateFormat = new SimpleDateFormat("yyyy/MM/dd");

            x = dateFormat.format(date).replace("/","-");
            String fName = "sales_" + x + ".csv";

                File newFile = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM), "/" + fName);
                newFile.getParentFile().mkdirs();
                try {

                        FileOutputStream out = new FileOutputStream(newFile);
                        out.write((data.toString()).getBytes());
                        out.flush();
                        out.close();

                        Log.d("NEWFILE", newFile.getAbsolutePath());
                        //Toast.makeText(MainActivity.this, newFile.getAbsolutePath(), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(MainActivity.this, showData.class);
                        intent.putExtra("fName", String.valueOf(newFile));
                        intent.putExtra("type","A");
                        startActivity(intent);

                       /* Uri apkURI = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", newFile);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setDataAndType(apkURI, "application/*");
                        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, apkURI);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);*/


                } catch (FileNotFoundException e) {
                    String err = String.valueOf(e);
                    Toast.makeText(MainActivity.this, err, Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                } catch (Exception e) {
                    String err = String.valueOf(e);
                    Toast.makeText(MainActivity.this, err, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(MainActivity.this, "No Sales Today", Toast.LENGTH_LONG).show();
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1 && (grantResults.length>0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            getTodaySale();
        }
    }

    private void setInvoiceNo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Invoice Number");
        LayoutInflater inflater = getLayoutInflater();
        final View dialogueView = inflater.inflate(R.layout.set_invoice_dialogue, null);
        builder.setView(dialogueView);
        final Button okId = (Button) dialogueView.findViewById(R.id.buttonSubmit);
        final Button cancelId = (Button) dialogueView.findViewById(R.id.buttonCancel);
        final EditText invoiceNoId = (EditText)dialogueView.findViewById(R.id.invoiceNoId);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        okId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                n = Integer.valueOf(invoiceNoId.getText().toString().trim());
                invoiceNo = n;
                Toast.makeText(MainActivity.this, "Invoice Number Set to: " + String.valueOf(invoiceNo),Toast.LENGTH_SHORT).show();
                alertDialog.cancel();
            }
        });
        cancelId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                n=-1;
                alertDialog.cancel();
            }
        });
    }


    private void getTodayCardUpisales()
    {
        String b,c,d,f,g,x,inv;
        int total = 0, cQnty = 0;
        StringBuilder data = new StringBuilder();

        data.append("INVOICE,Date,Time,Payment Type,Quantity,Sale");

        date = new Date();
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        DBHelper DB = new DBHelper(this);
        Cursor cursor = DB.retrieveCardUPISalesTable(dateFormat.format(date));

        int count = cursor.getCount();
        if (count != 0) {
            while (cursor.moveToNext()) {
                inv = cursor.getString(0).toString();                   //invoice
                b = cursor.getString(1).toString();                     //date
                x = b.replace("/",".");
                c = cursor.getString(2);                                //time
                d = cursor.getString(3);
                f = String.valueOf(cursor.getInt(4));                   //quantity
                g = String.valueOf(cursor.getInt(5));                   //sale

                data.append("\n" + inv + "," + x + "," + c + "," + d + "," + f + "," + g);
                total = total + cursor.getInt(5);
                cQnty = cQnty + cursor.getInt(4);
            }
            data.append("\n" + " " + "," + " " + "," + " " + "," + " " + "," + " " + "," + " ");
            data.append("\n" + "," + "," + ",Total: ,"+ String.valueOf(cQnty)+"," + String.valueOf(total));

            dateFormat = new SimpleDateFormat("dd/MM/yy");

            x = dateFormat.format(date).replace("/","-");
            String fName = "cardUpiSale_" + x + ".csv";

            File newFile = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM), "/" + fName);
            newFile.getParentFile().mkdirs();
            try {

                    FileOutputStream out = new FileOutputStream(newFile);
                    out.write((data.toString()).getBytes());
                    out.flush();
                    out.close();

                    Log.d("NEWFILE", newFile.getAbsolutePath());
                    //Toast.makeText(MainActivity.this, newFile.getAbsolutePath(), Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(MainActivity.this, showData.class);
                    intent.putExtra("fName", String.valueOf(newFile));
                    intent.putExtra("type","B");
                    startActivity(intent);

                       /* Uri apkURI = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", newFile);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setDataAndType(apkURI, "application/*");
                        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, apkURI);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);*/


            } catch (FileNotFoundException e) {
                String err = String.valueOf(e);
                Toast.makeText(MainActivity.this, err, Toast.LENGTH_LONG).show();
                e.printStackTrace();

            } catch (Exception e) {
                String err = String.valueOf(e);
                Toast.makeText(MainActivity.this, err, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(MainActivity.this, "No CARD/UPI Sales Today", Toast.LENGTH_LONG).show();
        }
    }

    private void getInvoiceNo(){

        try {
            DBHelper DB = new DBHelper(this);
            Cursor cursor = DB.retrieveCardUPISalesTable();
            int count = cursor.getCount();
            if (count != 0) {
                cursor.move(count);
                invoiceNo = (Integer.valueOf(cursor.getString(0))) + 1;
            }
        }catch (Exception e)
        {
            Toast.makeText(MainActivity.this,String.valueOf(e),Toast.LENGTH_SHORT).show();
        }

    }

    private void openPdfList()
    {
        Intent intent = new Intent(MainActivity.this, invoiceList.class);
        startActivity(intent);
    }

    private void openRemoveSales()
    {
        Intent intent = new Intent(MainActivity.this, removeSale.class);
        startActivity(intent);
    }

    private void FromToSale()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Particular Sale");
        LayoutInflater inflater = getLayoutInflater();
        final View dialogueView = inflater.inflate(R.layout.from_date_to_date, null);
        builder.setView(dialogueView);
        final Button getSale = (Button) dialogueView.findViewById(R.id.btGetSale);
        final Button cancelId = (Button) dialogueView.findViewById(R.id.btCancel);
        final TextView fromDate = (TextView)dialogueView.findViewById(R.id.fromDate);
        final TextView toDate = (TextView)dialogueView.findViewById(R.id.toDate);
        final AlertDialog alertDialog = builder.create();

        alertDialog.show();

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day= calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this,
                                                                       android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                                                        mDateSetListener1,
                                                                         year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });
        mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                String d,m;
                if(String.valueOf(month+1).length() == 1)
                {
                    m = "0" + String.valueOf(month+1);
                }
                else {
                    m = String.valueOf(month+1);
                }

                if(String.valueOf(dayOfMonth).length() == 1)
                {
                    d = "0" + String.valueOf(dayOfMonth);
                }
                else {
                    d = String.valueOf(dayOfMonth);
                }
                fromDate.setText(String.valueOf(year) + "/" + m + "/" + d);
            }
        };

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day= calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener2,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String d,m;
                if(String.valueOf(month+1).length() == 1)
                {
                    m = "0" + String.valueOf(month+1);
                }
                else {
                    m = String.valueOf(month+1);
                }

                if(String.valueOf(dayOfMonth).length() == 1)
                {
                    d = "0" + String.valueOf(dayOfMonth);
                }
                else {
                    d = String.valueOf(dayOfMonth);
                }
                toDate.setText(String.valueOf(year) + "/" +m + "/" + d);
            }
        };
        getSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toDate.getText().toString().contains("/") && fromDate.getText().toString().contains("/")) {
                    Intent intent = new Intent(MainActivity.this, FromToSales.class);
                    intent.putExtra("fDate", fromDate.getText().toString());
                    intent.putExtra("tDate", toDate.getText().toString());
                    startActivity(intent);
                }
                alertDialog.cancel();
            }
        });
        cancelId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.cancel();
            }
        });
    }



}