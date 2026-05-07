package ma.fzl.ebankingbackend.dtos;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.fzl.ebankingbackend.entities.AccountOperation;
import ma.fzl.ebankingbackend.entities.Customer;
import ma.fzl.ebankingbackend.enums.AccountStatus;

import java.util.Date;
import java.util.List;

@Data
public  class SavingBankAccountDTO {
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double interestRate;
}
