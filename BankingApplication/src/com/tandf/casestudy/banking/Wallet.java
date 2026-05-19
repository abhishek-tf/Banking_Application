package com.tandf.casestudy.banking;

import java.util.ArrayList;
import java.util.List;


public abstract class Wallet implements WalletOperations {

    public static final double MAX_WALLET_BALANCE = 50000.0;
    public static final double DAILY_TRANSFER_LIMIT = 20000.0;

    private String walletId;
    private String ownerName;
    private double balance;
    private double dailyTransferred;
    private final List<String> history = new ArrayList<>();

    public Wallet() {
    }

    public Wallet(String walletId, String ownerName, double initialBalance) {
        if (walletId == null || walletId.trim().isEmpty()) {
            throw new NullPointerException("Wallet id cannot be null or blank");
        }
        if (ownerName == null || ownerName.trim().isEmpty()) {
            throw new NullPointerException("Owner name cannot be null or blank");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        if (initialBalance > MAX_WALLET_BALANCE) {
            throw new WalletLimitExceededException(
                "Initial balance " + initialBalance + " exceeds wallet limit " + MAX_WALLET_BALANCE);
        }
        this.walletId = walletId;
        this.ownerName = ownerName;
        this.balance = initialBalance;
    }

    // ---- Getters / Setters (Encapsulation) ----
    public String getWalletId() {
        return walletId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public double getBalance() {
        return balance;
    }

    public double getDailyTransferred() {
        return dailyTransferred;
    }

    public List<String> getHistory() {
        return new ArrayList<>(history);
    }

    public void setOwnerName(String ownerName) {
        if (ownerName == null || ownerName.trim().isEmpty()) {
            throw new NullPointerException("Owner name cannot be null or blank");
        }
        this.ownerName = ownerName;
    }

    protected void recordHistory(String entry) {
        history.add(entry);
    }

    public abstract String getProvider();

    

    @Override
    public void addMoney(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Add money amount must be greater than zero");
        }
        double newBalance = this.balance + amount;
        if (newBalance > MAX_WALLET_BALANCE) {
            throw new WalletLimitExceededException(
                "Cannot add " + amount + ". Wallet limit of " + MAX_WALLET_BALANCE
                    + " would be exceeded. Current balance: " + balance);
        }
        this.balance = newBalance;
        recordHistory("ADD " + amount + " | Balance: " + balance);
        System.out.println("[" + getProvider() + "] Added " + amount
            + " | New balance: " + balance);
    }

    @Override
    public void payBill(String biller, double amount) {
        if (biller == null || biller.trim().isEmpty()) {
            throw new NullPointerException("Biller name cannot be null or blank");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Bill amount must be greater than zero");
        }
        if (amount > this.balance) {
            throw new InsufficientBalanceException(
                "Insufficient balance to pay " + amount + " to " + biller
                    + ". Available: " + balance);
        }
        this.balance -= amount;
        recordHistory("BILL " + biller + " -" + amount + " | Balance: " + balance);
        System.out.println("[" + getProvider() + "] Paid bill to " + biller
            + " : " + amount + " | New balance: " + balance);
    }

    @Override
    public void transferToWallet(Wallet target, double amount) {
        if (target == null) {
            throw new NullPointerException("Target wallet cannot be null");
        }
        if (this == target
            || (this.walletId != null && this.walletId.equals(target.getWalletId()))) {
            throw new IllegalArgumentException("Cannot transfer to the same wallet");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }
        if (amount > this.balance) {
            throw new InsufficientBalanceException(
                "Insufficient balance to transfer " + amount + ". Available: " + balance);
        }
        if (this.dailyTransferred + amount > DAILY_TRANSFER_LIMIT) {
            throw new WalletLimitExceededException(
                "Daily transfer limit of " + DAILY_TRANSFER_LIMIT
                    + " would be exceeded. Already transferred today: " + dailyTransferred);
        }

        
        this.balance -= amount;
        try {
            target.addMoney(amount);
        } catch (RuntimeException ex) {
            this.balance += amount; // rollback
            throw ex;
        }
        this.dailyTransferred += amount;
        recordHistory("TRANSFER ->" + target.getWalletId() + " " + amount
            + " | Balance: " + balance);
        System.out.println("[" + getProvider() + "] Transferred " + amount
            + " to wallet " + target.getWalletId());
    }

    public void displayDetails() {
        System.out.println("---- Wallet Details ----");
        System.out.println("Provider      : " + getProvider());
        System.out.println("Wallet Id     : " + walletId);
        System.out.println("Owner         : " + ownerName);
        System.out.println("Balance       : " + balance);
        System.out.println("Transferred today : " + dailyTransferred);
        System.out.println("History size  : " + history.size());
    }

    @Override
    public String toString() {
        return getProvider() + "Wallet[id=" + walletId
            + ", owner=" + ownerName + ", balance=" + balance + "]";
    }
}


interface WalletOperations {
    void addMoney(double amount);

    void payBill(String biller, double amount);

    void transferToWallet(Wallet target, double amount);
}


class PaytmWallet extends Wallet {

    private String upiId;

    public PaytmWallet(String walletId, String ownerName, double initialBalance) {
        super(walletId, ownerName, initialBalance);
    }

    public PaytmWallet(String walletId, String ownerName,
                       double initialBalance, String upiId) {
        super(walletId, ownerName, initialBalance);
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
        System.out.println("UPI Id        : " + (upiId != null ? upiId : "N/A"));
    }
}


class PhonePeWallet extends Wallet {

    private double cashback;

    public PhonePeWallet(String walletId, String ownerName, double initialBalance) {
        super(walletId, ownerName, initialBalance);
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
        double cb = amount * 0.01; // 1% cashback (kept separate from wallet balance)
        this.cashback += cb;
        System.out.println("[PhonePe] Cashback earned: " + cb
            + " | Total cashback: " + cashback);
    }

    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("Cashback      : " + cashback);
    }
}



