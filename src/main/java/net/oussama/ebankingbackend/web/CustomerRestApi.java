package net.oussama.ebankingbackend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.oussama.ebankingbackend.dtos.CustomerDto;
import net.oussama.ebankingbackend.entites.Customer;
import net.oussama.ebankingbackend.services.BanKAccountServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cutomer")
@AllArgsConstructor
@Slf4j
public class CustomerRestApi {
    private BanKAccountServices bankAccountServices;
     @GetMapping("/customers")
    public List<CustomerDto> customers(){
      return bankAccountServices.listCustomers();
    }
}
