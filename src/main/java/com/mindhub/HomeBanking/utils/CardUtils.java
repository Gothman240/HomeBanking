package com.mindhub.HomeBanking.utils;

import com.mindhub.HomeBanking.dtos.LoanApplicationDTO;
import com.mindhub.HomeBanking.models.Loan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public final class CardUtils {
    private CardUtils() {
    }
    public static int getCvv() {
        return (int)Math.floor(Math.random()*999-111)+111;
    }
    public static String getCardNumber() {
        return ((int)((Math.random() * (9999 - 1000))+1000))
                + "-" + ((int)((Math.random() * (9999 - 1000))+1000))
                + "-" + ((int)((Math.random() * (9999 - 1000))+1000))
                + "-" + ((int)((Math.random() * (9999 - 1000))+1000));
    }
    public static String getRandomAccountNumber() {
        return "VIN-" + String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999 + 1));
    }
    public static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }

    public static double getPercentage(LoanApplicationDTO loanApplicationDTO, Loan currentLoan) {
        double basePercentage = currentLoan.getLoanPercentage();

        double base = 1.0;
        int payments = loanApplicationDTO.getPayments();

        if (payments >= 12){
            base = 0.9;
        } else if (payments >= 6) {
            base = 0.8;
        }
        double finalPercentage = basePercentage * base;
        return loanApplicationDTO.getAmount() + (finalPercentage / 100) * loanApplicationDTO.getAmount();
    }
}
