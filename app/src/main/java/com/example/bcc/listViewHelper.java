package com.example.bcc;

public class listViewHelper {
    int srno, total;
    String product, price, quantity;

    public listViewHelper(int srno, String product, String price, String quantity, int total)
    {
        this.srno = srno;
        this.product = product;
        this.price = price;
        this.quantity = quantity;
        this.total = total;
    }

    public int getSrno() {
        return srno;
    }

    public void setSrno(int srno) {
        this.srno = srno;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
