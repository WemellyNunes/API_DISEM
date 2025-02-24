package com.disem.API.controllers;

import com.disem.API.dtos.DocumentDTO;
import com.disem.API.models.DocumentModel;
import com.disem.API.models.OrderServiceModel;
import com.disem.API.services.DocumentService;
import com.disem.API.services.FileCompressionService;
import com.disem.API.services.OrderServiceService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

    @Autowired
    FileCompressionService fileCompressionService;

    @Autowired
    MinioClient minioClient;

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

            String fileUrl = fileCompressionService.compressAndUploadFile(file);

            //File compressedFile = fileCompressionService.compressAndUploadFile(file);
            //String documentPath = "/uploads/documents/" + compressedFile.getName();

            /*
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            String fileName = file.getOriginalFilename();
            Path path = Paths.get(uploadDir + fileName);
            Files.write(path, file.getBytes());

            String documentPath = "/uploads/documents/" + fileName;
             */

            DocumentModel documentModel = new DocumentModel();
            documentModel.setNamefile(fileUrl);
            documentModel.setDescription("Arquivo compactado");
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
        } else {
            documents = documentService.findAll();
        }

        if (documents.isEmpty()) {
            return new ResponseEntity<>("Arquivos não encontrados", HttpStatus.NOT_FOUND);
        }

        List<Map<String, Object>> documentsData = new ArrayList<>();
        for (DocumentModel documentModel : documents) {
            Map<String, Object> documentData = new HashMap<>();
            documentData.put("id", documentModel.getId());
            documentData.put("nameFile", documentModel.getNamefile());
            documentData.put("description", documentModel.getDescription());

            String fileUrl = documentModel.getNamefile();
            documentData.put("fileUrl", fileUrl);

            documentsData.add(documentData);
        }

        return new ResponseEntity<>(documentsData, HttpStatus.OK);
    }

    @GetMapping("/files/{fileName}")
    public ResponseEntity<Object> getFile(@PathVariable String fileName) {
        try {
            String bucketName = fileCompressionService.getBucketName();

            InputStream fileStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object("uploads/" + fileName)
                            .build()
            );

            byte[] fileBytes = inputStreamToByteArray(fileStream);
            String base64Content = Base64.getEncoder().encodeToString(fileBytes);

            String contentType = Files.probeContentType(Paths.get(fileName));
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            Map<String, Object> response = new HashMap<>();
            response.put("fileName", fileName);
            response.put("content", base64Content);
            response.put("contentType", contentType);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao carregar o arquivo: " + e.getMessage());
        }
    }

    private byte[] inputStreamToByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, bytesRead);
        }
        return buffer.toByteArray();
    }


    @GetMapping("/document/{id}")
    public ResponseEntity<Object> findById(@PathVariable(value = "id") Long id) {
        Optional<DocumentModel> documentOptional = documentService.findById(id);

        if (documentOptional.isEmpty()) {
            return new ResponseEntity<>("Arquivo não encontrado", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(documentOptional.get(), HttpStatus.OK);
    }


    @DeleteMapping("/document/{id}")
    public ResponseEntity<Object> deleteDocument(@PathVariable(value = "id") Long id) {
        Optional<DocumentModel> documentOptional = documentService.findById(id);

        if (documentOptional.isEmpty()) {
            return new ResponseEntity<>("Arquivo não encontrado", HttpStatus.NOT_FOUND);
        }
        documentService.delete(documentOptional.get());
        return new ResponseEntity<>("Arquivo removido com sucesso", HttpStatus.OK);
    }


    @PutMapping("/document/{id}")
    public ResponseEntity<Object> updateDocument(@PathVariable(value = "id") Long id, @RequestBody @Valid DocumentDTO documentDTO) {
        Optional<DocumentModel> documentOptional = documentService.findById(id);

        if (documentOptional.isEmpty()) {
            return new ResponseEntity<>("Arquivo não encontrado", HttpStatus.NOT_FOUND);
        }
        else {
            var document = documentOptional.get();

            document.setNamefile(documentDTO.getNamefile());
            document.setDescription(documentDTO.getDescription());

            return new ResponseEntity<>(documentService.save(document), HttpStatus.OK);
        }
    }

}
