package com.mindhub.HomeBanking.services.Implements;

import com.mindhub.HomeBanking.dtos.TransactionDTO;
import com.mindhub.HomeBanking.models.Transaction;
import com.mindhub.HomeBanking.repositories.TransactionRepository;
import com.mindhub.HomeBanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.MissingFormatArgumentException;

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
    public List<TransactionDTO> findByAccountIdAndDateBetween(Long accountId, LocalDateTime date1, LocalDateTime date2) {
        if (date1 != null && date2 != null) {
            return transactionRepository.findByAccountIdAndDateBetween(accountId, date1, date2);
        } else {
            throw new MissingFormatArgumentException("Check the submitted data");
        }
    }
}
