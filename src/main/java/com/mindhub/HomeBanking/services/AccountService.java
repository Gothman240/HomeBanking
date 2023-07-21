package com.mindhub.HomeBanking.services;

import com.mindhub.HomeBanking.dtos.AccountDTO;
import com.mindhub.HomeBanking.models.Account;
import com.mindhub.HomeBanking.models.Client;

import java.util.List;

public interface AccountService {
    List<AccountDTO> findAll();
    Account findById(Long id);
    AccountDTO getAccountDTO(Long id);
    boolean existsByNumber(String number);
    void save(Account account);
    Account findByNumber(String account);
    List<Account> findByClientAndIsActiveTrue(Client client);
}
