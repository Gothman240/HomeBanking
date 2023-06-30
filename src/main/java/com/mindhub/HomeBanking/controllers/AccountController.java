package com.mindhub.HomeBanking.controllers;

import com.mindhub.HomeBanking.dtos.AccountDTO;
import com.mindhub.HomeBanking.models.Account;
import com.mindhub.HomeBanking.models.Client;
import com.mindhub.HomeBanking.repositories.AccountRepository;
import com.mindhub.HomeBanking.repositories.ClientRepository;
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
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountRepository.findAll()
                .stream()
                .map(AccountDTO::new)
                .collect(Collectors.toList());
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountRepository.findById(id)
                .map(AccountDTO::new)
                .orElse(null);
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> newAccount(Authentication authentication) {
         if(clientRepository.findByEmail(authentication.getName()).getAccounts().size() < 3){
             try {
                 Client client = clientRepository.findByEmail(authentication.getName());
                 String random;

                 do{
                     random = "VIN-" + String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999 + 1));

                 }while (accountRepository.existsByNumber(random));

                 Account account = new Account(random, 0.0);
                 client.addAccount(account);
                 accountRepository.save(account);
                 return new ResponseEntity<>(HttpStatus.CREATED);
             } catch (Exception e) {
                 return new ResponseEntity<>("Error creating account: " + e.getMessage() , HttpStatus.FORBIDDEN);
             }

         }else{
             return new ResponseEntity<>("Account limit reached", HttpStatus.FORBIDDEN);
         }
    }
}
