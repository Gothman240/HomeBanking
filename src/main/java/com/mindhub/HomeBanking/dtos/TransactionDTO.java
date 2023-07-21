package com.mindhub.HomeBanking.dtos;

import com.mindhub.HomeBanking.models.Transaction;
import com.mindhub.HomeBanking.models.TransactionType;

import java.time.LocalDateTime;

public class TransactionDTO {

    private long id;
    private Double amount;
    private String description;
    private LocalDateTime date;
    private TransactionType type;
    private Double currentAccountBalance;
    private boolean accountIsActive;

    public TransactionDTO() {
    }

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.date = transaction.getDate();
        this.type = transaction.getType();
        this.currentAccountBalance = transaction.getCurrentAccountBalance();
        this.accountIsActive = transaction.isAccountIsActive();
    }

    public long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public TransactionType getType() {
        return type;
    }

    public Double getCurrentAccountBalance() {
        return currentAccountBalance;
    }

    public boolean isAccountIsActive() {
        return accountIsActive;
    }
}
