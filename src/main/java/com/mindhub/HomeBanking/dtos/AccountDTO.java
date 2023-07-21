package com.mindhub.HomeBanking.dtos;

import com.mindhub.HomeBanking.models.Account;
import com.mindhub.HomeBanking.models.AccountType;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class AccountDTO{

    private long id;
    private String number;
    private LocalDate creationDate;
    private Double balance;
    private boolean isActive;
    private AccountType accountType;
    private List<TransactionDTO> transactions;

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.isActive = account.isActive();
        this.transactions = account.getTransactions()
                .stream()
                .map(TransactionDTO::new)
                .collect(Collectors.toList());
        this.accountType = account.getAccountType();
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Double getBalance() {
        return balance;
    }
    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public AccountType getAccountType() {
        return accountType;
    }
}
