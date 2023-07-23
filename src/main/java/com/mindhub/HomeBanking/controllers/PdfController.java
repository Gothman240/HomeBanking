package com.mindhub.HomeBanking.controllers;

import com.mindhub.HomeBanking.dtos.TransactionDTO;
import com.mindhub.HomeBanking.models.Transaction;
import com.mindhub.HomeBanking.services.TransactionService;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@RestController
@RequestMapping("/api")
public class PdfController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping(value = "/{accountId}/pdf")
    public ResponseEntity<Object> downloadPdf(@PathVariable Long accountId,
                                              @RequestParam String date1,
                                              @RequestParam String date2) throws IOException {
        try{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSSSS");
        LocalDateTime dateTime1 = LocalDateTime.parse(date1, formatter);
        LocalDateTime dateTime2 = LocalDateTime.parse(date2, formatter);

        LocalDateTime startOfDayDateTime1 = dateTime1.with(LocalTime.MIN);
        LocalDateTime endOfDayDateTime2 = dateTime2.with(LocalTime.MAX);

        System.out.println("dateTime1: " + dateTime1.format(formatter));
        System.out.println("dateTime2: " + dateTime2.format(formatter));
        System.out.println("startOfDayDateTime1: " + startOfDayDateTime1.format(formatter));
        System.out.println("endOfDayDateTime2: " + endOfDayDateTime2.format(formatter));

        List<TransactionDTO> transactions = transactionService.findByAccountIdAndDateBetween(accountId, startOfDayDateTime1, endOfDayDateTime2);
        System.out.println("Lista de transacciones del repositorio: " + transactions.size());

            byte[] pdfBytes = generatePDF(accountId, transactions);
            System.out.println("PDF size: " + pdfBytes.length);

//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_PDF);
//            headers.setContentDispositionFormData("inline", "transactions.pdf");
//            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, HttpStatus.OK);
        } catch (DocumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new byte[0]);
    }
    }

    private byte[] generatePDF(Long accountId, List<TransactionDTO> transactions) throws DocumentException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // Crear un nuevo objeto Document
            Document document = new Document();
            // Asociar el objeto Document con el flujo de salida ByteArrayOutputStream
            PdfWriter.getInstance(document, baos);

            // Abrir el documento
            document.open();

            // Agregar un párrafo al documento con el ID de la cuenta
            document.add(new Paragraph("Transacciones cuenta con ID: " + accountId));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (TransactionDTO transaction : transactions) {
                // Formatear la fecha de la transacción en el formato especificado
                String formattedDateTime = transaction.getDate().format(formatter);

                // Crear un nuevo párrafo con la fecha formateada y la descripción de la transacción
                Paragraph paragraph = new Paragraph(formattedDateTime + " - " + transaction.getDescription());

                // Agregar el párrafo al documento
                document.add(paragraph);
            }

            // Cerrar el documento
            document.close();

            return baos.toByteArray();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            throw new DocumentException("Error al generar el PDF", e);
        }
    }
}


