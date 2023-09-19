package com.mindhub.HomeBanking.controllers;

import com.mindhub.HomeBanking.dtos.LoanApplicationDTO;
import com.mindhub.HomeBanking.dtos.LoanDTO;
import com.mindhub.HomeBanking.models.*;
import com.mindhub.HomeBanking.repositories.*;
import com.mindhub.HomeBanking.services.AccountService;
import com.mindhub.HomeBanking.services.ClientService;
import com.mindhub.HomeBanking.services.LoanService;
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
public class LoanController {
    @Autowired
    private LoanService loanService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @RequestMapping("/loans")
    public List<LoanDTO> getLoansDTO(){
        List<Loan> loans =  loanService.findAll();
        return loans.stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @Transactional
    @RequestMapping(value = "/loans", method = RequestMethod.POST)
    public ResponseEntity<Object> newClientLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication){

        if (loanApplicationDTO == null){
            return new ResponseEntity<>("There are empty fields.", HttpStatus.FORBIDDEN);
        }

        Client currentClient = clientService.findByEmail(authentication.getName());


        if (loanApplicationDTO.getAmount().isNaN() || loanApplicationDTO.getNumberAccountDestiny().isBlank() || loanApplicationDTO.getPayments() == 0){
            return new ResponseEntity<>("There are incorrect fields." , HttpStatus.FORBIDDEN);
        }
        Loan currentLoan = loanService.findById(loanApplicationDTO.getId_loan());
        if (currentLoan == null) {
            return new ResponseEntity<>("Please provide a correct loan.", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() > currentLoan.getMaxAmount() || loanApplicationDTO.getAmount() <= 0){
            return new ResponseEntity<>("Please verify the provided amount.", HttpStatus.FORBIDDEN);
        }
        if (currentLoan.getPayments().stream().noneMatch(payment -> payment.equals(loanApplicationDTO.getPayments()))){
            return new ResponseEntity<>("Incorrect payment.", HttpStatus.FORBIDDEN);
        }
        if (accountService.findByNumber(loanApplicationDTO.getNumberAccountDestiny()) == null){
            return new ResponseEntity<>("Invalid destination account.", HttpStatus.FORBIDDEN);
        }
        if (currentClient.getAccounts().stream().noneMatch(account -> account.getNumber().equals(loanApplicationDTO.getNumberAccountDestiny()))){
            return new ResponseEntity<>("The destination account is invalid.", HttpStatus.FORBIDDEN);
        }
        try{
            String message = currentLoan.getName().concat("Loan approved.");
            double basePercentage = currentLoan.getLoanPercentage();;
            double base = 1.0;
            int payments = loanApplicationDTO.getPayments();

            if (payments >= 12){
                base = 0.9;
            } else if (payments >= 6) {
                base = 0.8;
            }
            double finalPercentage = basePercentage * base;
            double percentage = loanApplicationDTO.getAmount() + (finalPercentage / 100) * loanApplicationDTO.getAmount();

            ClientLoan clientLoan = new ClientLoan(percentage, loanApplicationDTO.getPayments());
            Transaction transaction = new Transaction(loanApplicationDTO.getAmount(), message, LocalDateTime.now(), TransactionType.CREDIT, true);
            Account account = accountService.findByNumber(loanApplicationDTO.getNumberAccountDestiny());
            account.setBalance(account.getBalance() + loanApplicationDTO.getAmount());
            account.addTransactions(transaction);
            transactionService.save(transaction);
            currentLoan.addClientLoan(clientLoan);
            currentClient.addClientLoan(clientLoan);
            clientLoanRepository.save(clientLoan);
            return new ResponseEntity<>("Loan approved.", HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>("Please try again later.", HttpStatus.FORBIDDEN);
        }

    }

    @RequestMapping(value = "/loans/admin", method = RequestMethod.POST)
    public ResponseEntity<Object> newLoan(@RequestBody LoanDTO loanDTO){
        if(loanService.findByName(loanDTO.getName()) != null){
            return new ResponseEntity<>("The name already exists", HttpStatus.FORBIDDEN);
        }
        if (loanDTO.getMaxAmount() < 1000){
            return new ResponseEntity<>("Low balance", HttpStatus.BAD_REQUEST);
        }
        if (loanDTO.getPayments().size() < 2){
            return new ResponseEntity<>("Not enough installments", HttpStatus.BAD_REQUEST);
        }
        if (loanDTO.getLoanPercentage() < 5){
            return new ResponseEntity<>("Percentage is not valid", HttpStatus.BAD_REQUEST);
        }
        Loan loan = new Loan(loanDTO.getName(), loanDTO.getMaxAmount(), loanDTO.getPayments(), loanDTO.getLoanPercentage());
        loanService.save(loan);
        return new ResponseEntity<>("New loan created", HttpStatus.CREATED);
    }

}
