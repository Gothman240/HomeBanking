package com.mindhub.HomeBanking.controllers;

import com.mindhub.HomeBanking.dtos.AccountDTO;
import com.mindhub.HomeBanking.models.Account;
import com.mindhub.HomeBanking.models.Client;
import com.mindhub.HomeBanking.repositories.AccountRepository;
import com.mindhub.HomeBanking.repositories.ClientRepository;
import com.mindhub.HomeBanking.services.AccountService;
import com.mindhub.HomeBanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.findAll();
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id, Authentication authentication){

        Client client = clientService.findByEmail(authentication.getName());
        Account account = accountService.findById(id);
        return new AccountDTO(accountService.findById(id));
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> newAccount(Authentication authentication) {
         if(clientService.findByEmail(authentication.getName()).getAccounts().size() < 3){
             try {
                 Client client = clientService.findByEmail(authentication.getName());
                 String random;

                 do{
                     random = "VIN-" + String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999 + 1));

                 }while (accountService.existsByNumber(random));

                 Account account = new Account(random, 0.0);
                 client.addAccount(account);
                 accountService.save(account);
                 return new ResponseEntity<>(HttpStatus.CREATED);
             } catch (Exception e) {
                 return new ResponseEntity<>("Error creating account: " + e.getMessage() , HttpStatus.FORBIDDEN);
             }

         }else{
             return new ResponseEntity<>("Account limit reached", HttpStatus.FORBIDDEN);
         }
    }
}
