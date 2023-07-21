package com.mindhub.HomeBanking.controllers;

import com.mindhub.HomeBanking.dtos.PDFTransactionDTO;
import com.mindhub.HomeBanking.models.Transaction;
import com.mindhub.HomeBanking.services.TransactionService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@RestController
@RequestMapping("/api")
public class PdfController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping(value = "/transactions/pdf/", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> downloadPdf(   @RequestParam(name = "date1") String date1,
                                                 @RequestParam(name = "date2") String date2
    ) throws IOException {

        LocalDateTime localDateTime1 = LocalDateTime.parse(date1);
        LocalDateTime localDateTime2 = LocalDateTime.parse(date2);


        List<Transaction> transactions = transactionService.findByDateBetween(localDateTime1, localDateTime2);

        byte[] pdfContent = generatePDF(transactions, localDateTime1, localDateTime2);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "transactions.pdf");

        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
    }

    public byte[] generatePDF(List<Transaction> transactions, LocalDateTime startDate, LocalDateTime endDate) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA, 12);

                contentStream.beginText();
                contentStream.newLineAtOffset(50, 700);
                contentStream.showText("Transacciones dentro del período de fechas:");
                contentStream.newLine();
                contentStream.showText("Fecha de inicio: " + startDate);
                contentStream.newLine();
                contentStream.showText("Fecha de fin: " + endDate);
                contentStream.newLine();
                contentStream.newLine();

                for (Transaction transaction : transactions) {
                    contentStream.showText(transaction.getDescription() + " - Monto: " + transaction.getAmount() + transaction.getType());
                    contentStream.newLine();
                }

                contentStream.endText();
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);

            return outputStream.toByteArray();
        }
    }

}


