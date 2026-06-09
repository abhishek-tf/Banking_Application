package com.tandf.casestudy.banking.model;
import com.tandf.casestudy.banking.exception.InsufficientBalanceException;
import com.tandf.casestudy.banking.exception.InvalidAmountException;
public abstract class BankAccount implements Cloneable {

    private String accountNumber;
    private CustomerAccount customer;
    private double balance;

    public BankAccount(){

    }

    public BankAccount(String accountNumber, CustomerAccount customer, double initialDeposit) {
        if (accountNumber == null) {
            throw new NullPointerException("Account number cannot be null or blank");
        }
        if (customer == null) {
            throw new NullPointerException("Customer cannot be null");
        }
        if (initialDeposit < 0) {
            throw new InvalidAmountException("Initial deposit cannot be negative");
        }
        this.accountNumber = accountNumber;
        this.customer = customer;
        this.balance = initialDeposit;
    }

   

    public String getAccountNumber() {
        return accountNumber;
    }

    public CustomerAccount getCustomer() {
        return customer;
    }

    public double getBalance() {
        return balance;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setCustomer(CustomerAccount customer) {
        this.customer = customer;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be greater than zero");
        }
        balance += amount;
        System.out.println("Deposited " + amount + "  New balance: " + balance);
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdraw amount must be greater than zero");
        }
        if (amount > balance) {
            throw new InsufficientBalanceException("Insufficient balance. Available: " + balance);
        }
        balance -= amount;
        System.out.println("Withdrew " + amount + " | New balance: " + balance);
    }

    public void transfer(BankAccount target, double amount) {
        if (target == null) {
            throw new NullPointerException("Target account cannot be null");
        }
        if (this == target || this.accountNumber.equals(target.accountNumber)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        if (amount <= 0) {
            throw new InvalidAmountException("Transfer amount must be greater than zero");
        }
        this.withdraw(amount);
        target.deposit(amount);
        System.out.println("Transferred " + amount + " from " + this.accountNumber + " to " + target.accountNumber);
    }

    public abstract void displayDetails();

    @Override
    public BankAccount clone() throws CloneNotSupportedException {
        return (BankAccount) super.clone();
    }

    public BankAccount deepClone() throws CloneNotSupportedException {
        BankAccount copy = (BankAccount) super.clone();
        if (this.customer != null) {
            copy.customer = this.customer.deepClone();
        }
        return copy;
    }
}

