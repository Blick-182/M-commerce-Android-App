package com.example.blacklight.Models;

public class Merchants {

    private String merchantName, merchant_phoneNumber, merchant_password;

    public Merchants(){

    }

    public Merchants(String merchantName, String merchant_phoneNumber, String merchant_password) {
        this.merchantName = merchantName;
        this.merchant_phoneNumber = merchant_phoneNumber;
        this.merchant_password = merchant_password;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchant_phoneNumber() {
        return merchant_phoneNumber;
    }

    public void setMerchant_phoneNumber(String merchant_phoneNumber) {
        this.merchant_phoneNumber = merchant_phoneNumber;
    }

    public String getMerchant_password() {
        return merchant_password;
    }

    public void setMerchant_password(String merchant_password) {
        this.merchant_password = merchant_password;
    }
}
