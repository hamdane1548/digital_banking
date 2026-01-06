package net.oussama.ebankingbackend.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.oussama.ebankingbackend.Execption.BalanceSoldeinsuffisantExecption;
import net.oussama.ebankingbackend.Execption.BankAccountNotfoundExecption;
import net.oussama.ebankingbackend.Execption.CustomerNotFondExecption;
import net.oussama.ebankingbackend.dtos.*;
import net.oussama.ebankingbackend.entites.*;
import net.oussama.ebankingbackend.enums.OperationType;
import net.oussama.ebankingbackend.mappers.BankAccountMapperImpl;
import net.oussama.ebankingbackend.repositroy.BankAccountOperationRepositroy;
import net.oussama.ebankingbackend.repositroy.BankAccountRepositroy;
import net.oussama.ebankingbackend.repositroy.CustomersRepositroy;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountsServiceImpl implements BanKAccountServices{

    private CustomersRepositroy customersRepositroy;
    private BankAccountRepositroy bankAccountRepositroy;
    private BankAccountOperationRepositroy bankAccountOperationrepositro;
    private BankAccountMapperImpl datoMapper;
    @Override
    public CustomerDto saveCustomer(CustomerDto customerDto) {
        log.info("Saving customer");
        Customer customer=datoMapper.fromCustomerDto(customerDto);
        Customer savedCustomer=customersRepositroy.save(customer);
        return datoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentBankAccountDTO saveBankCurrentAccount(double SoldeIntial, double overDraft, Long CustomerId) throws  CustomerNotFondExecption {
        Customer customer = customersRepositroy.findById(CustomerId).orElseThrow(()->new CustomerNotFondExecption("Customer not found"));

        log.info("Saving bank account");
        CurrentAccount currentAccount=new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setDate(new Date());
        currentAccount.setBalance(SoldeIntial);
        currentAccount.setOverdraft(overDraft);
        currentAccount.setCustomer(customer);
         CurrentAccount currentAccount1=bankAccountRepositroy.save(currentAccount);
        return datoMapper.fromcurrentbankaccounr(currentAccount1);
    }

    @Override
    public SavingBankAccountDTO saveBankSavingAccount(double SoldeIntial, double interestRate, Long CustomerId) throws CustomerNotFondExecption {
        Customer customer =customersRepositroy.findById(CustomerId).orElseThrow(()->new CustomerNotFondExecption("Customer not found"));
        log.info("Saving bank account");
        SavingAccount savingAccount=new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setDate(new Date());
        savingAccount.setBalance(SoldeIntial);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        SavingAccount savingAccount1=bankAccountRepositroy.save(savingAccount);
        return datoMapper.fromsavingbankaccount(savingAccount1);
    }
    //la defference entre toList et Collect.tolist si le 1 cree une list
    //no modifable et la 2 si de cree une liste modfiable
    @Override
    public List<CustomerDto> listCustomers() {
        List<Customer> customers= customersRepositroy.findAll();
        List<CustomerDto>customerDtos=customers.stream().map(
                customer -> datoMapper.fromCustomer(customer)
        ).collect(Collectors.toList());
        return customerDtos;
    }
    @Override
    public int CountCustomers(){
        return customersRepositroy.findAll().size();
     }
     @Override
     public int countBankAccount(){
        return bankAccountRepositroy.findAll().size();
     }
     @Override
     public double countamoutAccount(){
        List<AccountOperation> amountoperations=bankAccountOperationrepositro.findAll();
        double suma_debit=amountoperations.stream()
                .filter(op -> op.getType() == OperationType.DEBIT)
               .mapToDouble(AccountOperation::getAmount)
               .sum();
        double sum_credit=amountoperations.stream()
                .filter(op -> op.getType() == OperationType.CREDIT)
                .mapToDouble(AccountOperation::getAmount)
                .sum();
         System.out.println(suma_debit);
         System.out.println(sum_credit);
         BigDecimal result = BigDecimal.valueOf(sum_credit)
                 .subtract(BigDecimal.valueOf(suma_debit))
                 .setScale(2, RoundingMode.HALF_UP);

         return result.doubleValue();

     }
     @Override
     public  int accountOperations(){
          return bankAccountOperationrepositro.findAll().size();
     }
    @Override
    public BankAccountDto getBankAccount(String Id) throws BankAccountNotfoundExecption {
        BankAccount bankAccount =bankAccountRepositroy.findById(Id).orElseThrow(()->new BankAccountNotfoundExecption("erreur leur de trouve le banke account"));
        if(bankAccount instanceof CurrentAccount){
            return datoMapper.fromcurrentbankaccounr((CurrentAccount)bankAccount);
        }else {
           return datoMapper.fromsavingbankaccount((SavingAccount)bankAccount);
        }
    }

    @Override
    public void debitAccount(String acountId, double amount, String description) throws BankAccountNotfoundExecption ,BalanceSoldeinsuffisantExecption{
        BankAccount bankAccount =bankAccountRepositroy.findById(acountId).orElseThrow(()->new BankAccountNotfoundExecption("erreur leur de trouve le banke account"));
        if(bankAccount.getBalance()<amount){
            throw new BalanceSoldeinsuffisantExecption("solde no insuffusant pour retire l'argent");
        }
        AccountOperation accountOperation= new  AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setDate(new Date());
        accountOperation.setAccount(bankAccount);
        bankAccountOperationrepositro.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepositroy.save(bankAccount);
    }

    @Override
    public void creditAccount(String acountId, double amount, String description) throws BankAccountNotfoundExecption {
        BankAccount bankAccount =bankAccountRepositroy.findById(acountId).orElseThrow(()->new BankAccountNotfoundExecption("erreur leur de trouve le banke account"));
        AccountOperation accountOperation= new  AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setDate(new Date());
        accountOperation.setAccount(bankAccount);
        bankAccountOperationrepositro.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepositroy.save(bankAccount);
    }

    @Override
    public void virementAccount(String acountIdSource, String accountIddestination, double amount) throws BankAccountNotfoundExecption {
       debitAccount(acountIdSource,amount,"transfere");
       creditAccount(acountIdSource,amount,"transfere");
    }
     @Override
    public List<BankAccountDto> bankAccountslist(){
        List<BankAccount> bankAccounts= bankAccountRepositroy.findAll();
        List<BankAccountDto> banckaccount=bankAccounts.stream().map(banking ->{
               if(banking instanceof CurrentAccount){
                   return datoMapper.fromcurrentbankaccounr((CurrentAccount)banking);
               }else {
                   return datoMapper.fromsavingbankaccount((SavingAccount)banking);
               }
        }).toList();
        return banckaccount;
    }
    @Override
    public  CustomerDto get_Customers(Long customerId) throws CustomerNotFondExecption {
         return datoMapper.fromCustomer(customersRepositroy.findById(customerId).orElseThrow(()-> new CustomerNotFondExecption("customer not found")));
    }
    @Override
    public CustomerDto updateCustomer(CustomerDto customerDto) {
        log.info("Saving customer");
        Customer customer=datoMapper.fromCustomerDto(customerDto);
        Customer savedCustomer=customersRepositroy.save(customer);
        return datoMapper.fromCustomer(savedCustomer);
    }
    @Override
    public void deletecutomers(Long id){
        log.info("Deleting customer");
        customersRepositroy.deleteById(id);
    }
    @Override
    public List<AccountOperationDto> accountoperationslist(String account){
     List<AccountOperation> accounto= bankAccountOperationrepositro.findByAccountId(account);
     return accounto.stream().map(op->datoMapper.fromAccountOperation(op)).collect(Collectors.toList());
    }
    @Override
    public AccountHistroyDto historyaccount(String id, int page, int size) throws BankAccountNotfoundExecption{
        BankAccount bankAccount=bankAccountRepositroy.findById(id).orElseThrow(()-> new BankAccountNotfoundExecption("account not found"));
        Page<AccountOperation> account=bankAccountOperationrepositro.findByAccountId(id, PageRequest.of(page, size));
        AccountHistroyDto accountHistroyDto=new AccountHistroyDto();
        accountHistroyDto.setOperations(account.getContent().stream().map(op->datoMapper.fromAccountOperation(op)).collect(Collectors.toList()));
        accountHistroyDto.setAccountId(bankAccount.getId());
        accountHistroyDto.setBalance(bankAccount.getBalance());
        accountHistroyDto.setPageSize(size);
        accountHistroyDto.setTotalPage(account.getTotalPages());
        accountHistroyDto.setCurrentPage(page);
        return accountHistroyDto;
    }
    @Override
    public List<CustomerDto> search(String name){
        List<Customer> customer=customersRepositroy.serachcusomers( name);
        List<CustomerDto> cutomerdto= customer.stream().map(
                c->{
                    return  datoMapper.fromCustomer(c);
                }
        ).toList();
        return cutomerdto;
    }
}
