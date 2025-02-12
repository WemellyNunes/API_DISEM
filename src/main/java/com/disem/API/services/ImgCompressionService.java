package com.disem.API.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ImgCompressionService {

    public String compressAndSaveImage(MultipartFile file, String uploadDir) throws IOException {
        if (!file.getOriginalFilename().endsWith(".jpg") && !file.getOriginalFilename().endsWith(".jpeg")) {
            throw new IOException("Não é possível comprimir esse tipo de imagem. Somente JPEG.");
        }

        BufferedImage originalImage = ImageIO.read(file.getInputStream());

        Files.createDirectories(Paths.get(uploadDir));

        String compressedFileName = "compressed_" + file.getOriginalFilename();
        File compressedFile = new File(uploadDir + compressedFileName);

        try (OutputStream os = new FileOutputStream(compressedFile);
             ImageOutputStream ios = ImageIO.createImageOutputStream(os)) {

            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.2f); // Reduz a qualidade para 20%


            writer.write(null, new IIOImage(originalImage, null, null), param);
            writer.dispose();
        }

        return "/uploads/images/" + compressedFileName;
    }
}
