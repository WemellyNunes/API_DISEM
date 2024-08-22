package com.disem.API.controllers;

import com.disem.API.dtos.ImageDTO;
import com.disem.API.models.ImageModel;
import com.disem.API.models.ProgramingModel;
import com.disem.API.services.ImageService;
import com.disem.API.services.ProgramingService;
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


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ImageController {

    @Autowired
    ImageService imageService;

    @Autowired
    ProgramingService programingService;

    @PostMapping("/images")
    public ResponseEntity<Object> createImage(@RequestBody @Valid ImageDTO imageDTO) {
        Optional<ProgramingModel> programingModelOptional = programingService.findById(imageDTO.getPrograming_id());

        if (programingModelOptional.isEmpty()) {
            return new ResponseEntity<>("Ordem de serviço não enocntrada", HttpStatus.NOT_FOUND);
        }
        else {
            var imageModel = new ImageModel();
            BeanUtils.copyProperties(imageDTO, imageModel);
            imageModel.setPrograming(programingModelOptional.get());
            return new ResponseEntity<>(imageService.save(imageModel), HttpStatus.CREATED);
        }
    }


    @GetMapping("/images")
    public ResponseEntity<Object> getAllImages(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ImageModel> imageModelPage = imageService.findAll(pageable);

        if (imageModelPage.isEmpty()){
            return new ResponseEntity<>("Imagens não encontradas", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(imageModelPage, HttpStatus.OK);
    }


    @GetMapping("/image/{id}")
    public ResponseEntity<Object> getOneImage(@PathVariable(value = "id") Long id) {
        Optional<ImageModel> imageModelOptional = imageService.findById(id);

        if (imageModelOptional.isEmpty()) {
            return new ResponseEntity<>("Imagem não encontrada", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(imageModelOptional.get(), HttpStatus.OK);
    }


    @DeleteMapping("/image/{id}")
    public ResponseEntity<Object> deleteImage(@PathVariable(value = "id") Long id) {
        Optional<ImageModel> imageModelOptional = imageService.findById(id);

        if (imageModelOptional.isEmpty()) {
            return new ResponseEntity<>("Imagem não encontrada", HttpStatus.NOT_FOUND);
        }
        imageService.delete(imageModelOptional.get());
        return new ResponseEntity<>("Imagem apagada com sucesso!", HttpStatus.OK);
    }


    @PutMapping("/image/{id}")
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
