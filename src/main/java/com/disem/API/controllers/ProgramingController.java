package com.disem.API.controllers;

import com.disem.API.dtos.ProgramingDTO;
import com.disem.API.models.OrderServiceModel;
import com.disem.API.models.ProgramingModel;
import com.disem.API.services.OrderServiceService;
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
public class ProgramingController {

    @Autowired
    ProgramingService programingService;

    @Autowired
    OrderServiceService orderServiceService;

    @PostMapping("/programings")
    public ResponseEntity<Object> createPrograming(@RequestBody @Valid ProgramingDTO programingDTO){
        Optional<OrderServiceModel> orderServiceModelOptional = orderServiceService.findById(programingDTO.getOrderService_id());

        if (orderServiceModelOptional.isEmpty()){
            return new ResponseEntity<>("Ordem de serviço não encontrada", HttpStatus.NOT_FOUND);
        }
        else {
            var programingModel = new ProgramingModel();
            BeanUtils.copyProperties(programingDTO, programingModel);
            programingModel.setOrderService(orderServiceModelOptional.get());
            return ResponseEntity.status(HttpStatus.CREATED).body(programingService.save(programingModel));
        }
    }


    @GetMapping("/programings")
    public ResponseEntity<Object> getAllPrograming(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        Page<ProgramingModel> programingModelPage = programingService.findAll(pageable);
        if (programingModelPage.isEmpty()){
            return new ResponseEntity<>("Programação não encontrada", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(programingModelPage, HttpStatus.OK);
    }


    @GetMapping("/programings/{id}")
    public ResponseEntity<Object> getOnePrograming(@PathVariable(value = "id") Long id) {
        Optional<ProgramingModel> programingModelOptional = programingService.findById(id);
        if (programingModelOptional.isEmpty()){
            return new ResponseEntity<>("Programação não encontrada", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(programingModelOptional.get(), HttpStatus.OK);
    }


    @DeleteMapping("programing/{id}")
    public ResponseEntity<Object> deleteOnePrograming(@PathVariable(value = "id") Long id){
        Optional<ProgramingModel> programingModelOptional = programingService.findById(id);
        if (programingModelOptional.isEmpty()){
            return new ResponseEntity<>("Programação não encontrada", HttpStatus.NOT_FOUND);
        }
        else {
            programingService.delete(programingModelOptional.get());
            return new ResponseEntity<>("Programação apagada com sucesso", HttpStatus.OK);
        }
    }


    @PutMapping("programing/{id}")
    public ResponseEntity<Object> updatePrograming(@PathVariable(value = "id") Long id, @RequestBody @Valid ProgramingDTO programingDTO){
        Optional<ProgramingModel> programingModelOptional = programingService.findById(id);
        if (programingModelOptional.isEmpty()){
            return new ResponseEntity<>("Programação não encontrada", HttpStatus.NOT_FOUND);

        }
        else {
            var programingModel = programingModelOptional.get();

            programingModel.setDatePrograming(programingDTO.getDatePrograming());
            programingModel.setTime(programingDTO.getTime());
            programingModel.setOverseer(programingDTO.getOverseer());
            programingModel.setWorker(programingDTO.getWorker());
            programingModel.setCost(programingDTO.getCost());
            programingModel.setObservation(programingDTO.getObservation());
            programingModel.setCreationDate(programingDTO.getCreationDate());
            programingModel.setModificationDate(programingDTO.getModificationDate());
            programingModel.setDelayedDays(programingDTO.getDelayedDays());

            return new ResponseEntity<>(programingService.save(programingModel), HttpStatus.OK);
        }
    }

}
