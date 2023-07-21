package com.mindhub.HomeBanking.services.Implements;

import com.mindhub.HomeBanking.models.Loan;
import com.mindhub.HomeBanking.repositories.LoanRepository;
import com.mindhub.HomeBanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoanServiceImplement implements LoanService {
    @Autowired
    private LoanRepository loanRepository;
    @Override
    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

    @Override
    public Loan findByName(String name) {
        return loanRepository.findByName(name);
    }

    @Override
    public Loan findById(Long id) {
        return loanRepository.findById(id).orElse(null);
    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }

    @Override
    public void save(Loan loan) {
        loanRepository.save(loan);
    }
}
