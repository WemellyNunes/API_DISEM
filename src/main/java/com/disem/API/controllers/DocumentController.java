package com.disem.API.controllers;

import com.disem.API.dtos.DocumentDTO;
import com.disem.API.models.DocumentModel;
import com.disem.API.models.OrderServiceModel;
import com.disem.API.services.DocumentService;
import com.disem.API.services.OrderServiceService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DocumentController {

    @Autowired
    DocumentService documentService;

    @Autowired
    OrderServiceService orderServiceService;

    @PostMapping("/documents")
    public ResponseEntity<Object> createDocument(@RequestBody @Valid DocumentDTO documentDTO) {
        Optional<OrderServiceModel> orderServiceModelOptional = orderServiceService.findById(documentDTO.getOrderService_id());

        if (orderServiceModelOptional.isEmpty()) {
            return new ResponseEntity<>("Ordem de serviço não enocntrada", HttpStatus.NOT_FOUND);
        }
        else {
            var document = new DocumentModel();
            BeanUtils.copyProperties(documentDTO, document);
            document.setOrderService(orderServiceModelOptional.get());
            return new ResponseEntity<>(documentService.save(document), HttpStatus.CREATED);
        }
    }


    @GetMapping("/documents")
    public ResponseEntity<Object> getAllDocuments(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC)Pageable pageable) {
        Page<DocumentModel> documents = documentService.findAll(pageable);

        if (documents.isEmpty()){
            return new ResponseEntity<>("Documentos não encontrados",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }


    @GetMapping("/documents/{id}")
    public ResponseEntity<Object> findById(@PathVariable(value = "id") UUID id) {
        Optional<DocumentModel> documentOptional = documentService.findById(id);

        if (documentOptional.isEmpty()) {
            return new ResponseEntity<>("Documento não encontrado", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(documentOptional.get(), HttpStatus.OK);
    }


    @DeleteMapping("/documents/{id}")
    public ResponseEntity<Object> deleteDocument(@PathVariable(value = "id") UUID id) {
        Optional<DocumentModel> documentOptional = documentService.findById(id);

        if (documentOptional.isEmpty()) {
            return new ResponseEntity<>("Dcomento não encontrado", HttpStatus.NOT_FOUND);
        }
        documentService.delete(documentOptional.get());
        return new ResponseEntity<>("Documento removido com sucesso", HttpStatus.OK);
    }


    @PutMapping("/documents/{id}")
    public ResponseEntity<Object> updateDocument(@PathVariable(value = "id") UUID id, @RequestBody @Valid DocumentDTO documentDTO) {
        Optional<DocumentModel> documentOptional = documentService.findById(id);

        if (documentOptional.isEmpty()) {
            return new ResponseEntity<>("Dcomento não encontrado", HttpStatus.NOT_FOUND);
        }
        else {
            var document = documentOptional.get();

            document.setNameFile(documentDTO.getNameFile());
            document.setDescription(documentDTO.getDescription());

            return new ResponseEntity<>(documentService.save(document), HttpStatus.OK);
        }
    }

}