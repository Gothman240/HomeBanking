package com.mindhub.HomeBanking.repositories;

import com.mindhub.HomeBanking.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface LoanRepository extends JpaRepository<Loan, Long> {
    boolean existsById(Long id);
    Optional<Loan> findById(Long id);
}
