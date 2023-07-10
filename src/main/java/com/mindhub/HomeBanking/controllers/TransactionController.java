package com.mindhub.HomeBanking.controllers;

import com.mindhub.HomeBanking.dtos.TransactionDTO;
import com.mindhub.HomeBanking.models.Account;
import com.mindhub.HomeBanking.models.Client;
import com.mindhub.HomeBanking.models.Transaction;
import com.mindhub.HomeBanking.models.TransactionType;
import com.mindhub.HomeBanking.repositories.AccountRepository;
import com.mindhub.HomeBanking.repositories.ClientRepository;
import com.mindhub.HomeBanking.repositories.TransactionRepository;
import com.mindhub.HomeBanking.services.AccountService;
import com.mindhub.HomeBanking.services.ClientService;
import com.mindhub.HomeBanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;

    @Transactional
    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> newTransaction(@RequestParam Double amount, @RequestParam String description,
                                                 @RequestParam String originNumber, @RequestParam String destinyNumber, Authentication authentication){

        if (originNumber.isBlank()){
            return new ResponseEntity<>("numero de cuenta vacio" ,HttpStatus.FORBIDDEN);
        }
        if (destinyNumber.isBlank()){
            return new ResponseEntity<>("numero cuenta destino vacio", HttpStatus.FORBIDDEN);
        }
        if(description.isBlank()){
            return new ResponseEntity<>("descripcion vacia", HttpStatus.FORBIDDEN);
        }
        if (amount.isNaN() || amount <= 0.0){
            return new ResponseEntity<>("revisa el numero", HttpStatus.FORBIDDEN);
        }
        if (originNumber.equals(destinyNumber)){
            return new ResponseEntity<>("revisa las cuentas", HttpStatus.FORBIDDEN);
        }

        Account originAccount = accountService.findByNumber(originNumber);
        Account destinyAccount = accountService.findByNumber(destinyNumber);
        Client currentClient = clientService.findByEmail(authentication.getName());
        Client destinyClient = destinyAccount.getClient();

        if (originAccount == null){
            return new ResponseEntity<>("cuenta de origin no existe", HttpStatus.FORBIDDEN);
        }
        if (currentClient.getAccounts().stream().noneMatch(account -> account.getNumber().equals(originNumber))){
            return new ResponseEntity<>("la cuenta no coincide con el cliente actual", HttpStatus.FORBIDDEN );
        }
        if (originAccount.getBalance() < amount){
            return new ResponseEntity<>("monto no disponible", HttpStatus.FORBIDDEN);
        }
        try {
            Transaction originTransaction = new Transaction(amount,description, LocalDateTime.now(), TransactionType.DEBIT);
            Transaction destinyTransaction = new Transaction(amount, description, LocalDateTime.now(), TransactionType.CREDIT);

            transactionService.save(destinyTransaction);
            transactionService.save(originTransaction);

            originAccount.addTransactions(originTransaction);
            destinyAccount.addTransactions(destinyTransaction);
            originAccount.setBalance(originAccount.getBalance() - amount);
            destinyAccount.setBalance(destinyAccount.getBalance() + amount);

            accountService.save(originAccount);
            accountService.save(destinyAccount);

            return new ResponseEntity<>("transaccion realizada", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating transaction: " + e.getMessage() , HttpStatus.FORBIDDEN);
        }

    }
}
