package com.disem.API.controllers;

import com.disem.API.dtos.ChecklistCorrectiveDTO;
import com.disem.API.models.ChecklistCorrectiveModel;
import com.disem.API.models.ProgramingModel;
import com.disem.API.services.ChecklistCorrectiveService;
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
@RequestMapping("api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ChecklistCorrectiveController {

    @Autowired
    ChecklistCorrectiveService checklistCorrectiveService;

    @Autowired
    ProgramingService programingService;

    @PostMapping("/checklistCorrective")
    public ResponseEntity<Object> createChecklistCorrective(@RequestBody @Valid ChecklistCorrectiveDTO checklistCorrectiveDTO){
        Optional<ProgramingModel> programingModelOptional = programingService.findById(checklistCorrectiveDTO.getPrograming_id());

        if (programingModelOptional.isEmpty()){
            return new ResponseEntity<>("Programação não encontrada", HttpStatus.NOT_FOUND);
        }
        else {
            var checklistCorrective = new ChecklistCorrectiveModel();
            BeanUtils.copyProperties(checklistCorrectiveDTO, checklistCorrective);
            checklistCorrective.setPrograming(programingModelOptional.get());
            return new ResponseEntity<>(checklistCorrectiveService.save(checklistCorrective), HttpStatus.CREATED);
        }
    }

    @GetMapping("/checklistCorrective")
    public ResponseEntity<Object> getAllChecklistCorrective(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC)Pageable pageable) {
        Page<ChecklistCorrectiveModel> checklistCorrectiveModelPage = checklistCorrectiveService.findAll(pageable);

        if (checklistCorrectiveModelPage.isEmpty()){
            return new ResponseEntity<>("Checklists não encontrados", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(checklistCorrectiveModelPage, HttpStatus.OK);
    }

    @GetMapping("/checklistCorrective/{id}")
    public ResponseEntity<Object> getOneChecklistCorretive(@PathVariable(value = "id")Long id) {
        Optional<ChecklistCorrectiveModel> checklistCorrectiveModelOptional = checklistCorrectiveService.findById(id);

        if (checklistCorrectiveModelOptional.isEmpty()) {
            return new ResponseEntity<>("Checklist não encontrado", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(checklistCorrectiveModelOptional.get(), HttpStatus.OK);
    }

    @DeleteMapping("/checklistCorrective/{id}")
    public ResponseEntity<Object> deleteChecklistCorrective(@PathVariable(value = "id") Long id) {
        Optional<ChecklistCorrectiveModel> checklistCorrectiveModelOptional = checklistCorrectiveService.findById(id);

        if (checklistCorrectiveModelOptional.isEmpty()){
            return new ResponseEntity<>("Checklist não encontrado", HttpStatus.NOT_FOUND);
        }
        checklistCorrectiveService.delete(checklistCorrectiveModelOptional.get());
        return new ResponseEntity<>("Checklist apagado com sucesso", HttpStatus.OK);
    }

    @PutMapping("checklistCorrective/{id}")
    public ResponseEntity<Object> updateChecklistCorrective(@PathVariable(value = "id") Long id, @RequestBody @Valid ChecklistCorrectiveDTO checklistCorrectiveDTO){
        Optional<ChecklistCorrectiveModel> checklistCorrectiveModelOptional = checklistCorrectiveService.findById(id);

        if (checklistCorrectiveModelOptional.isEmpty()){
            return new ResponseEntity<>("Checklist não encontrado", HttpStatus.NOT_FOUND);
        }
        else {
            var checklistCorrective = checklistCorrectiveModelOptional.get();

            checklistCorrective.setTreatment(checklistCorrectiveDTO.getTreatment());
            checklistCorrective.setCreationDate(checklistCorrectiveDTO.getCreationDate());
            checklistCorrective.setModificationDate(checklistCorrectiveDTO.getModificationDate());

            return new ResponseEntity<>(checklistCorrectiveService.save(checklistCorrective), HttpStatus.OK);
        }
    }
}
