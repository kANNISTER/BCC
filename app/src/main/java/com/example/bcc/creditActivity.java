package com.example.bcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class creditActivity extends AppCompatActivity {

    private Button createCreditInvoice, viewCredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);


        createCreditInvoice = (Button)findViewById(R.id.btCreditCreditInvoice);
        viewCredit = (Button)findViewById(R.id.btViewCreditInvoice);

        createCreditInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(creditActivity.this, createCreditBill.class);
                startActivity(intent);
            }
        });

        viewCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(creditActivity.this, viewCreditBills.class);
                startActivity(intent);
            }
        });
    }
}