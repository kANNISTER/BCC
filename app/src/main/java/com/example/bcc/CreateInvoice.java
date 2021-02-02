package com.example.bcc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.canvas.CanvasCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CreateInvoice extends AppCompatActivity {

    private Button id250,id350,id550,id650,id750,id850,id999,id1100,id1250,id1350,id1450,idCreateInvoice;
    private TextView total;
    private int c250 = 0, c350 = 0, c550 = 0,c650 = 0, c750 = 0, c850 = 0 ,c999 = 0,c1100 = 0,c1250 = 0,c1350 = 0,c1450 = 0, totalamt = 0, srCounter = 1, inc=50;
    private Bitmap bmp, scalebmp;
    private Date date;
    public int sr = 90, id = 370, pr = 750, qty = 940, t = 1080, l = 520;;
    private DateFormat dateFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invoice);

        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);


        id250 = (Button)findViewById(R.id.id250);
        id350 = (Button)findViewById(R.id.id350);
        id550 = (Button)findViewById(R.id.id550);
        id650 = (Button)findViewById(R.id.id650);
        id750 = (Button)findViewById(R.id.id750);
        id850 = (Button)findViewById(R.id.id850);
        id999 = (Button)findViewById(R.id.id999);
        id1100 = (Button)findViewById(R.id.id1100);
        id1250 = (Button)findViewById(R.id.id1250);
        id1350 = (Button)findViewById(R.id.id1350);
        id1450 = (Button)findViewById(R.id.id1450);
        idCreateInvoice  = (Button)findViewById(R.id.idCreateInvoice);
        total = (TextView)findViewById(R.id.idtotal);
        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.bccinvoice1);
        scalebmp = Bitmap.createScaledBitmap(bmp,1246,1748,false);


        id250.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               c250 = c250 + 1;
               calculate(250);
            }
        });

        id350.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c350 = c350 + 1;
                calculate(350);
            }
        });

        id550.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c550 = c550 + 1;
                calculate(550);
            }
        });

        id650.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c650 = c650 + 1;
                calculate(650);
            }
        });

        id750.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c750 = c750 + 1;
                calculate(750);
            }
        });

        id850.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c850 = c850 +1;
                calculate(850);
            }
        });

        id999.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c999 = c999 + 1;
                calculate(999);
            }
        });

        id1100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c1100 = c1100 + 1;
                calculate(1100);
            }
        });

        id1250.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c1250 = c1250 +1;
                calculate(1250);

            }
        });

        id1350.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c1350 = c1350 +1;
                calculate(1350);
            }
        });

        id1450.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c1450 = c1450 +1;
                calculate(1450);
            }
        });

        idCreateInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = new Date();
               try {
                    Toast.makeText(CreateInvoice.this,"Creating....",Toast.LENGTH_SHORT).show();
                    createPDF();
                } catch (IOException e) {
                   String err = String.valueOf(e) ;
                   Toast.makeText(CreateInvoice.this,err,Toast.LENGTH_LONG).show();
                   e.printStackTrace();
                }


            }
        });
    }

    private void calculate(int amt)
    {
        totalamt = totalamt + amt;
        String text = ("Rs." + String.valueOf(totalamt) + "/-");
        total.setText(text);

    }

    public void createPDF() throws IOException {
        String b, c, d, f;
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint titlePaint = new Paint();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1246, 1748, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        canvas.drawBitmap(scalebmp, 0, 0, paint);

        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        titlePaint.setTextSize(50);

        canvas.drawText("Invoice", 600, 350, titlePaint); // 1200/2 = 600

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(26f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);


        canvas.drawRect(20, 400, 1246 - 20, 1500, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setStyle(Paint.Style.FILL);

        int a = 450;
        canvas.drawText("Sr.No.", 70, a, paint);
        canvas.drawText("Item Description", 320, a, paint);
        canvas.drawText("Price", 750, a, paint);
        canvas.drawText("Qty.", 940, a, paint);
        canvas.drawText("Total", 1080, a, paint);


        canvas.drawLine(20, 480, 1246 - 20, 480, paint);
        canvas.drawLine(20, 1400, 1246 - 20, 1400, paint);


        int x = 400, y = 1400;
        canvas.drawLine(180, x, 180, y, paint);
        canvas.drawLine(680, x, 680, y, paint);
        canvas.drawLine(880, x, 880, y, paint);
        canvas.drawLine(1030, x, 1030, y + 100, paint);


        /*
        if(c250 != 0)
        {
            canvas.drawText(String.valueOf(srCounter),sr,l,paint);
            canvas.drawText("Apparels",id,l,paint);
            canvas.drawText("250/-",pr,l,paint);
            canvas.drawText(String.valueOf(c250),qty,l,paint);
            canvas.drawText(String.valueOf(250*c250)+"/-",t,l,paint);
            srCounter++;
            l = l + inc;
        }

        if(c350 != 0)
        {
            canvas.drawText(String.valueOf(srCounter),sr,l,paint);
            canvas.drawText("Apparels",id,l,paint);
            canvas.drawText("350/-",pr,l,paint);
            canvas.drawText(String.valueOf(c350),qty,l,paint);
            canvas.drawText(String.valueOf(350*c350)+"/-",t,l,paint);
            srCounter++;
            l = l +inc;
        }

        if(c550 != 0)
        {
            canvas.drawText(String.valueOf(srCounter),sr,l,paint);
            canvas.drawText("Apparels",id,l,paint);
            canvas.drawText("550/-",pr,l,paint);
            canvas.drawText(String.valueOf(c550),qty,l,paint);
            canvas.drawText(String.valueOf(550*c550)+"/-",t,l,paint);
            srCounter++;
            l = l +inc;
        }

        if(c650 != 0)
        {
            canvas.drawText(String.valueOf(srCounter),sr,l,paint);
            canvas.drawText("Apparels",id,l,paint);
            canvas.drawText("650/-",pr,l,paint);
            canvas.drawText(String.valueOf(c650),qty,l,paint);
            canvas.drawText(String.valueOf(650*c650)+"/-",t,l,paint);
            srCounter++;
            l = l +inc;
        }

        if(c750 != 0)
        {
            canvas.drawText(String.valueOf(srCounter),sr,l,paint);
            canvas.drawText("Apparels",id,l,paint);
            canvas.drawText("750/-",pr,l,paint);
            canvas.drawText(String.valueOf(c750),qty,l,paint);
            canvas.drawText(String.valueOf(750*c750)+"/-",t,l,paint);
            srCounter++;
            l = l +inc;
        }

        if(c850 != 0)
        {
            canvas.drawText(String.valueOf(srCounter),sr,l,paint);
            canvas.drawText("Apparels",id,l,paint);
            canvas.drawText("850/-",pr,l,paint);
            canvas.drawText(String.valueOf(c850),qty,l,paint);
            canvas.drawText(String.valueOf(850*c850)+"/-",t,l,paint);
            srCounter++;
            l = l +inc;
        }

        if(c850 != 0)
        {
            canvas.drawText(String.valueOf(srCounter),sr,l,paint);
            canvas.drawText("Apparels",id,l,paint);
            canvas.drawText("850/-",pr,l,paint);
            canvas.drawText(String.valueOf(c850),qty,l,paint);
            canvas.drawText(String.valueOf(850*c850)+"/-",t,l,paint);
            srCounter++;
            l = l +inc;
        }

        if(c999 != 0)
        {
            canvas.drawText(String.valueOf(srCounter),sr,l,paint);
            canvas.drawText("Apparels",id,l,paint);
            canvas.drawText("999/-",pr,l,paint);
            canvas.drawText(String.valueOf(c999),qty,l,paint);
            canvas.drawText(String.valueOf(999*c999)+"/-",t,l,paint);
            srCounter++;
            l = l +inc;
        }

        if(c1100 != 0)
        {
            canvas.drawText(String.valueOf(srCounter),sr,l,paint);
            canvas.drawText("Apparels",id,l,paint);
            canvas.drawText("1100/-",pr,l,paint);
            canvas.drawText(String.valueOf(c1100),qty,l,paint);
            canvas.drawText(String.valueOf(1100*c1100)+"/-",t,l,paint);
            srCounter++;
            l = l +inc;

        }

        if(c1250 != 0)
        {
            canvas.drawText(String.valueOf(srCounter),sr,l,paint);
            canvas.drawText("Apparels",id,l,paint);
            canvas.drawText("1250/-",pr,l,paint);
            canvas.drawText(String.valueOf(c1250),qty,l,paint);
            canvas.drawText(String.valueOf(1250*c1250)+"/-",t,l,paint);
            srCounter++;
            l = l +inc;
        }

        if(c1350 != 0)
        {
            canvas.drawText(String.valueOf(srCounter),sr,l,paint);
            canvas.drawText("Apparels",id,l,paint);
            canvas.drawText("1350/-",pr,l,paint);
            canvas.drawText(String.valueOf(c1350),qty,l,paint);
            canvas.drawText(String.valueOf(1350*c1350)+"/-",t,l,paint);
            srCounter++;
            l = l +inc;
        }

        if(c1350 != 0)
        {
            canvas.drawText(String.valueOf(srCounter),sr,l,paint);
            canvas.drawText("Apparels",id,l,paint);
            canvas.drawText("1350/-",pr,l,paint);
            canvas.drawText(String.valueOf(c1350),qty,l,paint);
            canvas.drawText(String.valueOf(1350*c1350)+"/-",t,l,paint);
            srCounter++;
            l = l +inc;
        }

        if(c1450 != 0)
        {
            canvas.drawText(String.valueOf(srCounter),sr,l,paint);
            canvas.drawText("Apparels",id,l,paint);
            canvas.drawText("1450/-",pr,l,paint);
            canvas.drawText(String.valueOf(c1450),qty,l,paint);
            canvas.drawText(String.valueOf(1450*c1450)+"/-",t,l,paint);
            srCounter++;
            l = l +inc;
        }*/


        DBHelper DB = new DBHelper(this);


        Cursor cursor = DB.getData();

        int count = cursor.getCount();
        if (count != 0) {
            while (cursor.moveToNext()) {
                b = cursor.getString(1);
                c = cursor.getString(2);
                d = cursor.getString(3);
                f = String.valueOf(cursor.getInt(4));

                canvas.drawText(String.valueOf(srCounter),sr,l,paint);
                canvas.drawText(b,id,l,paint);
                canvas.drawText(c+"/-",pr,l,paint);
                canvas.drawText(d,qty,l,paint);
                canvas.drawText(f+"/-",t,l,paint);
                srCounter++;
                l = l +inc;
            }
            dateFormat = new SimpleDateFormat("dd/MM/yy");
            canvas.drawText("Date:  " + dateFormat.format(date), 1246 - 220, 330, paint);

            dateFormat = new SimpleDateFormat("HH:mm:ss");
            canvas.drawText("Time:  " + dateFormat.format(date), 1246 - 220, 360, paint);

            //canvas.drawLine(680,l,1180,l,paint);

            paint.setTextSize(40f);

            String xyz = "Total:             " + String.valueOf(totalamt) + "/-";
            canvas.drawText(xyz, 820, 1460, paint);

            pdfDocument.finishPage(page);

       /* File file = new File(Environment.getExternalStorageDirectory(),"/invoice.pdf");
        Toast.makeText(CreateInvoice.this,"Before",Toast.LENGTH_SHORT).show();
        pdfDocument.writeTo(new FileOutputStream(file));
        Toast.makeText(CreateInvoice.this,"After",Toast.LENGTH_SHORT).show();*/

            File newFile = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM), "/invoice_1234.pdf");
            newFile.getParentFile().mkdirs();
            try {
                FileOutputStream out = new FileOutputStream(newFile);
                pdfDocument.writeTo(out);
                out.flush();
                out.close();
                Log.d("NEWFILE", newFile.getAbsolutePath());
                Toast.makeText(CreateInvoice.this, newFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                String err = String.valueOf(e);
                Toast.makeText(CreateInvoice.this, err, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            /*try{
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(CreateInvoice.this,"PDF Created",Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        }

    }


}