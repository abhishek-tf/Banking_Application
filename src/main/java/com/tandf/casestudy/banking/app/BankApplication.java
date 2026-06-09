package com.tandf.casestudy.banking.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import com.tandf.casestudy.banking.exception.DuplicateCustomerException;
import com.tandf.casestudy.banking.exception.InsufficientBalanceException;
import com.tandf.casestudy.banking.exception.InvalidAmountException;
import com.tandf.casestudy.banking.exception.InvalidEmailException;
import com.tandf.casestudy.banking.exception.InvalidPhoneNumberException;
import com.tandf.casestudy.banking.model.BankAccount;
import com.tandf.casestudy.banking.model.CurrentAccount;
import com.tandf.casestudy.banking.model.CustomerAccount;
import com.tandf.casestudy.banking.model.PaytmWallet;
import com.tandf.casestudy.banking.model.PhonePeWallet;
import com.tandf.casestudy.banking.model.SavingAccount;
import com.tandf.casestudy.banking.model.Transaction;
import com.tandf.casestudy.banking.model.TransactionType;
import com.tandf.casestudy.banking.model.Wallet;



// 3. Deposit
// 4. Withdraw
// 5. Transfer
// 6. Wallet Operations
// 7. Clone Account
// 8. View Transactions
// 9. Exit
public class BankApplication {

    public static void main(String[] args) {
        String customerId = null;
        CustomerAccount customer;
        HashMap<String, CustomerAccount> l1 = new HashMap<>();
        Map<String, BankAccount> accounts = new HashMap<>();
        Map<String, Wallet> wallets = new HashMap<>();
        List<Transaction> history;
        history = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
       

        while (true) {
            System.out.println("Welcome to the Banking Application!");
            System.out.println("\n===== Banking Menu =====");
            System.out.println("1. Create Customer");
            System.out.println("2. Open Account");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Transfer");
            System.out.println("6. Wallet Operations");
            System.out.println("7. Clone Account");
            System.out.println("8. View Transactions");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
                case 1:
                    System.out.println("Enter Customer ID:");
                    try {
                        customerId = sc.nextLine();
                        if (l1.containsKey(customerId)) {
                            throw new DuplicateCustomerException("Customer ID already exists. Please try again.");
                        }
                    } catch (DuplicateCustomerException e) {
                        System.out.println(e.getMessage());
                    }
                    System.out.println("Enter Name:");
                    String name = sc.nextLine();
                    System.out.println("Enter Email:");
                    String email = sc.nextLine();
                    System.out.println("Enter Phone Number:");
                    String phNumber = sc.nextLine();
                    try {
                        customer = new CustomerAccount(customerId, name, email, phNumber);
                        l1.put(customerId, customer);
                        System.out.println("Customer created successfully: " + customer);
                    } catch (InvalidEmailException | InvalidPhoneNumberException e) {
                        System.out.println("Error creating customer: " + e.getMessage());
                    }
                    break;
                case 2:
                    System.out.println("Enter Customer ID to open account:");
                    String Id = sc.nextLine();
                    if (!l1.containsKey(Id)) {
                        System.out.println("Customer ID not found. Please create a customer first.");
                        break;
                    }
                    System.out.println("Enter Account Type (1 for Savings, 2 for Current):");
                    int accountType = Integer.parseInt(sc.nextLine());;
                    if (accountType != 1 && accountType != 2) {
                        System.out.println("Invalid account type. Please enter 1 for Savings or 2 for Current.");
                        break;
                    } else if (accountType == 1) {
                        System.out.println("Opening Savings Account for customer: " + l1.get(Id).getName());
                        int randomNumber = (int) (Math.random() * 9000000) + 1000000;
                        String accountNumber = "ACC" + randomNumber;

                        System.out.println("Enter Initial Deposit:");
                        double initialDeposit = Double.parseDouble(sc.nextLine());
                        //generate account number   
                        try {
                            SavingAccount savingAccount = new SavingAccount(accountNumber, l1.get(Id), initialDeposit);
                            accounts.put(accountNumber, savingAccount);
                            System.out.println("Savings Account created successfully: " + savingAccount.getAccountNumber());
                        } catch (InvalidAmountException | IllegalArgumentException e) {
                            System.out.println("Error creating account: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Opening Current Account for customer: " + l1.get(Id).getName());
                        int randomNumber = (int) (Math.random() * 9000000) + 1000000;
                        String accountNumber = "ACC" + randomNumber;
                        System.out.println("Generated Account Number: please copy it " + accountNumber);
                        System.out.println("Enter Initial Deposit:");
                        double initialDeposit =  Double.parseDouble(sc.nextLine());
                        try {
                            CurrentAccount currentAccount = new CurrentAccount(accountNumber, l1.get(Id), initialDeposit);
                            accounts.put(accountNumber, currentAccount);
                            System.out.println("Current Account created successfully: " + currentAccount.getAccountNumber());
                        } catch (InvalidAmountException | IllegalArgumentException e) {
                            System.out.println("Error creating account: " + e.getMessage());
                        }
                    }

                    break;
                case 3:
                    System.out.println("Enter Account Number for Deposit:");
                    String accNum = sc.nextLine();
                    if (!accounts.containsKey(accNum)) {
                        System.out.println("Account number not found. Please try again.");
                        break;
                    }
                    System.out.println("Enter Deposit Amount:");
                    double depositAmount = Double.parseDouble(sc.nextLine());
                    try {
                        // accounts.get(accNum).deposit(depositAmount);
                        BankAccount account = accounts.get(accNum);
                        account.deposit(depositAmount);
                        history.add(new Transaction(accNum, depositAmount, TransactionType.DEPOSIT));
                    } catch (InvalidAmountException e) {
                        System.out.println("Error during deposit: " + e.getMessage());
                    }
                    break;
                case 4:
                    System.out.println("Enter Account Number for Withdrawal:");
                    String accNumWithdraw = sc.nextLine();
                    if (!accounts.containsKey(accNumWithdraw)) {
                        System.out.println("Account number not found. Please try again.");
                        break;
                    }
                    System.out.println("Enter Withdrawal Amount:");
                    double withdrawAmount = Double.parseDouble(sc.nextLine());

                    try {
                        BankAccount account = accounts.get(accNumWithdraw);
                        account.withdraw(withdrawAmount);
                        history.add(new Transaction(accNumWithdraw, withdrawAmount, TransactionType.WITHDRAWAL));
                    } catch (InvalidAmountException | InsufficientBalanceException e) {
                        System.out.println("Error during withdrawal: " + e.getMessage());
                    }
                    break;
                case 5:
                    System.out.println("Enter Source Account Number for Transfer:");
                    String sourceAccNum = sc.nextLine();
                    if (!accounts.containsKey(sourceAccNum)) {
                        System.out.println("Source account number not found. Please try again.");
                        break;
                    }
                    System.out.println("Enter Destination Account Number for Transfer:");
                    String destAccNum = sc.nextLine();
                    if (!accounts.containsKey(destAccNum)) {
                        System.out.println("Destination account number not found. Please try again.");
                        break;
                    }
                    System.out.println("Enter Transfer Amount:");
                    double transferAmount = Double.parseDouble(sc.nextLine());

                    try {
                        BankAccount sourceAccount = accounts.get(sourceAccNum);
                        BankAccount destAccount = accounts.get(destAccNum);
                        sourceAccount.withdraw(transferAmount);
                        destAccount.deposit(transferAmount);
                        history.add(new Transaction(sourceAccNum, transferAmount, TransactionType.TRANSFER));
                        history.add(new Transaction(destAccNum, transferAmount, TransactionType.TRANSFER));
                        System.out.println("Transfer successful. New balance of source account: " + sourceAccount.getBalance());
                        System.out.println("New balance of destination account: " + destAccount.getBalance());
                    } catch (InvalidAmountException | InsufficientBalanceException e) {
                        System.out.println("Error during transfer: " + e.getMessage());
                    }
                    break;
                case 6:
                    // addMoney()
                    // payBill()
                    // transferToWallet()
                    //Idea is to create wallet and they link internally bankaccount but i should give option whether to go with Paytm or phonepe

                    System.out.println("Wallet Operations:");
                    System.out.println("Enter: 1 for Creating Wallet, 2 for Adding Money, 3 for Paying Bill, 4 for Transferring to Wallet");
                    int walletChoice = Integer.parseInt(sc.nextLine());
                    if (walletChoice < 1 || walletChoice > 4) {
                        System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                        break;
                    } else if (walletChoice == 1) {
                        System.out.println("Enter Account Number to link with Wallet:");
                        String accNumWallet = sc.nextLine();
                        if (!accounts.containsKey(accNumWallet)) {
                            System.out.println("Account number not found. Please try again.");
                            break;
                        }

                        boolean hasPaytm = false;
                        boolean hasPhonePe = false;

                        for (Wallet w : wallets.values()) {

                            if (w.getLinkedAccount().getAccountNumber().equals(accNumWallet)) {

                                if (w instanceof PaytmWallet) {
                                    hasPaytm = true;
                                }

                                if (w instanceof PhonePeWallet) {
                                    hasPhonePe = true;
                                }
                            }
                        }
                        if (hasPaytm && hasPhonePe) {

                            System.out.println("This account is already linked to both Paytm and PhonePe wallets.");
                            break;
                        }

                        System.out.println("Select Wallet Type: 1 for Paytm, 2 for PhonePe");
                        int walletType = Integer.parseInt(sc.nextLine());
                        if (walletType != 1 && walletType != 2) {

                            System.out.println("Invalid wallet type. Please enter 1 for Paytm or 2 for PhonePe.");
                            break;
                        }
                        if (hasPaytm && hasPhonePe) {
                            System.out.println("This account is already linked to both Paytm and PhonePe wallets.");
                            break;
                        }

                        if (walletType == 1 && hasPaytm) {
                            System.out.println("This account is already linked to Paytm Wallet.");
                            System.out.println("You can still link it to PhonePe Wallet.");
                            break;
                        }

                        if (walletType == 2 && hasPhonePe) {
                            System.out.println("This account is already linked to PhonePe Wallet.");
                            System.out.println("You can still link it to Paytm Wallet.");
                            break;
                        }
                        BankAccount linkedAccount = accounts.get(accNumWallet);

                        if (walletType == 1) {
                            String walletId = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
                            Wallet wallet = new PaytmWallet(walletId, linkedAccount);
                            wallets.put(walletId, wallet);

                        } else {
                            String walletId = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
                            Wallet wallet = new PhonePeWallet(walletId, linkedAccount);
                            wallets.put(walletId, wallet);
                        }
                    } else if (walletChoice == 2) {

                        System.out.println("Enter Account Number:");
                        String accNumber = sc.nextLine();

                        if (!accounts.containsKey(accNumber)) {
                            System.out.println("Account number not found.");
                            continue;
                        }

                        System.out.println("Select Wallet Type:");
                        System.out.println("1. Paytm");
                        System.out.println("2. PhonePe");

                        int walletType = Integer.parseInt(sc.nextLine());

                        Wallet wallet = null;

                        for (Wallet w : wallets.values()) {

                            if (w.getLinkedAccount().getAccountNumber().equals(accNumber)) {

                                if (walletType == 1 && w instanceof PaytmWallet) {
                                    wallet = w;
                                    break;
                                }

                                if (walletType == 2 && w instanceof PhonePeWallet) {
                                    wallet = w;
                                    break;
                                }
                            }
                        }

                        if (wallet == null) {

                            if (walletType == 1) {
                                System.out.println("No Paytm wallet linked with this account.");
                            } else {
                                System.out.println("No PhonePe wallet linked with this account.");
                            }

                            continue;
                        }

                        System.out.println("Enter Amount to Add:");
                        double amountToAdd = Double.parseDouble(sc.nextLine());

                        try {

                            wallet.addMoney(amountToAdd);

                            System.out.println("Money added successfully.");
                            System.out.println("New Wallet Balance: " + wallet.getBalance());

                        } catch (InvalidAmountException e) {

                            System.out.println("Error adding money: " + e.getMessage());
                        }
                    } else if (walletChoice == 3) {

                        System.out.println("Enter Account Number:");
                        String accNumber = sc.nextLine();

                        if (!accounts.containsKey(accNumber)) {
                            System.out.println("Account number not found.");
                            continue;
                        }

                        System.out.println("Select Wallet Type:");
                        System.out.println("1. Paytm");
                        System.out.println("2. PhonePe");

                        int walletType = Integer.parseInt(sc.nextLine());

                        Wallet wallet = null;

                        // Find wallet
                        for (Wallet w : wallets.values()) {

                            if (w.getLinkedAccount().getAccountNumber().equals(accNumber)) {

                                if (walletType == 1 && w instanceof PaytmWallet) {
                                    wallet = w;
                                    break;
                                }

                                if (walletType == 2 && w instanceof PhonePeWallet) {
                                    wallet = w;
                                    break;
                                }
                            }
                        }

                        // Wallet not linked
                        if (wallet == null) {

                            if (walletType == 1) {
                                System.out.println("No Paytm wallet linked with this account.");
                            } else {
                                System.out.println("No PhonePe wallet linked with this account.");
                            }

                            continue;
                        }

                        System.out.println("Enter Bill Amount:");
                        double billAmount = Double.parseDouble(sc.nextLine());
                        System.out.println("Enter Bill Name:");
                        String billName = sc.nextLine(); // Consume newline and read bill name

                        try {

                            wallet.payBill(billName, billAmount);

                            System.out.println("Bill paid successfully.");
                            System.out.println("New Wallet Balance: " + wallet.getBalance());

                        } catch (InvalidAmountException | InsufficientBalanceException e) {

                            System.out.println("Error paying bill: " + e.getMessage());
                        }
                       

                    }
                     break;
                case 7:
                    System.out.println("Enter Account Number to Clone:");
                    String accNumClone = sc.nextLine();
                    if (!accounts.containsKey(accNumClone)) {
                        System.out.println("Account number not found. Please try again.");
                        break;
                    }
                    BankAccount originalAccount = accounts.get(accNumClone);
                    try {
                        BankAccount clonedAccount = originalAccount.clone();
                        String newAccountNumber = "ACC" + ((int) (Math.random() * 9000000) + 1000000);
                        clonedAccount.setAccountNumber(newAccountNumber);
                        accounts.put(newAccountNumber, clonedAccount);
                        System.out.println("Account cloned successfully. New Account Number: " + newAccountNumber);
                    } catch (CloneNotSupportedException e) {
                        System.out.println("Error cloning account: " + e.getMessage());
                    }
                    break;
                case 8:
                    System.out.println("Enter Account Number to View Transactions:");
                    String accNumHistory = sc.nextLine();
                    if (!accounts.containsKey(accNumHistory)) {
                        System.out.println("Account number not found. Please try again.");
                        break;
                    }
                    System.out.println("Transaction History for Account: " + accNumHistory);
                    for (Transaction t : history) {
                        if (t.getAccountNumber().equals(accNumHistory)) {
                            System.out.println(t);
                        }
                    }
                    break;
                case 9:
                    System.out.println("Exiting the application. Thank you for using our services!");
                    System.out.println("Refresh the web to start from fresh");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 9.");
            }
        }
    }
}
