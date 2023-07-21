package com.mindhub.HomeBanking.controllers;

import com.mindhub.HomeBanking.models.Transaction;
import com.mindhub.HomeBanking.services.TransactionService;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

@RestController
@RequestMapping("/api")
public class PdfController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping(value = "/{accountId}/pdf")
    public ResponseEntity<byte[]> downloadPdf( @PathVariable Long accountId,
                                               @RequestParam String date1,
                                               @RequestParam String date2) throws IOException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSSSS");
        LocalDateTime dateTime1 = LocalDateTime.parse(date1, formatter);
        LocalDateTime dateTime2 = LocalDateTime.parse(date2, formatter);

        LocalDateTime startOfDayDateTime1 = dateTime1.with(LocalTime.MIN);
        LocalDateTime endOfDayDateTime2 = dateTime2.with(LocalTime.MAX);


        System.out.println("dateTime1: " + dateTime1.format(formatter));
        System.out.println("dateTime2: " + dateTime2.format(formatter));
        System.out.println("startOfDayDateTime1: " + startOfDayDateTime1.format(formatter));
        System.out.println("endOfDayDateTime2: " + endOfDayDateTime2.format(formatter));


        List<Transaction> transactions = transactionService.findByAccountIdAndDateBetween( accountId, startOfDayDateTime1, endOfDayDateTime2);
        System.out.println("Lista de transacciones del repositorio: " + transactions.size());

        byte[] pdfBytes = generatePDF(accountId, transactions);
        System.out.println("PDF size: " + pdfBytes.length);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "transactions.pdf");
        headers.setContentLength(pdfBytes.length);

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    private byte[] generatePDF(Long accountId, List<Transaction> transactions) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Document document = new Document();
            PdfWriter.getInstance(document, baos);

            document.open();

            document.add(new Paragraph("Transacciones cuenta con ID: " + accountId));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (Transaction transaction : transactions) {
                String formattedDateTime = transaction.getDate().format(formatter);
                Paragraph paragraph = new Paragraph(formattedDateTime + " - " + transaction.getDescription());
                document.add(paragraph);
            }

            document.close();

            return baos.toByteArray();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}


