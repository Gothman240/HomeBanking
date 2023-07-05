package com.mindhub.HomeBanking.controllers;

import com.mindhub.HomeBanking.dtos.TransactionDTO;
import com.mindhub.HomeBanking.models.Account;
import com.mindhub.HomeBanking.models.Client;
import com.mindhub.HomeBanking.models.Transaction;
import com.mindhub.HomeBanking.models.TransactionType;
import com.mindhub.HomeBanking.repositories.AccountRepository;
import com.mindhub.HomeBanking.repositories.ClientRepository;
import com.mindhub.HomeBanking.repositories.TransactionRepository;
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
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;


    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> newTransaction(@RequestParam Double amount, @RequestParam String description,
                                                 @RequestParam String originNumber, @RequestParam String destinyNumber, Authentication authentication){
        Account originAccount = accountRepository.findByNumber(originNumber);
        Account destinyAccount = accountRepository.findByNumber(destinyNumber);
        Client currentClient = clientRepository.findByEmail(authentication.getName());


        if (originAccount == null){
            return new ResponseEntity<>("cuenta de origin no existe", HttpStatus.FORBIDDEN);
        }
        if(destinyAccount == null){
            return new ResponseEntity<>("cuenta de destino no existe", HttpStatus.FORBIDDEN);
        }
        Client destinyClient = destinyAccount.getClient();
        if (destinyClient == null){
            return new ResponseEntity<>("cuenta de destino no existe", HttpStatus.FORBIDDEN);
        }
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
        if (currentClient.getAccounts().stream().noneMatch(account -> account.getNumber().equals(originNumber))){
            return new ResponseEntity<>("la cuenta no coincide con el cliente actual", HttpStatus.FORBIDDEN );
        }
        if (originAccount.getBalance() < amount){
            return new ResponseEntity<>("monto no disponible", HttpStatus.FORBIDDEN);
        }
        Transaction originTransaction = new Transaction(amount,description, LocalDateTime.now(), TransactionType.DEBIT);
        Transaction destinyTransaction = new Transaction(amount, description, LocalDateTime.now(), TransactionType.CREDIT);

        transactionRepository.save(destinyTransaction);
        transactionRepository.save(originTransaction);

        originAccount.addTransactions(originTransaction);
        destinyAccount.addTransactions(destinyTransaction);
        originAccount.setBalance(originAccount.getBalance() - amount);
        destinyAccount.setBalance(destinyAccount.getBalance() + amount);

        accountRepository.save(originAccount);
        accountRepository.save(destinyAccount);

        return new ResponseEntity<>("transaccion realizada", HttpStatus.CREATED);

    }
}
