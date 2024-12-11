package com.disem.API.repositories;

import com.disem.API.models.NoteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepositoy extends JpaRepository<NoteModel, Long> {

    List<NoteModel> findByProgramingId(Long programingId);
}
