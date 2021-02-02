package com.example.bcc;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class showData extends AppCompatActivity {
    private Button btSettlement;
    public Date date;
    private DateFormat dateFormat;
    private String cQnty, cAmount;
    private DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);


        btSettlement = (Button)findViewById(R.id.btSettlement);

        DB = new DBHelper(this);

        Bundle bundle = getIntent().getExtras();
        String newFile, type ;
        newFile = bundle.getString("fName");
        type = bundle.getString("type");

        File file = new File(newFile);

        try {
            Scanner scanner = new Scanner(file);
            int i =0;
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                String[] content = new String[6];
                content = data.split(",");

                addTable(content,i,type);
                i += 1;

            }
            scanner.close();
        }catch (Exception e) {
            String err = String.valueOf(e);
            Toast.makeText(showData.this, err, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        btSettlement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogue();
            }
        });



    }

    private void addTable(String content[], int i, String type)
    {
        TableLayout ll = (TableLayout) findViewById(R.id.displayLinear);

            int tSize;
            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);

            TextView tv1 = new TextView(this);
            TextView tv2 = new TextView(this);
            TextView tv3 = new TextView(this);
            TextView tv4 = new TextView(this);
            TextView tv5 = new TextView(this);
            TextView tv6 = new TextView(this);

            TextView tvSpace1 = new TextView(this);
            TextView tvSpace2 = new TextView(this);
            TextView tvSpace3 = new TextView(this);
            TextView tvSpace4 = new TextView(this);
            TextView tvSpace5 = new TextView(this);

            if(type.contentEquals("B"))
            {
                tSize = 30;
                tvSpace1.setText("     |     ");
                tvSpace2.setText("     |     ");
                tvSpace3.setText("     |     ");
                tvSpace4.setText("     |     ");
            }
            else
            {
                tSize = 30;
                tvSpace1.setText("      |      ");
                tvSpace2.setText("      |      ");
                tvSpace3.setText("      |      ");
                tvSpace4.setText("      |      ");
            }

            tv1.setTextSize(tSize);
            tv2.setTextSize(tSize);
            tv3.setTextSize(tSize);
            tv4.setTextSize(tSize);
            tv5.setTextSize(tSize);

            tvSpace1.setTextSize(tSize);
            tvSpace2.setTextSize(tSize);
            tvSpace3.setTextSize(tSize);
            tvSpace4.setTextSize(tSize);

            tv1.setText(content[0]);
            tv2.setText(content[1]);
            tv3.setText(content[2]);
            tv4.setText(content[3]);
            tv5.setText(content[4]);

            cQnty = content[3];
            cAmount = content[4];



            row.addView(tv1);
            row.addView(tvSpace1);
            row.addView(tv2);
            row.addView(tvSpace2);
            row.addView(tv3);
            row.addView(tvSpace3);
            row.addView(tv4);
            row.addView(tvSpace4);
            row.addView(tv5);
            ll.addView(row, i);

        if(type.contentEquals("B"))
        {
            tv6.setTextSize(tSize);
            tvSpace5.setTextSize(tSize);
            tv6.setText(content[5]);
            tvSpace5.setText("  |  ");
            row.addView(tvSpace5);
            row.addView(tv6);
        }
    }

    private void openDialogue() {

        AlertDialog.Builder builder = new AlertDialog.Builder(showData.this);

        builder.setMessage("Do you want to make Settlement?");

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

               settlement();
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

    private void settlement()
    {
        StringBuilder data = new StringBuilder();
        StringBuilder data1 = new StringBuilder();
        date = new Date();

        dateFormat = new SimpleDateFormat("MMM");
        String fileName = "sales_" + dateFormat.format(date);

        dateFormat = new SimpleDateFormat("yyyy");
        String folderName = "sales_" + dateFormat.format(date);

        String xyz = folderName + "/" + fileName + ".csv";
        File newFile = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM), "/" + xyz);

        if(!newFile.exists())
        {
            newFile.getParentFile().mkdirs();
            data.append("Date,Quantity,Sale");
            try {
                FileOutputStream out = new FileOutputStream(newFile);
                out.write((data.toString()).getBytes());
                out.flush();
                out.close();
            }
            catch (Exception e)
            {
                Toast.makeText(showData.this,String.valueOf(e),Toast.LENGTH_SHORT).show();
            }
        }
        else
        {

        }

        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String x = dateFormat.format(date).replace("/",".");
        data1.append("\n"+x + "," + cQnty + "," + cAmount);

        try {
            Files.write(Paths.get(String.valueOf(newFile)), data1.toString().getBytes(), StandardOpenOption.APPEND);
            if(!DB.addSettlement(Integer.valueOf(cQnty),Integer.valueOf(cAmount)))
            {
                throw new Exception("Error Storing into Database");
            }
            Toast.makeText(showData.this,"Settlement Added Successfully!",Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Toast.makeText(showData.this,String.valueOf(e),Toast.LENGTH_SHORT).show();
        }

    }

}