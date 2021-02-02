package com.example.bcc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class invoiceList extends AppCompatActivity {

    private ListView pdfList;
    public ArrayAdapter<String> adapter;
    public ArrayList<String> arrayList;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_list);

        pdfList = (ListView)findViewById(R.id.lvPdfList);
        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        pdfList.setAdapter(adapter);

        String path = getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/Invoices";
        File file = new File(path);
        Search_Dir(file);


        pdfList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) pdfList.getItemAtPosition(position);

                String path = getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/Invoices/" + item;
                File file = new File(path);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
                intent.setDataAndType(uri,"application/*");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


                //Intent intent1 = Intent.createChooser(intent, "Open File");
                startActivity(intent);
            }
        });
    }


    public void Search_Dir(File dir) {
        String pdfPattern = ".pdf";

        File FileList[] = dir.listFiles();

        if (FileList != null) {
            for (int i = 0; i < FileList.length; i++) {

                if (FileList[i].isDirectory()) {
                    Search_Dir(FileList[i]);
                } else {
                    if (FileList[i].getName().endsWith(pdfPattern)){
                        arrayList.add(FileList[i].getName());
                    }adapter.notifyDataSetChanged();
                }
            }
        }
    }
}