package com.disem.API.controllers;

import com.disem.API.dtos.ImageDTO;
import com.disem.API.models.ImageModel;
import com.disem.API.models.ProgramingModel;
import com.disem.API.services.ImageService;
import com.disem.API.services.ProgramingService;
import jakarta.validation.Valid;
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
import java.util.Optional;


@RestController
@RequestMapping("api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ImageController {

    @Autowired
    ImageService imageService;

    @Autowired
    ProgramingService programingService;

    @PostMapping("/uploadFile")
    public ResponseEntity<Object> createImage(
            @RequestParam("file")MultipartFile file,
            @RequestParam("programingId") Long programingId,
            @RequestParam(value = "description", required = false) String description)
            {

       if (file.isEmpty()){
           return new ResponseEntity<>("nenhum arquivo enviado", HttpStatus.BAD_REQUEST);
       }

        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            return new ResponseEntity<>("Apenas arquivos JPG ou PNG são permitidos", HttpStatus.BAD_REQUEST);
        }

        Optional<ProgramingModel> programingModelOptional = programingService.findById(programingId);

        if (programingModelOptional.isEmpty()) {
            return new ResponseEntity<>("Programação não encontrada", HttpStatus.NOT_FOUND);
        }

        if (description == null || description.isEmpty()) {
            description = "Descrição padrão da imagem";
        }

        try {
            String uploadDir = System.getProperty("user.dir") + "/uploads/images/";

            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }
            String fileName = file.getOriginalFilename();
            Path path = Paths.get(uploadDir + fileName);
            Files.write(path, file.getBytes());

            String imagePath =  "/uploads/images/" + fileName;
            ImageModel imageModel = new ImageModel();
            imageModel.setNameFile(imagePath);
            imageModel.setDescription(description);
            imageModel.setPrograming(programingModelOptional.get());

            imageService.save(imageModel);

            return new ResponseEntity<>(imageModel, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/files")
    public ResponseEntity<Object> getAllImages(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ImageModel> imageModelPage = imageService.findAll(pageable);

        if (imageModelPage.isEmpty()){
            return new ResponseEntity<>("Imagens não encontradas", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(imageModelPage, HttpStatus.OK);
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
    public ResponseEntity<Object> updateImage(@PathVariable(value = "id") Long id, @RequestBody @Valid ImageDTO imageDTO) {
        Optional<ImageModel> imageModelOptional = imageService.findById(id);
        if (imageModelOptional.isEmpty()) {
            return new ResponseEntity<>("Imagem não escontrada!", HttpStatus.NOT_FOUND);
        }
        else {
            var imageModel = imageModelOptional.get();

            imageModel.setNameFile(imageDTO.getNameFile());
            imageModel.setDescription(imageDTO.getDescription());

            return new ResponseEntity<>(imageService.save(imageModel), HttpStatus.OK);
        }
    }
}
