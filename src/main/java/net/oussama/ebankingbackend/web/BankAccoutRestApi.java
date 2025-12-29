package net.oussama.ebankingbackend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.oussama.ebankingbackend.services.BanKAccountServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bankaccount")
@AllArgsConstructor
@Slf4j
public class BankAccoutRestApi {
    private BanKAccountServices bankAccountServices;

}
