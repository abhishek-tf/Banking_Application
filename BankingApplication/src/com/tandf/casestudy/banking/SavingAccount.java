package com.tandf.casestudy.banking;

public class SavingAccount extends BankAccount {

    private static final double MIN_BALANCE = 1000.0;
    private static final double DEFAULT_INTEREST_RATE = 4.0;

    private double interestRate;

    public SavingAccount() {
        super();
        this.interestRate = DEFAULT_INTEREST_RATE;
    }

    public SavingAccount(String accountNumber, CustomerAccount customer, double initialDeposit) {
        super(accountNumber, customer, initialDeposit);
        if (initialDeposit < MIN_BALANCE) {
            throw new InvalidAmountException(
                "Initial deposit for a Savings Account must be at least " + MIN_BALANCE);
        }
        this.interestRate = DEFAULT_INTEREST_RATE;
    }

    public SavingAccount(String accountNumber, CustomerAccount customer,
                         double initialDeposit, double interestRate) {
        this(accountNumber, customer, initialDeposit);
        if (interestRate < 0) {
            throw new IllegalArgumentException("Interest rate cannot be negative");
        }
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        if (interestRate < 0) {
            throw new IllegalArgumentException("Interest rate cannot be negative");
        }
        this.interestRate = interestRate;
    }

    public static double getMinBalance() {
        return MIN_BALANCE;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdraw amount must be greater than zero");
        }
        double projectedBalance = getBalance() - amount;
        if (projectedBalance < MIN_BALANCE) {
            throw new InsufficientBalanceException(
                "Withdrawal denied. Savings Account must maintain a minimum balance of "
                    + MIN_BALANCE + ". Available: " + getBalance());
        }
        setBalance(projectedBalance);
        System.out.println("Withdrew " + amount + " | New balance: " + getBalance());
    }

    public double addInterest() {
        double interest = (getBalance() * interestRate) / 100.0;
        setBalance(getBalance() + interest);
        System.out.println("Interest credited: " + interest + " | New balance: " + getBalance());
        return interest;
    }

    @Override
    public void displayDetails() {
        System.out.println("Account Number : " + getAccountNumber());
        System.out.println("Customer       : " + (getCustomer() != null ? getCustomer().getName() : "N/A"));
        System.out.println("Balance        : " + getBalance());
        System.out.println("Interest Rate  : " + interestRate + "%");
        System.out.println("Minimum Balance: " + MIN_BALANCE);
    }
}


