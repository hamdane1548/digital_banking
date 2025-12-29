package net.oussama.ebankingbackend.services;

import net.oussama.ebankingbackend.Execption.BankAccountNotfoundExecption;
import net.oussama.ebankingbackend.Execption.CustomerNotFondExecption;
import net.oussama.ebankingbackend.dtos.CustomerDto;
import net.oussama.ebankingbackend.entites.BankAccount;
import net.oussama.ebankingbackend.entites.CurrentAccount;
import net.oussama.ebankingbackend.entites.Customer;
import net.oussama.ebankingbackend.entites.SavingAccount;

import java.util.List;

public interface BanKAccountServices {
    public Customer saveCustomer(Customer customer);
    CurrentAccount saveBankCurrentAccount(double SoldeIntial, double overDraft, Long CustomerId) throws CustomerNotFondExecption;
    SavingAccount saveBankSavingAccount(double SoldeIntial, double interestRate, Long CustomerId) throws CustomerNotFondExecption;

    List<CustomerDto> listCustomers();
    BankAccount getBankAccount(String Id) throws BankAccountNotfoundExecption;
    void debitAccount(String acountId,double amount,String description) throws BankAccountNotfoundExecption;
    void creditAccount(String acountId,double amount,String description) throws BankAccountNotfoundExecption;
    void virementAccount(String acountIdSource,String accountIddestination,double amount) throws BankAccountNotfoundExecption;

    List<BankAccount> bankAccountslist();
}
