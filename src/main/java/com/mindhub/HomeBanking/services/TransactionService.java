package com.mindhub.HomeBanking.services;

import com.mindhub.HomeBanking.models.Transaction;

public interface TransactionService {
    void save(Transaction transaction);
}
