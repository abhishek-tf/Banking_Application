package com.tandf.casestudy.banking.model;

public class PhonePeWallet extends Wallet {

    private double cashback;

    public PhonePeWallet(String walletId, BankAccount linkedAccount) {
        super(walletId, linkedAccount);
    }

    public PhonePeWallet(String walletId, BankAccount linkedAccount, double initialBalance) {
        super(walletId, linkedAccount, initialBalance);
    }

    public double getCashback() {
        return cashback;
    }

    @Override
    public String getProvider() {
        return "PhonePe";
    }

    @Override
    public void addMoney(double amount) {
        super.addMoney(amount);
        double cb = amount * 0.01; // 1% cashback, tracked separately from wallet balance
        this.cashback += cb;
        System.out.println("[PhonePe] Cashback earned: " + cb
            + " | Total cashback: " + cashback);
    }

    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("Cashback          : " + cashback);
    }
}
