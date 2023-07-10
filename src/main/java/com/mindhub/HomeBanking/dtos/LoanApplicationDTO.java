package com.mindhub.HomeBanking.dtos;


public class LoanApplicationDTO {
    private Long id_loan;
    private Double amount;
    private Integer payments;
    private String numberAccountDestiny;

    public LoanApplicationDTO() {
    }

    public LoanApplicationDTO(Long id_loan, Double amount, int payments, String numberAccountDestiny) {
        this.id_loan = id_loan;
        this.amount = amount;
        this.payments = payments;
        this.numberAccountDestiny = numberAccountDestiny;
    }

    public Long getId_loan() {
        return id_loan;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getNumberAccountDestiny() {
        return numberAccountDestiny;
    }
}
