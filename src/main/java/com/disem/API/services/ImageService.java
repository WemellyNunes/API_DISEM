package com.disem.API.services;

import com.disem.API.models.ImageModel;
import com.disem.API.repositories.ImageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;


import java.util.Optional;


@Service
public class ImageService {

    @Autowired
    ImageRepository imageRepository;

    @Transactional
    public ImageModel save(ImageModel imageModel) {
        return imageRepository.save(imageModel);
    }

    public Page<ImageModel> findAll(Pageable pageable) {
        return imageRepository.findAll(pageable);
    }

    @Transactional
    public Optional<ImageModel> findById(Long id) {
        return imageRepository.findById(id);
    }

    public void delete(ImageModel imageModel){
        imageRepository.delete(imageModel);
    }
}
