package com.disem.API.services;

import com.disem.API.models.ImageModel;
import com.disem.API.repositories.ImageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ImageService {

    @Autowired
    ImageRepository imageRepository;

    @Transactional
    public ImageModel save(ImageModel imageModel) {
        return imageRepository.save(imageModel);
    }

    public List<ImageModel> findAll() {
        return imageRepository.findAll();
    }

    public List<ImageModel> findByProgramingId(Long programingId) {
        return imageRepository.findByProgramingId(programingId);
    }

    @Transactional
    public Optional<ImageModel> findById(Long id) {
        return imageRepository.findById(id);
    }

    public void delete(ImageModel imageModel){
        imageRepository.delete(imageModel);
    }
}
