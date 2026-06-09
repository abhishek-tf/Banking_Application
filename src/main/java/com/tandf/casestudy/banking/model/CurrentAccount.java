package com.tandf.casestudy.banking.model;
import com.tandf.casestudy.banking.exception.InsufficientBalanceException;
import com.tandf.casestudy.banking.exception.InvalidAmountException;
public class CurrentAccount extends BankAccount {

    private static final double DEFAULT_OVERDRAFT_LIMIT = 10000.0;

    private double overdraftLimit;

    public CurrentAccount() {
        super();
        this.overdraftLimit = DEFAULT_OVERDRAFT_LIMIT;
    }

    public CurrentAccount(String accountNumber, CustomerAccount customer, double initialDeposit) {
        super(accountNumber, customer, initialDeposit);
        this.overdraftLimit = DEFAULT_OVERDRAFT_LIMIT;
    }

    public CurrentAccount(String accountNumber, CustomerAccount customer,
                          double initialDeposit, double overdraftLimit) {
        super(accountNumber, customer, initialDeposit);
        if (overdraftLimit < 0) {
            throw new IllegalArgumentException("Overdraft limit cannot be negative");
        }
        this.overdraftLimit = overdraftLimit;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        if (overdraftLimit < 0) {
            throw new IllegalArgumentException("Overdraft limit cannot be negative");
        }
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdraw amount must be greater than zero");
        }
        double availableFunds = getBalance() + overdraftLimit;
        if (amount > availableFunds) {
            throw new InsufficientBalanceException(
                "Withdrawal denied. Amount exceeds balance plus overdraft limit. Available: "
                    + availableFunds);
        }
        setBalance(getBalance() - amount);
        System.out.println("Withdrew " + amount + " | New balance: " + getBalance());
    }

    @Override
    public void displayDetails() {
        System.out.println("Account Number  : " + getAccountNumber());
        System.out.println("Customer        : " + (getCustomer() != null ? getCustomer().getName() : "N/A"));
        System.out.println("Balance         : " + getBalance());
        System.out.println("Overdraft Limit : " + overdraftLimit);
    }
}
