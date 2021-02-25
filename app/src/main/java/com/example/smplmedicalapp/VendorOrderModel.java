package com.example.smplmedicalapp;

public class VendorOrderModel {
    private String medicineId, uOID, storeId, status,uid;

    public VendorOrderModel() {
    }

    public VendorOrderModel(String medicineId,  String uOID, String storeId, String status, String uid) {
        this.medicineId = medicineId;

        this.uOID = uOID;
        this.uid=uid;
        this.status=status;
        this.storeId = storeId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }


    public String getuOID() {
        return uOID;
    }

    public void setuOID(String uOID) {
        this.uOID = uOID;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
