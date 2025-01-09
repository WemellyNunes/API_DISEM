package com.disem.API.controllers;

import com.disem.API.enums.OrdersServices.StatusEnum;
import com.disem.API.models.OrderServiceModel;
import com.disem.API.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api")
@CrossOrigin(origins = { "http://localhost:5173", "http://app-disem.com.s3-website-sa-east-1.amazonaws.com/"
}, allowedHeaders = "*")
public class ReportController {

    @Autowired
    ReportService reportService;

    @GetMapping("/{id}/report")
    public ResponseEntity<byte[]> gerarReport(@PathVariable Long id) {
        byte[] pdfContents = reportService.generateReport(id);

        String fileName;
        OrderServiceModel os = reportService.findOrderServiceById(id);
        if (os.getStatus() == StatusEnum.EM_ATENDIMENTO) {
            fileName = "programacao_os_" + id + ".pdf";
        } else {
            fileName = "relatorio_os_" + id + ".pdf";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(fileName)
                .build());

        return ResponseEntity.ok().headers(headers).body(pdfContents);
    }





}
