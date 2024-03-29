package com.mindhub.HomeBanking.dtos;

import com.mindhub.HomeBanking.models.Client;
import com.mindhub.HomeBanking.models.ClientLoan;
import com.mindhub.HomeBanking.models.Loan;

import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<AccountDTO> accounts;
    private Set<ClientLoanDTO> loans;
    private Set<CardDTO> cards;
    private boolean isAdmin;
    public ClientDTO(Client client){
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts = client.getAccounts()
                .stream()
                .map(AccountDTO::new)
                .collect(Collectors.toSet());

        this.loans = client.getClientLoans()
                .stream()
                .map(ClientLoanDTO::new)
                .collect(Collectors.toSet());
        this.cards = client.getCards()
                .stream()
                .map(card -> new CardDTO(card)).collect(Collectors.toSet());
        this.isAdmin = client.isAdmin();
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }

    public Set<ClientLoanDTO> getLoans() {
        return loans;
    }

    public Set<CardDTO> getCards() {
        return cards;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
