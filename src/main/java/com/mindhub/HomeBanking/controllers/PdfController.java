package com.mindhub.HomeBanking.controllers;

import com.mindhub.HomeBanking.dtos.TransactionDTO;
import com.mindhub.HomeBanking.services.TransactionService;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;

@RestController
@RequestMapping("/api")
public class PdfController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping(value = "/pdf/{accountId}")
    public ResponseEntity<Object> downloadPdf(@PathVariable Long accountId,
                                              @RequestParam String date1,
                                              @RequestParam String date2) throws IOException {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSSSS");
            LocalDateTime dateTime1 = LocalDateTime.parse(date1, formatter);
            LocalDateTime dateTime2 = LocalDateTime.parse(date2, formatter);

            LocalDateTime startOfDayDateTime1 = dateTime1.with(LocalTime.MIN);
            LocalDateTime endOfDayDateTime2 = dateTime2.with(LocalTime.MAX);

            List<TransactionDTO> transactions = transactionService.findByAccountIdAndDateBetween(accountId, startOfDayDateTime1, endOfDayDateTime2);

            // Crear un documento PDF
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            document.open();

            // Agregar título
            document.add(new Paragraph("Transacciones cuenta con ID: " + accountId));

            // Crear una tabla para mostrar las transacciones
            PdfPTable table = new PdfPTable(4); // 4 columnas para amount, description, date y type

            // Agregar encabezados de columna
            table.addCell("Amount");
            table.addCell("Description");
            table.addCell("Date");
            table.addCell("Type");

            // Agregar las transacciones a la tabla
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (TransactionDTO transaction : transactions) {
                table.addCell(transaction.getAmount().toString());
                table.addCell(transaction.getDescription());
                table.addCell(transaction.getDate().format(dateFormatter));
                table.addCell(transaction.getType().toString());
            }

            // Agregar la tabla al documento
            document.add(table);

            // Cerrar el documento
            document.close();

            // Obtener el contenido del PDF en bytes
            byte[] pdfBytes = baos.toByteArray();

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=transacciones.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(pdfBytes.length)
                    .body(pdfBytes);
        } catch (DocumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new byte[0]);
        }
    }
}


