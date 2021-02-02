package com.example.bcc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {

    public Date date;
    private DateFormat dateFormat;
    public DBHelper(Context context) {
        super(context, "BCC.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {

        DB.execSQL("create Table temporaryBill(srno INTEGER, product TEXT,price TEXT,quantity TEXT,total INTEGER)");
        //DB.execSQL("create Table allSales(date DATE,time TIME,pType TEXT,sales INTEGER)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL("drop table if exists temporaryBill");
        DB.execSQL("drop table if exists allSales");
    }

    public Boolean insertUserData(int srno, String product, String price, String quantity, int total)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("srno",srno);
        contentValues.put("product",product);
        contentValues.put("price",price);
        contentValues.put("quantity",quantity);
        contentValues.put("total",total);

        long result = DB.insert("temporaryBill",null,contentValues);
        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }

    }

    public Boolean deleteUserData(int srno)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from temporaryBill where srno = ?",new String[]{String.valueOf(srno)});
        String x = "DELETE FROM temporaryBill where srno = " +String.valueOf(srno);
        long result = DB.delete("temporaryBill","srno = ?", new String[]{String.valueOf(srno)});
        //DB.execSQL(x);

       if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }

    }

    public Cursor getData()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from temporaryBill",null);
        return cursor;

    }
    public void truncate()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        DB.delete("temporaryBill", null, null);

    }

    public boolean addAllSalesTable(String pType,int quantity, int sale, String invoiceNo)
    {

        date = new Date();
        SQLiteDatabase DB = this.getWritableDatabase();
        DB.execSQL("create Table if not exists allSales(invoiceNo TEXT, date DATE,time TIME,pType TEXT,quantity INTEGER,sales INTEGER)");
        ContentValues contentValues = new ContentValues();
        contentValues.put("invoiceNo",String.valueOf(invoiceNo));
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        contentValues.put("date",dateFormat.format(date));

        dateFormat = new SimpleDateFormat("HH:mm:ss");
        contentValues.put("time",dateFormat.format(date));

        contentValues.put("pType",pType);
        contentValues.put("quantity",quantity);
        contentValues.put("sales",sale);

        long result = DB.insert("allSales",null,contentValues);
        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public Cursor retrieveAllSalesTable(String date)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select date,time,pType,quantity,sales from allSales where date=?", new String[]{date});
        return cursor;
    }

    public Cursor retrieveAllSalesTable1(String date)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select date,time,pType,quantity,sales,invoiceNo from allSales where date=?", new String[]{date});
        return cursor;
    }


    public Cursor retrieveCardUPISalesTable(String date)
    {
        String q = "Select * from all sales where date=" + new String[]{date} + "and invoiceNo!=" + "0000";
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from allSales where date=? and invoiceNo !=?", new String[]{date,"0000"});
        //Cursor cursor = DB.execSQL(q);


        return cursor;
    }

    public Cursor retrieveCardUPISalesTable()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from allSales where invoiceNo !=?", new String[]{"0000"});
        return cursor;
    }

    public Cursor retrieveNonCardUPISalesTable()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from allSales where invoiceNo =?", new String[]{"0000"});
        return cursor;
    }



    public boolean addCardUPISalesTable(String invoiceNo,int quantity, String pType, int sale)
    {
        date = new Date();
        SQLiteDatabase DB = this.getWritableDatabase();
        DB.execSQL("create Table if not exists cardUPISales(invoiceNo TEXT, date DATE,time TIME,pType TEXT,quantity INTEGER,sales INTEGER)");
        ContentValues contentValues = new ContentValues();

        contentValues.put("invoiceNo",invoiceNo);

        dateFormat = new SimpleDateFormat("yy/MM/dd");
        contentValues.put("date",dateFormat.format(date));

        dateFormat = new SimpleDateFormat("HH:mm:ss");
        contentValues.put("time",dateFormat.format(date));

        contentValues.put("pType",pType);
        contentValues.put("quantity",quantity);
        contentValues.put("sales",sale);

        long result = DB.insert("cardUPISales",null,contentValues);
        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public Cursor retrieveAllSalesTable()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select date,time,pType,quantity,sales from allSales",null);
        return cursor;
    }

    public boolean deleteAllSales(String date,String time)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        long result = DB.delete("allSales","date = ? and time = ?", new String[]{date,time});

        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }

    }
    public boolean addSettlement(int quantity, int sale)
    {

        date = new Date();
        SQLiteDatabase DB = this.getWritableDatabase();
        DB.execSQL("create Table if not exists settlement(date DATE,quantity INTEGER,sales INTEGER)");
        ContentValues contentValues = new ContentValues();

        dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        contentValues.put("date",dateFormat.format(date));
        contentValues.put("quantity",quantity);
        contentValues.put("sales",sale);

        long result = DB.insert("settlement",null,contentValues);
        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }


    public Cursor retrieveSalesFromTo(String date1, String date2)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from settlement where date between ? and ?", new String[]{date1,date2});
        //Cursor cursor = DB.execSQL(q);


        return cursor;
    }

    public boolean addCredit(String cName, String cPhone, int amount, String tbName)
    {

        date = new Date();

        SQLiteDatabase DB = this.getWritableDatabase();
        DB.execSQL("create Table if not exists credit(tbName TEXT NOT NULL PRIMARY KEY, name TEXT,phone TEXT, date DATE, time TIME)");
        ContentValues contentValues = new ContentValues();

        Cursor cursor = DB.rawQuery("select * from credit where tbName = ?",new String[]{tbName});
        if(cursor.getCount() <= 0){
            dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            contentValues.put("date",dateFormat.format(date));

            dateFormat = new SimpleDateFormat("HH:mm:ss");
            contentValues.put("time",dateFormat.format(date));


            contentValues.put("name",cName);
            contentValues.put("phone",cPhone);
            contentValues.put("tbName",tbName);
            long result = DB.insert("credit",null,contentValues);
            if(result == -1)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
            /*int total = Integer.valueOf(cursor.getString(3));
            total = total + amount;

            DB.execSQL("UPDATE credit SET amount = total where tbName = " + tbName);
                return true;*/
return false;
        }

    }
    public boolean addCreditHistory(String tName, int amount)
    {

        date = new Date();
        SQLiteDatabase DB = this.getWritableDatabase();
        DB.execSQL("create Table if not exists creditHistory(tName TEXT,date DATE,time TIME,amount INTEGER)");
        ContentValues contentValues = new ContentValues();

        dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        contentValues.put("tName",tName);
        contentValues.put("date",dateFormat.format(date));

        dateFormat = new SimpleDateFormat("HH:mm:ss");
        contentValues.put("time",dateFormat.format(date));
        contentValues.put("amount",amount);

        long result = DB.insert("creditHistory",null,contentValues);
        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }


    public void createCreditUserBillTable(String tableName)
    {
        SQLiteDatabase DB = this.getWritableDatabase();

        String userTableQuery = "create Table if not exists " + tableName + "(itemId INTEGER PRIMARY KEY AUTOINCREMENT, product TEXT,price TEXT,quantity TEXT,total INTEGER)";
        DB.execSQL(userTableQuery);
        String copyToThisTable = "INSERT INTO " + tableName + "(product,price,quantity,total)" + " SELECT product,price,quantity,total FROM temporaryBill";
        DB.execSQL(copyToThisTable);

    }

    public Cursor getCreditTableName()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select tbName from credit", null);
        //Cursor cursor = DB.execSQL(q);


        return cursor;
    }

    public Cursor getCreditData(String tName)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from " + tName, null);
        return cursor;
    }

    public Cursor getCreditHistory(String tName)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select date,time,amount from creditHistory where tName = ?", new String[]{tName});
        return cursor;
    }

    public boolean deleteCreditData(int id, String tbName)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        long result = DB.delete(tbName,"itemId = ?", new String[]{String.valueOf(id)});
        //DB.execSQL(x);

        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean deleteCreditHistory(String date,String time)
    {
        SQLiteDatabase DB = this.getWritableDatabase();

        long result = DB.delete("creditHistory","date = ? and time = ?", new String[]{date,time});
        String query = "delete from creditHistory where date = " + date +  " and time = " + time;
        //DB.execSQL(query);
       if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }


}
