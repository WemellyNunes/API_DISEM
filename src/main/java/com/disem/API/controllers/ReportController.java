package com.disem.API.controllers;

import com.disem.API.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ReportController {

    @Autowired
    ReportService reportService;

    @GetMapping("/{id}/report")
    public ResponseEntity<byte[]> gerarReport(@PathVariable Long id) {
        byte[] pdfContents = reportService.generateReport(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "relatorio_os" + id + ".pdf");

        return ResponseEntity.ok().headers(headers).body(pdfContents);
    }
}
