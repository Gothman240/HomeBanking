package com.mindhub.HomeBanking.controllers;

import com.mindhub.HomeBanking.dtos.PayLoanDTO;
import com.mindhub.HomeBanking.dtos.TransactionDTO;
import com.mindhub.HomeBanking.models.*;
import com.mindhub.HomeBanking.repositories.AccountRepository;
import com.mindhub.HomeBanking.repositories.ClientLoanRepository;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ClientLoanRepository clientLoanRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientRepository clientRepository;

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

        if (originAccount == null || !originAccount.isActive()){
            return new ResponseEntity<>("cuenta de origin no existe", HttpStatus.FORBIDDEN);
        }
        if (currentClient.getAccounts().stream().noneMatch(account -> account.getNumber().equals(originNumber))){
            return new ResponseEntity<>("la cuenta no coincide con el cliente actual", HttpStatus.FORBIDDEN );
        }
        if (originAccount.getBalance() < amount){
            return new ResponseEntity<>("monto no disponible", HttpStatus.FORBIDDEN);
        }
        if (!destinyAccount.isActive()) {
            return new ResponseEntity<>("error cuenta de destino", HttpStatus.FORBIDDEN);
        }

        try {
            String customDescription = originNumber + ": " + description;

            Transaction originTransaction = new Transaction(amount, customDescription, LocalDateTime.now(), TransactionType.DEBIT, originAccount.getBalance() - amount, true);
            Transaction destinyTransaction = new Transaction(amount, customDescription, LocalDateTime.now(), TransactionType.CREDIT, destinyAccount.getBalance()  + amount, true);

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

    @Transactional
    @RequestMapping(value = "/transactions/loan", method = RequestMethod.POST)
    public ResponseEntity<Object> paidLoan(@RequestBody PayLoanDTO payLoanDTO, Authentication authentication){

        if (payLoanDTO == null){
            return new ResponseEntity<>("Review the submitted data", HttpStatus.BAD_REQUEST);
        }

        Client currentClient = clientService.findByEmail(authentication.getName());
        List<Loan> loans = currentClient.getClientLoans().stream().map(clientLoan -> clientLoan.getLoan()).collect(Collectors.toList());

        boolean hasLoan = loans.stream().anyMatch(loan -> loan.getId().equals(payLoanDTO.getLoan_id()));

        if (hasLoan){
            Account currentAccount = currentClient.getAccounts().stream().filter(account -> account.getNumber().equals(payLoanDTO.getAccountNumber())).findFirst().orElse(null);

            if (currentAccount == null){
                return new ResponseEntity<>("Error retrieving information", HttpStatus.FORBIDDEN);
            }
            ClientLoan clientLoan = currentClient.getClientLoans().stream().filter(clientLoan1 -> clientLoan1.getId() == payLoanDTO.getLoan_id()).findAny().orElse(null);
            if(clientLoan == null){
                return new ResponseEntity<>("Error retrieving information", HttpStatus.FORBIDDEN);
            }

            if (currentAccount.getBalance() < payLoanDTO.getAmount()){
                return new ResponseEntity<>("Payment processing error", HttpStatus.FORBIDDEN);
            }

            clientLoan.setTotalPayments(payLoanDTO.getPayment() - 1 );

            currentAccount.setBalance(currentAccount.getBalance() - payLoanDTO.getAmount());
            Transaction transaction = new Transaction(payLoanDTO.getAmount(), clientLoan.getLoan().getName() + " loan payment." ,LocalDateTime.now(), TransactionType.DEBIT,currentAccount.getBalance() - payLoanDTO.getAmount(), true);


            transactionService.save(transaction);
            currentAccount.addTransactions(transaction);
            accountService.save(currentAccount);
            clientLoanRepository.save(clientLoan);


            return new ResponseEntity<>("Paid", HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>("Issues with account data", HttpStatus.BAD_REQUEST);
        }


  }

}
