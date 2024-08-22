package com.disem.API.services;

import com.disem.API.models.DocumentModel;
import com.disem.API.repositories.DocumentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class DocumentService {

    @Autowired
    DocumentRepository documentRepository;

    @Transactional
    public DocumentModel save(DocumentModel documentModel) {
        return documentRepository.save(documentModel);
    }

    public Optional<DocumentModel> findById(Long id) {
        return documentRepository.findById(id);
    }

    public Page<DocumentModel> findAll(Pageable pageable) {
        return documentRepository.findAll(pageable);
    }

    @Transactional
    public void delete(DocumentModel documentModel) {
        documentRepository.delete(documentModel);
    }
}
