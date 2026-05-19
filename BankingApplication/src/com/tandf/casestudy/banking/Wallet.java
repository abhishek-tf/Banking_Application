package com.tandf.casestudy.banking;

import java.util.ArrayList;
import java.util.List;

/*
 * Wallet Module - Smart Banking & Wallet System
 *
 * A Wallet is LINKED to a BankAccount, which in turn belongs to a
 * CustomerAccount. This closes the workflow:
 *
 *      CustomerAccount  ->  BankAccount  ->  Wallet
 *
 * Money flows:
 *      BankAccount.balance  <-- loadFromBankAccount / transferToBank -->  Wallet.balance
 *      Wallet.balance       <-- transferToWallet -->  Wallet.balance
 *      Wallet.balance       --  payBill  -->  (biller)
 *
 * File contents:
 *   1. Wallet                (public abstract base, matches file name)
 *   2. WalletOperations      (interface)
 *   3. PaytmWallet           (concrete subclass)
 *   4. PhonePeWallet         (concrete subclass, overrides addMoney for cashback)
 *
 * Concepts demonstrated:
 *   - Interface, abstract class, inheritance, polymorphism, method overriding
 *   - Encapsulation (private fields + getters/setters)
 *   - Custom exceptions (WalletLimitExceededException, InsufficientBalanceException)
 *   - try-catch (rollback on failed transfer)
 *   - throw keyword (validation)
 *   - Collections (ArrayList history)
 *   - Composition / linkage with CustomerAccount and BankAccount
 */
public abstract class Wallet implements WalletOperations {

    public static final double MAX_WALLET_BALANCE = 50000.0;
    public static final double DAILY_TRANSFER_LIMIT = 20000.0;

    private String walletId;
    private CustomerAccount owner;       // derived from linkedAccount.getCustomer()
    private BankAccount linkedAccount;   // the bank account this wallet draws from
    private double balance;
    private double dailyTransferred;
    private final List<String> history = new ArrayList<>();

    public Wallet() {
    }

    /*
     * Primary constructor - links the wallet to an existing BankAccount.
     * The owning CustomerAccount is taken from the bank account itself,
     * guaranteeing wallet and account share the same customer.
     */
    public Wallet(String walletId, BankAccount linkedAccount) {
        if (walletId == null || walletId.trim().isEmpty()) {
            throw new NullPointerException("Wallet id cannot be null or blank");
        }
        if (linkedAccount == null) {
            throw new NullPointerException("Linked bank account cannot be null");
        }
        if (linkedAccount.getCustomer() == null) {
            throw new NullPointerException("Linked bank account must have a customer");
        }
        this.walletId = walletId;
        this.linkedAccount = linkedAccount;
        this.owner = linkedAccount.getCustomer();
        this.balance = 0.0;
    }

    /*
     * Convenience constructor - links to a bank account AND seeds the wallet
     * with an initial balance (useful for tests / demo data).
     */
    public Wallet(String walletId, BankAccount linkedAccount, double initialBalance) {
        this(walletId, linkedAccount);
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        if (initialBalance > MAX_WALLET_BALANCE) {
            throw new WalletLimitExceededException(
                "Initial balance " + initialBalance + " exceeds wallet limit " + MAX_WALLET_BALANCE);
        }
        this.balance = initialBalance;
    }

    // ---- Getters / Setters (Encapsulation) ----
    public String getWalletId() {
        return walletId;
    }

    public CustomerAccount getOwner() {
        return owner;
    }

    public BankAccount getLinkedAccount() {
        return linkedAccount;
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

    protected void recordHistory(String entry) {
        history.add(entry);
    }

    public abstract String getProvider();

    // ---- WalletOperations implementation ----

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

        // Debit sender first, then credit target - rollback if credit fails.
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

    // ---- Linkage with the BankAccount module ----

    /*
     * Move money from the linked bank account INTO this wallet.
     * Uses BankAccount.withdraw(), which may throw InsufficientBalanceException
     * if the bank account does not have enough funds. If the bank withdrawal
     * succeeds but the wallet credit would breach the wallet limit, the
     * bank balance is restored (deposit back) so no money is lost.
     */
    public void loadFromBankAccount(double amount) {
        if (linkedAccount == null) {
            throw new NullPointerException("No bank account is linked to this wallet");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Load amount must be greater than zero");
        }
        if (this.balance + amount > MAX_WALLET_BALANCE) {
            throw new WalletLimitExceededException(
                "Cannot load " + amount + " into wallet. Limit " + MAX_WALLET_BALANCE
                    + " would be exceeded. Current wallet balance: " + balance);
        }
        linkedAccount.withdraw(amount); // may throw InsufficientBalanceException
        try {
            this.balance += amount;
        } catch (RuntimeException ex) {
            linkedAccount.deposit(amount); // rollback
            throw ex;
        }
        recordHistory("LOAD from " + linkedAccount.getAccountNumber()
            + " " + amount + " | Balance: " + balance);
        System.out.println("[" + getProvider() + "] Loaded " + amount
            + " from bank account " + linkedAccount.getAccountNumber()
            + " | Wallet balance: " + balance);
    }

    /*
     * Move money FROM this wallet back into the linked bank account.
     * Wallet is debited first, then bank is credited - rollback on failure.
     */
    public void transferToBank(double amount) {
        if (linkedAccount == null) {
            throw new NullPointerException("No bank account is linked to this wallet");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }
        if (amount > this.balance) {
            throw new InsufficientBalanceException(
                "Insufficient wallet balance to transfer " + amount
                    + " to bank. Available: " + balance);
        }
        this.balance -= amount;
        try {
            linkedAccount.deposit(amount);
        } catch (RuntimeException ex) {
            this.balance += amount; // rollback
            throw ex;
        }
        recordHistory("WITHDRAW to " + linkedAccount.getAccountNumber()
            + " " + amount + " | Balance: " + balance);
        System.out.println("[" + getProvider() + "] Transferred " + amount
            + " back to bank account " + linkedAccount.getAccountNumber()
            + " | Wallet balance: " + balance);
    }

    public void displayDetails() {
        System.out.println("---- Wallet Details ----");
        System.out.println("Provider          : " + getProvider());
        System.out.println("Wallet Id         : " + walletId);
        System.out.println("Owner             : "
            + (owner != null ? owner.getName() : "N/A"));
        System.out.println("Linked Account    : "
            + (linkedAccount != null ? linkedAccount.getAccountNumber() : "N/A"));
        System.out.println("Bank Balance      : "
            + (linkedAccount != null ? linkedAccount.getBalance() : "N/A"));
        System.out.println("Wallet Balance    : " + balance);
        System.out.println("Transferred today : " + dailyTransferred);
        System.out.println("History size      : " + history.size());
    }

    @Override
    public String toString() {
        return getProvider() + "Wallet[id=" + walletId
            + ", owner=" + (owner != null ? owner.getName() : "?")
            + ", linkedAccount=" + (linkedAccount != null ? linkedAccount.getAccountNumber() : "?")
            + ", balance=" + balance + "]";
    }
}


interface WalletOperations {
    void addMoney(double amount);

    void payBill(String biller, double amount);

    void transferToWallet(Wallet target, double amount);
}


class PaytmWallet extends Wallet {

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


class PhonePeWallet extends Wallet {

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
