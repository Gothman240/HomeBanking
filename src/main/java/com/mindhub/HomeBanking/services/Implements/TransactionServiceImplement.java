package com.mindhub.HomeBanking.services.Implements;

import com.mindhub.HomeBanking.models.Transaction;
import com.mindhub.HomeBanking.repositories.TransactionRepository;
import com.mindhub.HomeBanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImplement implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Override
    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> findByAccountIsActiveTrue() {
        return transactionRepository.findByAccountIsActiveTrue();
    }

    @Override
    public List<Transaction> findByDateBetween(LocalDateTime date1, LocalDateTime date2)  {

        return transactionRepository.findByDateBetween(date1, date2);

    }
}
