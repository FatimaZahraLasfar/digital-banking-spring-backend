package ma.fzl.ebankingbackend.repositories;

import ma.fzl.ebankingbackend.entities.AccountOperation;
import ma.fzl.ebankingbackend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountOperationRepository extends JpaRepository <AccountOperation, Long> {
    public List<AccountOperation> findByBankAccountId(String accountId);

}
