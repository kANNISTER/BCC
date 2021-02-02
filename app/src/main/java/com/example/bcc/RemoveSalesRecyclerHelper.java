package com.example.bcc;

public class RemoveSalesRecyclerHelper {

    public String srno, date,time,pType,qty,amount;


    public RemoveSalesRecyclerHelper(String srno, String date, String time, String pType, String qty, String amount) {
        this.srno = srno;
        this.date = date;
        this.time = time;
        this.pType = pType;
        this.qty = qty;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSrno() {
        return srno;
    }

    public void setSrno(String srno) {
        this.srno = srno;
    }

    public String getpType() {
        return pType;
    }

    public void setpType(String pType) {
        this.pType = pType;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
