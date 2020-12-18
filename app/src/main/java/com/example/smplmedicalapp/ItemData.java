package com.example.smplmedicalapp;

public class ItemData {

    private  String name, description, image, price, discount, qty, size, dprice ;

    public ItemData() {
    }

    public ItemData(String name, String description,  String image, String price, String dprice, String size, String qty, String discount) {

        this.name = name;
        this.image = image;
        this.description = description;
        this.price = price;
        this.dprice = dprice;
        this.discount = discount;
        this.qty = qty;
        this.size = size;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDprice() {
        return dprice;
    }

    public void setDprice(String dprice) {
        this.dprice = dprice;
    }
}
