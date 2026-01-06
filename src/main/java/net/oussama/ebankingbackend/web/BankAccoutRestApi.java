package net.oussama.ebankingbackend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.oussama.ebankingbackend.Execption.BankAccountNotfoundExecption;
import net.oussama.ebankingbackend.dtos.AccountHistroyDto;
import net.oussama.ebankingbackend.dtos.AccountOperationDto;
import net.oussama.ebankingbackend.dtos.BankAccountDto;
import net.oussama.ebankingbackend.services.BanKAccountServices;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class BankAccoutRestApi {
    private BanKAccountServices bankAccountServices;
    @GetMapping("/bankaccount/{id}")
    public BankAccountDto getBankAccountById(@PathVariable String id) throws BankAccountNotfoundExecption {
        return bankAccountServices.getBankAccount(id);
    }
    @GetMapping("/bankaccount/count")
    public int getBankAccountCount() throws BankAccountNotfoundExecption {
        return bankAccountServices.countBankAccount();
    }
    @GetMapping("/operation/count")
    public int getBankOperationsCount() throws BankAccountNotfoundExecption {
        return bankAccountServices.accountOperations();
    }
    @GetMapping("/bankaccount/totaloperation")
    public double getBankAccountTotalOperation() throws BankAccountNotfoundExecption {
        return bankAccountServices.countamoutAccount();
    }
    @GetMapping("/bankaccount")
    public List<BankAccountDto> getBankAccounts() throws BankAccountNotfoundExecption {
        return bankAccountServices.bankAccountslist();
    }
    @GetMapping("/accountoperation/{id}/operation")
    public List<AccountOperationDto> getAccountOperation(@PathVariable String id) throws BankAccountNotfoundExecption {
        return bankAccountServices.accountoperationslist(id);
    }
    @GetMapping("/accountoperation/{id}/pageOperation")
    public AccountHistroyDto getAccountHistory(@PathVariable String id, @RequestParam(name = "page",defaultValue = "0") int page,@RequestParam(name = "size",defaultValue = "6") int size) throws BankAccountNotfoundExecption {
        return bankAccountServices.historyaccount(id,page,size);
    }

}
