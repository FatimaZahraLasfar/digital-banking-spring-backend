package ma.fzl.ebankingbackend.services;

import ma.fzl.ebankingbackend.entities.BankAccount;
import ma.fzl.ebankingbackend.entities.Customer;

import java.util.List;

public interface BankAccountService {
    Customer saveCustomer(Customer customer);
    BankAccount saveBankAccount(double initialBalance, String type, Long customerId);
    List<Customer> ListCustomer();
    BankAccount getBankAccount(String accountId);
    void debit(String accountId, double amount, String description );
    void credit(String accountId, double amount, String description );
    void transfer(String accountIdSource, String accountIdDestination, double amount);
}
