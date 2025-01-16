package com.disem.API.controllers;

import com.disem.API.dtos.NoteDTO;
import com.disem.API.models.NoteModel;
import com.disem.API.models.ProgramingModel;
import com.disem.API.services.NoteService;
import com.disem.API.services.ProgramingService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = { "*"
}, allowedHeaders = "*")
public class NoteController {

    @Autowired
    NoteService noteService;

    @Autowired
    ProgramingService programingService;

    @PostMapping("/note")
    public ResponseEntity<Object> addNote(@RequestBody @Valid NoteDTO note) {
        Optional<ProgramingModel> programingModel = programingService.findById(note.getPrograming_id());

        if (programingModel.isEmpty()){
            return new ResponseEntity<>("Programação não encontrada", HttpStatus.NOT_FOUND);
        }
        else {
            var notes = new NoteModel();
            BeanUtils.copyProperties(note, notes);
            notes.setPrograming(programingModel.get());
            return new ResponseEntity<>(noteService.save(notes), HttpStatus.CREATED);
        }
    }

    @GetMapping("/notes")
    public ResponseEntity<Object> getAllNotes() {
        List<NoteModel> notes = noteService.findAll();

        if (notes.isEmpty()) {
            return new ResponseEntity<>("Lista vazia", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    @GetMapping("/note/{id}")
    public ResponseEntity<Object> getNoteById(@PathVariable Long id) {
        Optional<NoteModel> note = noteService.findById(id);

        if (note.isEmpty()) {
            return new ResponseEntity<>("Nota não encontrada", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(note.get(), HttpStatus.OK);
    }

    @GetMapping("note/byPrograming/{programingId}")
    public ResponseEntity<Object> getNoteByProgramingId(@PathVariable(value = "programingId") Long programingId) {
        List<NoteModel> note = noteService.findByProgramingId(programingId);

        if (note.isEmpty()) {
            return new ResponseEntity<>("Nota não encontrada", HttpStatus.NOT_FOUND);
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        List<Map<String, Object>> response = note.stream().map(noteModel -> {
            Map<String, Object> noteMap = new HashMap<>();
            noteMap.put("id", noteModel.getId());
            noteMap.put("content", noteModel.getContent());
            noteMap.put("date", noteModel.getDate() != null ? noteModel.getDate().format(dateTimeFormatter) : "");
            noteMap.put("time", noteModel.getTime() != null ? noteModel.getTime().format(timeFormatter) : "");
            noteMap.put("programingId", noteModel.getPrograming().getId());
            return noteMap;
        }).collect(Collectors.toList());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/note/{id}")
    public ResponseEntity<Object> deleteNote(@PathVariable Long id) {
        Optional<NoteModel> note = noteService.findById(id);
        if (note.isEmpty()) {
            return new ResponseEntity<>("Nota não encontrada", HttpStatus.NOT_FOUND);
        }
        noteService.delete(note.get());
        return new ResponseEntity<>("Nota deletada com sucesso", HttpStatus.OK);
    }

    @PutMapping("/note/{id}")
    public ResponseEntity<Object> updateNote(@PathVariable Long id, @RequestBody @Valid NoteDTO note) {
        Optional<NoteModel> noteModel = noteService.findById(id);
        if (noteModel.isEmpty()) {
            return new ResponseEntity<>("Nota não encontrada", HttpStatus.NOT_FOUND);
        }

        var notes = new NoteModel();
        notes.setContent(note.getContent());
        notes.setDate(note.getDate());
        notes.setTime(note.getTime());

        return new ResponseEntity<>(noteService.save(notes), HttpStatus.OK);
    }

}
