package com.disem.API.controllers;

import com.disem.API.dtos.ProgramingDTO;
import com.disem.API.models.OrderServiceModel;
import com.disem.API.models.ProgramingModel;
import com.disem.API.services.OrderServiceService;
import com.disem.API.services.ProgramingService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class ProgramingController {

    @Autowired
    ProgramingService programingService;

    @Autowired
    OrderServiceService orderServiceService;

    @PostMapping("/programings")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Object> createPrograming(@RequestBody @Valid ProgramingDTO programingDTO){
        Optional<OrderServiceModel> orderServiceModelOptional = orderServiceService.findById(programingDTO.getOrderService_id());
        if (orderServiceModelOptional.isEmpty()){
            return new ResponseEntity<>("Ordem de serviço não encontrada", HttpStatus.NOT_FOUND);
        } else {
            var programingModel = new ProgramingModel();
            BeanUtils.copyProperties(programingDTO, programingModel);
            programingModel.setOrderService(orderServiceModelOptional.get());
            return ResponseEntity.status(HttpStatus.CREATED).body(programingService.save(programingModel));
        }
    }

    //@GetMapping("/programings")
   // @CrossOrigin(origins = "*", allowedHeaders = "*")

}
