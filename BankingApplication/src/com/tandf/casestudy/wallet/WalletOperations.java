package com.tandf.casestudy.wallet;

import com.tandf.casestudy.exception.InsufficientBalanceException;
import com.tandf.casestudy.exception.InvalidAmountException;
import com.tandf.casestudy.exception.WalletLimitExceededException;


public interface WalletOperations {

    void addMoney(double amount)
            throws InvalidAmountException, WalletLimitExceededException;

    
    void payBill(double amount)
            throws InvalidAmountException, InsufficientBalanceException;

    
    void transferToWallet(WalletOperations receiver, double amount)
            throws InvalidAmountException,
                   InsufficientBalanceException,
                   WalletLimitExceededException;
}
