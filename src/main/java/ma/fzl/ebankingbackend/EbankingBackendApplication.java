package ma.fzl.ebankingbackend;

import ma.fzl.ebankingbackend.entities.*;
import ma.fzl.ebankingbackend.enums.AccountStatus;
import ma.fzl.ebankingbackend.enums.OperationType;
import ma.fzl.ebankingbackend.exceptions.BalanceNotSufficentException;
import ma.fzl.ebankingbackend.exceptions.BankAccountNotFoundException;
import ma.fzl.ebankingbackend.exceptions.CustomerNotFoundException;
import ma.fzl.ebankingbackend.repositories.AccountOperationRepository;
import ma.fzl.ebankingbackend.repositories.BankAccountRepository;
import ma.fzl.ebankingbackend.repositories.CustomerRepository;
import ma.fzl.ebankingbackend.services.BankAccountService;
import ma.fzl.ebankingbackend.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;


@SpringBootApplication
public class EbankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }
    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
        return args -> {
            Stream.of("Fatima", "Zahra", "Ghita").forEach(name->{
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                bankAccountService.saveCustomer(customer);
            });
            bankAccountService.listCustomer().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*90000,9000,customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random()*120000,5.5,customer.getId());
                    List<BankAccount> bankAccounts = bankAccountService.bankAccountsList();
                    for(BankAccount bankAccount:bankAccounts){
                        for (int  i=0; i<10 ; i++){
                            bankAccountService.credit(bankAccount.getId(), 10000+Math.random()*120000,"Credit");
                            bankAccountService.debit(bankAccount.getId(),1000+Math.random()*9000,"Debit");
                        }
                    }

                } catch (CustomerNotFoundException | BankAccountNotFoundException | BalanceNotSufficentException e) {
                   e.printStackTrace();
                }
            });
        };
    }
    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            AccountOperationRepository accountOperationRepository,
                            BankAccountRepository bankAccountRepository){
        return args ->{
            Stream.of("Hassan", "Yassine" , "Aicha").forEach(name->{
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name+ "@gmail.com");
                customerRepository.save(customer);
            });

            customerRepository.findAll().forEach(cust ->{
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*90000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(cust);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*90000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(cust);
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);
            });

            bankAccountRepository.findAll().forEach(acc ->{
                for(int i = 0 ; i<10 ; i++){
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random()*12000);
                    accountOperation.setType(Math.random()>0.5? OperationType.DEBIT : OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);
                }
            });
        };
    }

    }
