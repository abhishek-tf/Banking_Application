package com.tandf.casestudy.banking.model;

public class PaytmWallet extends Wallet {

    private String upiId;

    public PaytmWallet(String walletId, BankAccount linkedAccount) {
        super(walletId, linkedAccount);
    }

    public PaytmWallet(String walletId, BankAccount linkedAccount, double initialBalance) {
        super(walletId, linkedAccount, initialBalance);
    }

    public PaytmWallet(String walletId, BankAccount linkedAccount,
                       double initialBalance, String upiId) {
        super(walletId, linkedAccount, initialBalance);
        this.upiId = upiId;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    @Override
    public String getProvider() {
        return "Paytm";
    }

    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("UPI Id            : " + (upiId != null ? upiId : "N/A"));
    }
}
