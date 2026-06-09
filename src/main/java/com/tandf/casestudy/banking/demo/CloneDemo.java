package com.tandf.casestudy.banking.demo;

import com.tandf.casestudy.banking.exception.InvalidEmailException;
import com.tandf.casestudy.banking.exception.InvalidPhoneNumberException;
import com.tandf.casestudy.banking.model.Address;
import com.tandf.casestudy.banking.model.CustomerAccount;
import com.tandf.casestudy.banking.model.SavingAccount;
public class CloneDemo {

    public static void main(String[] args) {
        try {
            Address address = new Address("12 MG Road", "Bangalore", "Karnataka", "560001");
            CustomerAccount customer = new CustomerAccount("C001", "Asha", "asha@example.com", "9876543210", address);
            SavingAccount original = new SavingAccount("SA-1001", customer, 5000.0);

            System.out.println("Shallow Clone");
            SavingAccount shallow = (SavingAccount) original.clone();

            System.out.println("Before mutating the shallow clone:");
            System.out.println("  original.customer.address = " + original.getCustomer().getAddress());
            System.out.println("  shallow.customer.address  = " + shallow.getCustomer().getAddress());
            
            shallow.getCustomer().getAddress().setCity("Mysore");
            shallow.getCustomer().setName("Asha (edited via clone)");

            System.out.println("After editing shallow.customer.address.city:");
            System.out.println("  original.customer.name    = " + original.getCustomer().getName());
            System.out.println("  original.customer.address = " + original.getCustomer().getAddress());
            System.out.println("  --> original was corrupted because the shallow clone shared references.");

            System.out.println();
            System.out.println("Deep Clone ");
            Address freshAddress = new Address("12 MG Road", "Bangalore", "Karnataka", "560001");
            CustomerAccount freshCustomer = new CustomerAccount("C001", "Asha", "asha@example.com", "9876543210", freshAddress);
            SavingAccount originalDeep = new SavingAccount("SA-1001", freshCustomer, 5000.0);

            SavingAccount deep = (SavingAccount) originalDeep.deepClone();

            System.out.println("Before mutating the deep clone:");
            System.out.println("  originalDeep.customer.address = " + originalDeep.getCustomer().getAddress());
            System.out.println("  deep.customer.address         = " + deep.getCustomer().getAddress());
            
            deep.getCustomer().getAddress().setCity("Mysore");
            deep.getCustomer().setName("Asha (edited via clone)");

            System.out.println("After editing deep.customer.address.city:");
            System.out.println("  originalDeep.customer.name    = " + originalDeep.getCustomer().getName());
            System.out.println("  originalDeep.customer.address = " + originalDeep.getCustomer().getAddress());
            System.out.println("  deep.customer.address         = " + deep.getCustomer().getAddress());

        } catch (CloneNotSupportedException e) {
            System.err.println("Clone failed: " + e.getMessage());
            e.printStackTrace();
        } catch (InvalidEmailException | InvalidPhoneNumberException e) {
            System.err.println("Could not construct customer: " + e.getMessage());
        }
    }
}
