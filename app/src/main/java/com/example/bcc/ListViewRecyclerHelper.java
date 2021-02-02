package com.example.bcc;

public class ListViewRecyclerHelper {
    private String srno, product, quantity, price, amount;

    public ListViewRecyclerHelper(String srno, String product, String quantity, String price, String amount) {
        this.srno = srno;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.amount = amount;
    }

    public String getSrno() {
        return srno;
    }

    public void setSrno(String srno) {
        this.srno = srno;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
