package com.mindhub.HomeBanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String name;
    private double maxAmount;
    @ElementCollection
    private List<Integer> payments;
    private Double loanPercentage;
    @OneToMany(mappedBy = "loan")
    private List<ClientLoan> clientLoans = new ArrayList<>();
    public Loan() {
    }

    public Loan(String name, double maxAmount, List<Integer> payments) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
    }

    public Loan(String name, double maxAmount, List<Integer> payments, Double loanPercentage) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.loanPercentage = loanPercentage;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public List<Client> getClients() {
        return clientLoans.stream()
                .map(clientLoan -> clientLoan.getClient())
                .collect(Collectors.toList());
    }

    public List<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void addClientLoan(ClientLoan client) {
        client.setLoan(this);
        this.clientLoans.add(client);
    }

    public Double getLoanPercentage() {
        return loanPercentage;
    }

    public void setLoanPercentage(Double loanPercentage) {
        this.loanPercentage = loanPercentage;
    }
}
