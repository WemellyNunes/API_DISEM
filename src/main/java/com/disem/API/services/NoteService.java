package com.disem.API.services;

import com.disem.API.models.NoteModel;
import com.disem.API.repositories.NoteRepositoy;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    @Autowired
    NoteRepositoy repo;

    @Transactional
    public NoteModel save(NoteModel note) {
        return repo.save(note);
    }

    public List<NoteModel> findAll() {
        return repo.findAll();
    }

    public List<NoteModel> findByProgramingId(Long programingId) {
        return repo.findByProgramingId(programingId);
    }

    public Optional<NoteModel> findById(Long id) {
        return repo.findById(id);
    }

    public void delete(NoteModel note) {
        repo.delete(note);
    }
}
