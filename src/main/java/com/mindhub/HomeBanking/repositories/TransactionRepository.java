package com.mindhub.HomeBanking.repositories;

import com.mindhub.HomeBanking.models.Client;
import com.mindhub.HomeBanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountIsActiveTrue();
    List<Transaction> findByAccountClient(Client client);
    List<Transaction> findByAccountIdAndDateBetween(Long accountId, LocalDateTime date1, LocalDateTime  date2);

}
