package ma.fzl.ebankingbackend.mappers;

import ma.fzl.ebankingbackend.dtos.CustomerDTO;
import ma.fzl.ebankingbackend.entities.Customer;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

//MapStruct
@Service
public class BankAccountMapperImpl {
    public CustomerDTO fromCustomer (Customer customer){
        CustomerDTO customerDTO  =new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }
    public Customer fromCustomerDTO (CustomerDTO customerDTO){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO,customer);
        return customer;
    }

    public SavingBankAccountDTO fromSavingBankAccount (SavingAccount savingAccount){

    }

    public SavingAccount fromSavingAccountDTO (SavingBankAccountDTO savingBankAccountDTO){

    }

    public CurrentBankAccountDTO fromCurrentBankAccount (CurrentAccount currentAccount){

    }

    public CurrentAccount fromCurrentAccountDTO (CurrentBankAccountDTO currentBankAccountDTO){

    }
}

