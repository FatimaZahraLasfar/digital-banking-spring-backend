package ma.fzl.ebankingbackend.services;

import ma.fzl.ebankingbackend.dtos.BankAccountDTO;
import ma.fzl.ebankingbackend.dtos.CurrentBankAccountDTO;
import ma.fzl.ebankingbackend.dtos.CustomerDTO;
import ma.fzl.ebankingbackend.dtos.SavingBankAccountDTO;
import ma.fzl.ebankingbackend.entities.BankAccount;
import ma.fzl.ebankingbackend.entities.CurrentAccount;
import ma.fzl.ebankingbackend.entities.Customer;
import ma.fzl.ebankingbackend.entities.SavingAccount;
import ma.fzl.ebankingbackend.exceptions.BalanceNotSufficentException;
import ma.fzl.ebankingbackend.exceptions.BankAccountNotFoundException;
import ma.fzl.ebankingbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomer();
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description ) throws BankAccountNotFoundException, BalanceNotSufficentException;
    void credit(String accountId, double amount, String description ) throws BankAccountNotFoundException;
    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficentException;
    List<BankAccountDTO> bankAccountsList();

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);
}
