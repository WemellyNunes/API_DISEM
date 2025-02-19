package com.disem.API.controllers;

import com.disem.API.dtos.ImageDTO;
import com.disem.API.enums.OrdersServices.TypeEnum;
import com.disem.API.models.ImageModel;
import com.disem.API.models.ProgramingModel;
import com.disem.API.services.FileCompressionService;
import com.disem.API.services.ImageService;
import com.disem.API.services.ProgramingService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@RestController
@RequestMapping("api")
@CrossOrigin(origins = { "*"
}, allowedHeaders = "*")
public class ImageController {

    @Autowired
    ImageService imageService;

    @Autowired
    ProgramingService programingService;

    @Autowired
    FileCompressionService fileCompressionService;

    @Autowired
    MinioClient minioClient;


    @PostMapping("/uploadFile")
    public ResponseEntity<Object> createImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("programingId") Long programingId,
            @RequestParam("type") TypeEnum type,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "createdAt", required = false) String createdAt
            ) {
        final long MAX_FILE_SIZE = 5 * 1024 * 1024;

        if (file.getSize() > MAX_FILE_SIZE) {
            return new ResponseEntity<>("Arquivo excede o limite de 5MB", HttpStatus.BAD_REQUEST);
        }

        if (file.isEmpty()) {
            return new ResponseEntity<>("Nenhum arquivo enviado", HttpStatus.BAD_REQUEST);
        }

        Optional<ProgramingModel> programingModelOptional = programingService.findById(programingId);
        if (programingModelOptional.isEmpty()) {
            return new ResponseEntity<>("Programação não encontrada", HttpStatus.NOT_FOUND);
        }

        try {
            String fileUrl = fileCompressionService.compressAndUploadFile(file);

            /*
            String uploadDir = System.getProperty("user.dir") + "/uploads/images/";

            File compressedFile = fileCompressionService.compressFile(file, uploadDir);
            String compressedFilePath = "/uploads/files/" + compressedFile.getName();
             */

            ImageModel imageModel = new ImageModel();
            imageModel.setNameFile(fileUrl);
            imageModel.setDescription(description != null ? description : "Imagens da manutenção");
            imageModel.setType(type);
            imageModel.setPrograming(programingModelOptional.get());
            LocalDateTime dateTime = (createdAt != null)
                    ? LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_DATE_TIME)
                    : LocalDateTime.now();
            imageModel.setCreatedAt(dateTime);

            imageService.save(imageModel);

            return new ResponseEntity<>(imageModel, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/files")
    public ResponseEntity<Object> getAllImages(@RequestParam(required = false) Long programingId) {
        List<ImageModel> imageModels = (programingId != null) ?
                imageService.findByProgramingId(programingId) : imageService.findAll();

        if (imageModels.isEmpty()) {
            return new ResponseEntity<>("Imagens não encontradas", HttpStatus.NOT_FOUND);
        }

        List<Map<String, Object>> imageDataList = new ArrayList<>();
        String bucketName = fileCompressionService.getBucketName();

        for (ImageModel imageModel : imageModels) {
            Map<String, Object> imageData = new HashMap<>();
            imageData.put("id", imageModel.getId());
            imageData.put("nameFile", imageModel.getNameFile());
            imageData.put("description", imageModel.getDescription());
            imageData.put("type", imageModel.getType());

            String fileUrl = imageModel.getNameFile();
            imageData.put("fileUrl", fileUrl);

            try {
                String objectName = fileUrl.substring(fileUrl.indexOf(bucketName) + bucketName.length() + 1);
                InputStream inputStream = minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(bucketName)
                                .object(objectName)
                                .build()
                );

                byte[] fileContent = inputStreamToByteArray(inputStream);
                String base64Content = Base64.getEncoder().encodeToString(fileContent);
                imageData.put("content", base64Content);
            } catch (MinioException e) {
                imageData.put("content", null);
                System.err.println("Erro ao buscar imagem no MinIO: " + e.getMessage());
            } catch (Exception e) {
                imageData.put("content", null);
                System.err.println("Erro ao converter imagem para Base64: " + e.getMessage());
            }

            imageDataList.add(imageData);
        }

        return new ResponseEntity<>(imageDataList, HttpStatus.OK);
    }

    private byte[] inputStreamToByteArray(InputStream inputStream) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, bytesRead);
        }
        return buffer.toByteArray();
    }


    @GetMapping("/file/view/{fileName}")
    public ResponseEntity<byte[]> viewImage(@PathVariable String fileName) {
        try {
            Path imagePath = Paths.get(System.getProperty("user.dir") + "/uploads/images/").resolve(fileName);
            if (!Files.exists(imagePath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            byte[] imageBytes = Files.readAllBytes(imagePath);

            return ResponseEntity.ok()
                    .header("Content-Type", Files.probeContentType(imagePath))
                    .body(imageBytes);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/file/{id}")
    public ResponseEntity<Object> getOneImage(@PathVariable(value = "id") Long id) {
        Optional<ImageModel> imageModelOptional = imageService.findById(id);
        if (imageModelOptional.isEmpty()) {
            return new ResponseEntity<>("Imagem não encontrada", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(imageModelOptional.get(), HttpStatus.OK);
    }

    @DeleteMapping("/file/{id}")
    public ResponseEntity<Object> deleteImage(@PathVariable(value = "id") Long id) {
        Optional<ImageModel> imageModelOptional = imageService.findById(id);
        if (imageModelOptional.isEmpty()) {
            return new ResponseEntity<>("Imagem não encontrada", HttpStatus.NOT_FOUND);
        }
        imageService.delete(imageModelOptional.get());
        return new ResponseEntity<>("Imagem apagada com sucesso!", HttpStatus.OK);
    }

    @PutMapping("/file/{id}")
    public ResponseEntity<Object> updateImage(
            @PathVariable(value = "id") Long id,
            @RequestBody @Valid ImageDTO imageDTO
    ) {
        Optional<ImageModel> imageModelOptional = imageService.findById(id);
        if (imageModelOptional.isEmpty()) {
            return new ResponseEntity<>("Imagem não encontrada!", HttpStatus.NOT_FOUND);
        }

        Optional<ProgramingModel> programingModelOptional = programingService.findById(imageDTO.getPrograming_id());
        if (programingModelOptional.isEmpty()) {
            return new ResponseEntity<>("Programação não encontrada!", HttpStatus.NOT_FOUND);
        }

        var imageModel = imageModelOptional.get();
        var programingModel = programingModelOptional.get();

        imageModel.setNameFile(imageDTO.getNameFile());
        imageModel.setDescription(imageDTO.getDescription());
        imageModel.setCreatedAt(imageDTO.getCreatedAt());
        imageModel.setPrograming(programingModel);

        return new ResponseEntity<>(imageService.save(imageModel), HttpStatus.OK);
    }
}
