package net.oussama.ebankingbackend.services;

import net.oussama.ebankingbackend.Execption.BankAccountNotfoundExecption;
import net.oussama.ebankingbackend.Execption.CustomerNotFondExecption;
import net.oussama.ebankingbackend.dtos.*;

import java.util.List;

public interface BanKAccountServices {
    public CustomerDto saveCustomer(CustomerDto customer);
    CurrentBankAccountDTO saveBankCurrentAccount(double SoldeIntial, double overDraft, Long CustomerId) throws CustomerNotFondExecption;
    SavingBankAccountDTO saveBankSavingAccount(double SoldeIntial, double interestRate, Long CustomerId) throws CustomerNotFondExecption;

    List<CustomerDto> listCustomers();

    int CountCustomers();

    BankAccountDto getBankAccount(String Id) throws BankAccountNotfoundExecption;
    void debitAccount(String acountId,double amount,String description) throws BankAccountNotfoundExecption;
    void creditAccount(String acountId,double amount,String description) throws BankAccountNotfoundExecption;
    void virementAccount(String acountIdSource,String accountIddestination,double amount) throws BankAccountNotfoundExecption;

    List<BankAccountDto> bankAccountslist();
    public CustomerDto get_Customers(Long customerId) throws CustomerNotFondExecption;

    CustomerDto updateCustomer(CustomerDto customerDto);

    void deletecutomers(Long id);

    List<AccountOperationDto> accountoperationslist(String id);

    AccountHistroyDto historyaccount(String id, int page, int size) throws BankAccountNotfoundExecption;

    List<CustomerDto> search(String keyword);

    int countBankAccount();

    double countamoutAccount();

    int accountOperations();
}
