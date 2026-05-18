package com.tandf.casestudy.main;

import com.tandf.casestudy.exception.InsufficientBalanceException;
import com.tandf.casestudy.exception.InvalidAmountException;
import com.tandf.casestudy.exception.WalletLimitExceededException;
import com.tandf.casestudy.wallet.PaytmWallet;
import com.tandf.casestudy.wallet.PhonePeWallet;
import com.tandf.casestudy.wallet.WalletOperations;


public class Main {

    public static void main(String[] args) {

        System.out.println("=========================================");
        System.out.println("   SMART BANKING & WALLET SYSTEM DEMO    ");
        System.out.println("=========================================\n");

        // ---------- 1. Create wallet objects ----------
        // Notice the LEFT-HAND-SIDE type is the interface WalletOperations.
        // This is POLYMORPHISM -- the same reference type can point to
        // any class that implements WalletOperations.
        WalletOperations paytm   = new PaytmWallet("Hemanth");
        WalletOperations phonePe = new PhonePeWallet("Abhishek");

        // ---------- 2. Test addMoney (happy path) ----------
        System.out.println("---- Test 1 : addMoney (valid) ----");
        try {
            paytm.addMoney(15_000);
            phonePe.addMoney(10_000);
        } catch (InvalidAmountException | WalletLimitExceededException e) {
            System.out.println("ERROR: " + e.getMessage());
        } finally {
            System.out.println("Test 1 completed.\n");
        }

        // ---------- 3. Test payBill (happy path) ----------
        System.out.println("---- Test 2 : payBill (valid) ----");
        try {
            paytm.payBill(2_500);
        } catch (InvalidAmountException | InsufficientBalanceException e) {
            System.out.println("ERROR: " + e.getMessage());
        } finally {
            System.out.println("Test 2 completed.\n");
        }

        // ---------- 4. Test transferToWallet (happy path) ----------
        System.out.println("---- Test 3 : transferToWallet (valid) ----");
        try {
            paytm.transferToWallet(phonePe, 5_000);
        } catch (InvalidAmountException
                 | InsufficientBalanceException
                 | WalletLimitExceededException e) {
            System.out.println("ERROR: " + e.getMessage());
        } finally {
            System.out.println("Test 3 completed.\n");
        }

        // ---------- 5. Demonstrate InvalidAmountException ----------
        System.out.println("---- Test 4 : addMoney with negative amount ----");
        try {
            paytm.addMoney(-100);   // negative -> InvalidAmountException
        } catch (InvalidAmountException e) {
            System.out.println("Caught InvalidAmountException -> " + e.getMessage());
        } catch (WalletLimitExceededException e) {
            System.out.println("ERROR: " + e.getMessage());
        } finally {
            System.out.println("Test 4 completed.\n");
        }

        // ---------- 6. Demonstrate WalletLimitExceededException ----------
        System.out.println("---- Test 5 : addMoney that exceeds ₹50,000 limit ----");
        try {
            // paytm currently around ₹7,500, adding 60,000 would breach the limit
            paytm.addMoney(60_000);
        } catch (WalletLimitExceededException e) {
            System.out.println("Caught WalletLimitExceededException -> " + e.getMessage());
        } catch (InvalidAmountException e) {
            System.out.println("ERROR: " + e.getMessage());
        } finally {
            System.out.println("Test 5 completed.\n");
        }

        // ---------- 7. Demonstrate InsufficientBalanceException ----------
        System.out.println("---- Test 6 : payBill larger than balance ----");
        try {
            phonePe.payBill(99_999);    // way more than current balance
        } catch (InsufficientBalanceException e) {
            System.out.println("Caught InsufficientBalanceException -> " + e.getMessage());
        } catch (InvalidAmountException e) {
            System.out.println("ERROR: " + e.getMessage());
        } finally {
            System.out.println("Test 6 completed.\n");
        }

        // ---------- 8. Demonstrate per-transfer cap (₹20,000) ----------
        System.out.println("---- Test 7 : transfer exceeding ₹20,000 cap ----");
        try {
            // Top up first so balance is high enough
            paytm.addMoney(30_000);
            paytm.transferToWallet(phonePe, 25_000);   // > 20,000
        } catch (WalletLimitExceededException e) {
            System.out.println("Caught WalletLimitExceededException -> " + e.getMessage());
        } catch (InvalidAmountException | InsufficientBalanceException e) {
            System.out.println("ERROR: " + e.getMessage());
        } finally {
            System.out.println("Test 7 completed.\n");
        }

        // ---------- 9. Final balances ----------
        System.out.println("=========================================");
        System.out.println("           FINAL WALLET STATE            ");
        System.out.println("=========================================");
        System.out.println("Paytm   (Hemanth)  balance : ₹"
                + ((PaytmWallet) paytm).getBalance());
        System.out.println("PhonePe (Abhishek) balance : ₹"
                + ((PhonePeWallet) phonePe).getBalance());
        System.out.println("=========================================");
    }
}
