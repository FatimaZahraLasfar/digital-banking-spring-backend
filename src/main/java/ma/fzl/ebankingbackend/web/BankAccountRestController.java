package ma.fzl.ebankingbackend.web;

import lombok.AllArgsConstructor;
import ma.fzl.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankAccountRestController {
    private BankAccountService bankAccountService;

    public BankAccountRestController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccoun(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccounts(){
        return bankAccountService.bankAccountsList();
    }
}

