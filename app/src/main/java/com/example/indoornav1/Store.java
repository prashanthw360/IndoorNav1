package com.example.indoornav1;

public class Store {
    private String sid, price, rating, quantity;

    public Store() {
    }

    public Store(String sid, String price, String rating, String quantity) {
        this.sid = sid;
        this.price = price;
        this.rating = rating;
        this.quantity = quantity;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

}
