package com.example.smplmedicalapp;

import java.util.Objects;

public class ItemData {

    private  String name, company, image, price, discount, qty, size  ;

    public ItemData() {
    }

    public ItemData(String name, String company,  String image, String price,  String size, String qty, String discount) {

        this.name = name;
        this.image = image;
        this.company = company;
        this.price = price;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemData itemData = (ItemData) o;
        return Objects.equals(name, itemData.name) &&
                Objects.equals(company, itemData.company) &&
                Objects.equals(image, itemData.image) &&
                Objects.equals(price, itemData.price) &&
                Objects.equals(discount, itemData.discount) &&
                Objects.equals(qty, itemData.qty) &&
                Objects.equals(size, itemData.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, company, image, price, discount, qty, size);
    }
}
