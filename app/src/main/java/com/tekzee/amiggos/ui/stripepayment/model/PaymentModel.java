package com.tekzee.amiggos.ui.stripepayment.model;

public class PaymentModel {
    private String id;
    private String brand;
    private String country;
    private String last4;
    private String isDefault;
    private String expiryMonth;
    private String fingerPrint;
    private String expiryYear;

    public PaymentModel(String id, String brand, String country, String last4, String isDefault, String expiryMonth, String fingerPrint, String expiryYear) {
        this.id = id;
        this.brand = brand;
        this.country = country;
        this.last4 = last4;
        this.isDefault = isDefault;
        this.expiryMonth = expiryMonth;
        this.fingerPrint = fingerPrint;
        this.expiryYear = expiryYear;
    }

    public String getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getCountry() {
        return country;
    }

    public String getLast4() {
        return last4;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public String getFingerPrint() {
        return fingerPrint;
    }

    public String getExpiryYear() {
        return expiryYear;
    }
}
