package com.mindhub.HomeBanking.dtos;

public class PayLoanDTO {
    Long loan_id;
    Double amount;
    String accountNumber;
    int payment;

    public PayLoanDTO() {
    }

    public PayLoanDTO(Long loan_id, Double amount, String accountNumber, int payment) {
        this.loan_id = loan_id;
        this.amount = amount;
        this.accountNumber = accountNumber;
        this.payment = payment;
    }

    public Long getLoan_id() {
        return loan_id;
    }

    public Double getAmount() {
        return amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public int getPayment() {
        return payment;
    }
}
