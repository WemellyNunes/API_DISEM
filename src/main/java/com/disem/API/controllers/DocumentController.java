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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = { "*"
}, allowedHeaders = "*")
public class DocumentController {

    @Autowired
    DocumentService documentService;

    @Autowired
    OrderServiceService orderServiceService;

    @PostMapping("/uploadDocument")
    public ResponseEntity<Object> createDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("orderServiceId") Long orderServiceId
    ) {
        final long MAX_FILE_SIZE = 5 * 1024 * 1024;

        if (file.getSize() > MAX_FILE_SIZE) {
            return new ResponseEntity<>("Arquivo excede o limite de 5MB", HttpStatus.BAD_REQUEST);
        }

        if (file.isEmpty()){
            return new ResponseEntity<>("nenhum arquivo enviado", HttpStatus.BAD_REQUEST);
        }

        Optional<OrderServiceModel> orderServiceModelOptional = orderServiceService.findById(orderServiceId);

        if (orderServiceModelOptional.isEmpty()) {
            return new ResponseEntity<>("Ordem de serviço não encontrada", HttpStatus.NOT_FOUND);
        }

        try {
            String uploadDir = System.getProperty("user.dir") + "/uploads/documents/";

            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            String fileName = file.getOriginalFilename();
            Path path = Paths.get(uploadDir + fileName);
            Files.write(path, file.getBytes());

            String documentPath = "/uploads/documents/" + fileName;
            DocumentModel documentModel = new DocumentModel();
            documentModel.setNameFile(documentPath);
            documentModel.setDescription("x");
            documentModel.setOrderService(orderServiceModelOptional.get());

            documentService.save(documentModel);
            return new ResponseEntity<>(documentModel, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/documents")
    public ResponseEntity<Object> getAllDocuments(@RequestParam(required = false) Long orderServiceId) {
        List<DocumentModel> documents;

        if (orderServiceId != null) {
            documents = documentService.findByOrderServiceId(orderServiceId);
        } else documents = documentService.findAll();

        if (documents.isEmpty()){
            return new ResponseEntity<>("Documentos não encontrados",HttpStatus.NOT_FOUND);
        }

        List<Map<String, Object>> documentsData = new ArrayList<>();
        for (DocumentModel documentModel : documents) {
            String filePath = System.getProperty("user.dir") + documentModel.getNameFile();
            Map<String, Object> documentData = new HashMap<>();
            documentData.put("nameFile", documentModel.getNameFile());
            documentData.put("description", documentModel.getDescription());

            try {
                byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
                String base64Content = Base64.getEncoder().encodeToString(fileContent);
                documentData.put("content", base64Content);
            } catch (Exception e) {
                documentData.put("content", null);
                System.err.println("Erro ao ler arquivo" + filePath);
            }

            documentsData.add(documentData);
        }

        return new ResponseEntity<>(documents, HttpStatus.OK);
    }


    @GetMapping("/document/{id}")
    public ResponseEntity<Object> findById(@PathVariable(value = "id") Long id) {
        Optional<DocumentModel> documentOptional = documentService.findById(id);

        if (documentOptional.isEmpty()) {
            return new ResponseEntity<>("Documento não encontrado", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(documentOptional.get(), HttpStatus.OK);
    }


    @DeleteMapping("/document/{id}")
    public ResponseEntity<Object> deleteDocument(@PathVariable(value = "id") Long id) {
        Optional<DocumentModel> documentOptional = documentService.findById(id);

        if (documentOptional.isEmpty()) {
            return new ResponseEntity<>("Dcomento não encontrado", HttpStatus.NOT_FOUND);
        }
        documentService.delete(documentOptional.get());
        return new ResponseEntity<>("Documento removido com sucesso", HttpStatus.OK);
    }


    @PutMapping("/document/{id}")
    public ResponseEntity<Object> updateDocument(@PathVariable(value = "id") Long id, @RequestBody @Valid DocumentDTO documentDTO) {
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
