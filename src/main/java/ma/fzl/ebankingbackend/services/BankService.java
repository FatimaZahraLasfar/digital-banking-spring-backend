package ma.fzl.ebankingbackend.services;

import jakarta.transaction.Transactional;
import ma.fzl.ebankingbackend.entities.BankAccount;
import ma.fzl.ebankingbackend.entities.CurrentAccount;
import ma.fzl.ebankingbackend.entities.SavingAccount;
import ma.fzl.ebankingbackend.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BankService {
    @Autowired
    private BankAccountRepository bankAccountRepository;
    public void consulter(){
        BankAccount bankAccount =
                bankAccountRepository.findById("8080b928-846f-48d4-86ab-8a5038b05d53").orElse(null);
        if(bankAccount !=null) {
            System.out.println("*****************************");
            System.out.println(bankAccount.getId());
            System.out.println(bankAccount.getBalance());
            System.out.println(bankAccount.getStatus());
            System.out.println(bankAccount.getCreatedAt());
            System.out.println(bankAccount.getCustomer().getName());
            System.out.println(bankAccount.getClass().getSimpleName());
            if (bankAccount instanceof CurrentAccount) {
                System.out.println("Over Draft => " + ((CurrentAccount) bankAccount).getOverDraft());
            } else if (bankAccount instanceof SavingAccount) {
                System.out.println("Interest Rate=> " + ((SavingAccount) bankAccount).getInterestRate());
            }

            bankAccount.getAccountOperations().forEach(op -> {
                System.out.println(op.getType() + "\t" + op.getOperationDate()
                        + "\t" + op.getAmount());
            });
        }
    }
}
