package com.mindhub.HomeBanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private Double amount;
    private String description;
    private LocalDateTime date;
    private TransactionType type;
    private Double currentAccountBalance;
    private boolean accountIsActive;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    public Transaction() {
    }

    public Transaction(Double amount, String description, LocalDateTime date, TransactionType type, Double currentAccountBalance, boolean accountIsActive) {
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.type = type;
        this.currentAccountBalance = currentAccountBalance;
        this.accountIsActive = accountIsActive;
    }

    public Transaction(Double amount, String description, LocalDateTime date, TransactionType type, boolean accountIsActive) {
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.type = type;
        this.accountIsActive = accountIsActive;
    }

    public long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
    @JsonIgnore
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Double getCurrentAccountBalance() {
        return currentAccountBalance;
    }

    public void setCurrentAccountBalance(Double currentAccountBalance) {
        this.currentAccountBalance = currentAccountBalance;
    }

    public boolean isAccountIsActive() {
        return accountIsActive;
    }

    public void setAccountIsActive(boolean accountIsActive) {
        this.accountIsActive = accountIsActive;
    }
}
