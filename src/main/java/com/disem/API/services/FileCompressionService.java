package com.disem.API.services;

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

    //tirar o String uploadDir quando for testar pro minio
    public File compressFile(MultipartFile file, String uploadDir) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IOException("Nome do arquivo inválido.");
        }

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        /*
        tirar do comentário, aq é o arquivo temporario antes de ir pro minio
        File compressedFile = File.createTempFile("compressed_", originalFilename.endsWith(".pdf") ? ".pdf" : ".jpg");
         */

        File compressedFile = new File(uploadDir, "compressed_" + originalFilename);

        if (originalFilename.toLowerCase().endsWith(".jpg") || originalFilename.toLowerCase().endsWith(".jpeg") ||
                originalFilename.toLowerCase().endsWith(".png")) {
            compressImage(file, compressedFile);
        } else if (originalFilename.toLowerCase().endsWith(".pdf")) {
            compressPdf(file, compressedFile);
        } else {
            throw new IOException("Formato não suportado para compressão.");
        }

        /*
        String minioUrl = storageService.uploadFile(compressedFile, file.getContentType());

        compressedFile.delete();

        return minioUrl;
         */

        return compressedFile;
    }

    private void compressImage(MultipartFile file, File outputFile) throws IOException {
        BufferedImage originalImage = ImageIO.read(file.getInputStream());

        Thumbnails.of(originalImage)
                .scale(1.0)  // <- tamanho original, mas da pra definir o tamanho tipo: '.size(800,600)
                .outputQuality(0.2) // Reduz pra 20%
                .toFile(outputFile);
    }

    private void compressPdf(MultipartFile file, File outputFile) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            document.setAllSecurityToBeRemoved(true);
            document.save(outputFile);
        }
    }
}
