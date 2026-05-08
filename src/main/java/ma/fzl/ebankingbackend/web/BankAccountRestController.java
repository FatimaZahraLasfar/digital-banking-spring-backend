package ma.fzl.ebankingbackend.web;

import lombok.AllArgsConstructor;
import ma.fzl.ebankingbackend.dtos.AccountHistoryDTO;
import ma.fzl.ebankingbackend.dtos.AccountOperationDTO;
import ma.fzl.ebankingbackend.dtos.BankAccountDTO;
import ma.fzl.ebankingbackend.exceptions.BankAccountNotFoundException;
import ma.fzl.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class BankAccountRestController {
    private BankAccountService bankAccountService;

    public BankAccountRestController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccounts(){
        return bankAccountService.bankAccountsList();
    }

    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> getHistory (@PathVariable String accountId){
        return bankAccountService.accountHistory(accountId);
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistory (
            @PathVariable String accountId,
            @RequestParam(name="page" , defaultValue = "0") int page,
            @RequestParam(name="size", defaultValue = "5") int size)
            throws BankAccountNotFoundException{
        return bankAccountService.getAccountHistory(accountId,page,size);
    }
}

