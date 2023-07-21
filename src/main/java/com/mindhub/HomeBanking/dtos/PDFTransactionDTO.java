package com.mindhub.HomeBanking.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PDFTransactionDTO {
    private LocalDateTime date1;
    private LocalDateTime date2;

    public PDFTransactionDTO() {
    }

    public PDFTransactionDTO(LocalDateTime date1, LocalDateTime date2) {
        this.date1 = date1;
        this.date2 = date2;
    }

    public LocalDateTime getDate1() {
        return date1;
    }

    public LocalDateTime getDate2() {
        return date2;
    }
}
