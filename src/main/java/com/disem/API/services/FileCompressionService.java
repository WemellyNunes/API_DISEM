package com.disem.API.services;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

@Service
public class FileCompressionService {

    @Autowired
    StorageService storageService;

    @Autowired
    MinioClient minioClient;

    private static final String BUCKET_NAME = "sinfra";

    public String compressAndUploadFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IOException("Nome do arquivo inválido.");
        }

        File compressedFile = File.createTempFile("compressed_", originalFilename.endsWith(".pdf") ? ".pdf" : ".jpg");

        if (originalFilename.toLowerCase().endsWith(".jpg") || originalFilename.toLowerCase().endsWith(".jpeg") ||
                originalFilename.toLowerCase().endsWith(".png")) {
            compressImage(file, compressedFile);
        } else if (originalFilename.toLowerCase().endsWith(".pdf")) {
            compressPdf(file, compressedFile);
        } else {
            throw new IOException("Formato não suportado para compressão.");
        }

        String objectName = "uploads/" + compressedFile.getName();
        uploadToMinio(compressedFile, objectName);

        return "https://minio-dev.unifesspa.edu.br:9000/" + BUCKET_NAME + "/" + objectName;
    }

    private void compressImage(MultipartFile file, File outputFile) throws IOException {
        BufferedImage originalImage = ImageIO.read(file.getInputStream());

        Thumbnails.of(originalImage)
                .scale(1.0)  // Mantém o tamanho original
                .outputQuality(0.2) // Reduz para 20% da qualidade original
                .toFile(outputFile);
    }

    private void compressPdf(MultipartFile file, File outputFile) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            document.setAllSecurityToBeRemoved(true);
            document.save(outputFile);
        }
    }

    private void uploadToMinio(File file, String objectName) throws IOException {
        try (InputStream fileInputStream = new FileInputStream(file)) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(objectName)
                            .stream(fileInputStream, file.length(), -1)
                            .contentType("application/octet-stream")
                            .build()
            );
        } catch (Exception e) {
            throw new IOException("Erro ao enviar arquivo para o MinIO: " + e.getMessage(), e);
        }
    }
}
