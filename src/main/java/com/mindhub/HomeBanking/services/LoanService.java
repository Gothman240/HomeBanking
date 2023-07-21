package com.mindhub.HomeBanking.services;

import com.mindhub.HomeBanking.models.Loan;

import java.util.List;

public interface LoanService {
    List<Loan> findAll();
    Loan findByName(String name);
    Loan findById(Long id);
    boolean existsById(Long id);
    void save(Loan loan);
}
