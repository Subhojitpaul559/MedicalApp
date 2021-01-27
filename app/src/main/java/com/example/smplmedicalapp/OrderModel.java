package com.example.smplmedicalapp;

public class OrderModel {

    private String medicineName, address, name,phone, amount, tax,quantity, total, orderID, storeId, umedID , UID, ustatus;

    //private Long quantity;
    //private Integer quantity;

    public OrderModel() {
    }

    public OrderModel(String medicineName, String quantity, String amount, String address,
                      String name, String phone, String tax, String total, String orderID,
                      String storeId, String umedID, String UID, String ustatus) {
        this.medicineName = medicineName;
        this.amount = amount;
        this.umedID = umedID;
        this.ustatus = ustatus;
        this.quantity = quantity;
        this.UID = UID;
        this.address = address;
        this.name = name;
        this.phone = phone;
        this.tax = tax;
        this.total = total;
        this.orderID = orderID;
        this.storeId = storeId;
    }

    public String getUstatus() {
        return ustatus;
    }

    public void setUstatus(String ustatus) {
        this.ustatus = ustatus;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getUmedID() {
        return umedID;
    }

    public void setUmedID(String umedID) {
        this.umedID = umedID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
