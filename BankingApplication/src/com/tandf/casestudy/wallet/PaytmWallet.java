package com.tandf.casestudy.wallet;

import com.tandf.casestudy.exception.InsufficientBalanceException;
import com.tandf.casestudy.exception.InvalidAmountException;
import com.tandf.casestudy.exception.WalletLimitExceededException;


public class PaytmWallet implements WalletOperations {

   
    private static final double MAX_WALLET_BALANCE = 50_000.0;
    
    private static final double MAX_TRANSFER_AMOUNT = 20_000.0;

    
    private final String ownerName;
    private double balance;

   
    public PaytmWallet(String ownerName) {
        this.ownerName = ownerName;
        this.balance = 0.0;
    }

   
    public String getOwnerName() {
        return ownerName;
    }

    public double getBalance() {
        return balance;
    }

   
    @Override
    public void addMoney(double amount)
            throws InvalidAmountException, WalletLimitExceededException {

        
        if (amount <= 0) {
            throw new InvalidAmountException(
                "Paytm: Amount must be greater than zero. Provided: " + amount);
        }

       
        if ((balance + amount) > MAX_WALLET_BALANCE) {
            throw new WalletLimitExceededException(
                "Paytm: Adding ₹" + amount +
                " would exceed the maximum wallet balance of ₹" +
                MAX_WALLET_BALANCE + ". Current balance: ₹" + balance);
        }

        balance += amount;
        System.out.println("Paytm: ₹" + amount + " added successfully. " +
                           "New balance: ₹" + balance);
    }

    
    @Override
    public void payBill(double amount)
            throws InvalidAmountException, InsufficientBalanceException {

        if (amount <= 0) {
            throw new InvalidAmountException(
                "Paytm: Bill amount must be greater than zero. Provided: " + amount);
        }

        if (amount > balance) {
            throw new InsufficientBalanceException(
                "Paytm: Insufficient balance to pay ₹" + amount +
                ". Available: ₹" + balance);
        }

        balance -= amount;
        System.out.println("Paytm: Bill of ₹" + amount + " paid successfully. " +
                           "Remaining balance: ₹" + balance);
    }

    
    @Override
    public void transferToWallet(WalletOperations receiver, double amount)
            throws InvalidAmountException,
                   InsufficientBalanceException,
                   WalletLimitExceededException {

        if (amount <= 0) {
            throw new InvalidAmountException(
                "Paytm: Transfer amount must be greater than zero. Provided: " + amount);
        }

        if (amount > MAX_TRANSFER_AMOUNT) {
            throw new WalletLimitExceededException(
                "Paytm: Transfer amount ₹" + amount +
                " exceeds the per-transfer limit of ₹" + MAX_TRANSFER_AMOUNT);
        }

        if (amount > balance) {
            throw new InsufficientBalanceException(
                "Paytm: Insufficient balance to transfer ₹" + amount +
                ". Available: ₹" + balance);
        }

       
        balance -= amount;
        try {
            receiver.addMoney(amount);
            System.out.println("Paytm: ₹" + amount + " transferred successfully. " +
                               "Remaining balance: ₹" + balance);
        } catch (InvalidAmountException | WalletLimitExceededException e) {
        
            balance += amount;
           
            throw e;
        }
    }
}
