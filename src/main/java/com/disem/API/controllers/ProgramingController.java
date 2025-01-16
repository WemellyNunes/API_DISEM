package com.disem.API.controllers;

import com.disem.API.dtos.ProgramingDTO;
import com.disem.API.enums.OrdersServices.StatusEnum;
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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = { "*"
}, allowedHeaders = "*")
public class ProgramingController {

    @Autowired
    ProgramingService programingService;

    @Autowired
    OrderServiceService orderServiceService;

    @PostMapping("/programing")
    public ResponseEntity<Object> createPrograming(@RequestBody @Valid ProgramingDTO programingDTO){
        Optional<OrderServiceModel> orderServiceModelOptional = orderServiceService.findById(programingDTO.getOrderService_id());

        if (orderServiceModelOptional.isEmpty()){
            return new ResponseEntity<>("Ordem de serviço não encontrada", HttpStatus.NOT_FOUND);
        }
        else {
            var programingModel = new ProgramingModel();
            BeanUtils.copyProperties(programingDTO, programingModel);
            programingModel.setOrderService(orderServiceModelOptional.get());

            OrderServiceModel orderService = orderServiceModelOptional.get();
            orderService.setStatus(StatusEnum.fromDescricao("Em atendimento"));
            orderServiceService.save(orderService);

            ProgramingModel savedPrograming = programingService.save(programingModel);

            return ResponseEntity.status(HttpStatus.CREATED).body(programingService.save(programingModel));
        }
    }


    @GetMapping("/programing")
    public ResponseEntity<Object> getAllPrograming(){
        List<ProgramingModel> programingModelList = programingService.findAll();
        if (programingModelList.isEmpty()){
            return new ResponseEntity<>("Programação não encontrada", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(programingModelList, HttpStatus.OK);
    }


    @GetMapping("/programing/{id}")
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


            return new ResponseEntity<>(programingService.save(programingModel), HttpStatus.OK);
        }
    }

}
