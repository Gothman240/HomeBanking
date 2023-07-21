package com.mindhub.HomeBanking.controllers;

import com.mindhub.HomeBanking.dtos.AccountDTO;
import com.mindhub.HomeBanking.models.Account;
import com.mindhub.HomeBanking.models.AccountType;
import com.mindhub.HomeBanking.models.Client;
import com.mindhub.HomeBanking.models.Transaction;
import com.mindhub.HomeBanking.repositories.TransactionRepository;
import com.mindhub.HomeBanking.services.AccountService;
import com.mindhub.HomeBanking.services.ClientService;
import com.mindhub.HomeBanking.services.TransactionService;
import com.mindhub.HomeBanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionRepository transactionRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.findAll();
    }

    @RequestMapping("/active/accounts")
    public List<AccountDTO> getActiveAccountsDTO(Authentication authentication){

        Client client = clientService.findByEmail(authentication.getName());
        List<Account> accounts = client.getAccounts().stream().collect(Collectors.toList());
        List<Account> activeAccounts = accounts.stream().filter(Account::isActive).collect(Collectors.toList());
        List<AccountDTO> accounts1 = activeAccounts.stream().map(AccountDTO::new).collect(Collectors.toList());

        return accounts1;

        //return accounts.stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id, Authentication authentication){

        Client client = clientService.findByEmail(authentication.getName());

        Account account = accountService.findById(id);

        if (!account.isActive()){
            throw new AccessDeniedException("Cuenta no disponible");
        }

        if (client.getAccounts().stream().anyMatch(account1 -> account1.getNumber().equals(account.getNumber()))){
            return new AccountDTO(account);
        }
        throw new AccessDeniedException("No tienes acceso a la cuenta solicitada.");


    }

    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> newAccount(@RequestParam AccountType accountType, Authentication authentication) {

        Client client = clientService.findByEmail(authentication.getName());

        List<Account> accounts = accountService.findByClientAndIsActiveTrue(client);

         if(accounts.size() < 3){
             try {
                 String random;
                 do {
                     random = CardUtils.getRandomAccountNumber();

                 }while (accountService.existsByNumber(random));

                 Account account = new Account(random, true, accountType);
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

    @RequestMapping(value = "/accounts/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Object> deleteAccount(@PathVariable Long id){
            Account account = accountService.findById(id);
            if (account == null){
                return new ResponseEntity<>("Verifica la cuenta seleccionada", HttpStatus.BAD_REQUEST);
            }
            if (account.getBalance() > 0){
                return new ResponseEntity<>("La cuenta tiene efectivo", HttpStatus.BAD_REQUEST);
            }

        try{
            List<Transaction> transactionSet = account.getTransactions().stream().collect(Collectors.toList());
            transactionSet.forEach(transaction -> transaction.setAccountIsActive(false));
            transactionSet.forEach(transaction -> transactionService.save(transaction));

            transactionRepository.saveAll(transactionSet);

            account.setActive(false);
            accountService.save(account);
            return new ResponseEntity<>("Cuenta eliminada", HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
