package com.mindhub.HomeBanking.services;

import com.mindhub.HomeBanking.models.Transaction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    void save(Transaction transaction);
    List<Transaction> findByAccountIsActiveTrue();
    List<Transaction> findByDateBetween(LocalDateTime date1, LocalDateTime  date2);
}
