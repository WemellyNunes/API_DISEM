package com.disem.API.controllers;

import com.disem.API.dtos.PreventiveSystemDTO;
import com.disem.API.models.OrderServiceModel;
import com.disem.API.models.PreventiveSystemModel;
import com.disem.API.repositories.OrderServiceRepository;
import com.disem.API.services.PreventiveSystemService;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PreventiveSystemController {

    @Autowired
    PreventiveSystemService preventiveSystemService;

    @Autowired
    OrderServiceRepository orderServiceRepository;

    @PostMapping("/system")
    public ResponseEntity<Object> savePreventiveSystem(@RequestBody @Valid PreventiveSystemDTO preventiveSystemDTO) {
        var preventiveSystem = new PreventiveSystemModel();

        BeanUtils.copyProperties(preventiveSystemDTO, preventiveSystem);

        if (preventiveSystemDTO.getOrderService_id() != null){
            OrderServiceModel orderServiceModel = orderServiceRepository.findById(preventiveSystemDTO.getOrderService_id())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ordem de serviço não encontrada"));
            preventiveSystem.setOrderService(orderServiceModel);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(preventiveSystemService.save(preventiveSystem));

    }


    @GetMapping("/systems")
    public ResponseEntity<Object> getAllPreventiveSystem(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC)Pageable pageable) {
        Page<PreventiveSystemModel> preventiveSystemModelPage = preventiveSystemService.findAll(pageable);
        if (preventiveSystemModelPage.isEmpty()){
            return new ResponseEntity<>("Preventiva de sistema não encontrado", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(preventiveSystemModelPage, HttpStatus.OK);
    }


    @GetMapping("/system/{id}")
    public ResponseEntity<Object> getOnePreventiveSystem(@PathVariable(value = "id") Long id) {
        Optional<PreventiveSystemModel> preventiveSystem = preventiveSystemService.findById(id);
        if (preventiveSystem.isEmpty()){
            return new ResponseEntity<>("Preventiva de sistema não encontrado", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(preventiveSystem, HttpStatus.OK);
    }


    @DeleteMapping("/system/{id}")
    public ResponseEntity<Object> deletePreventiveSystem(@PathVariable(value = "id") Long id) {
        Optional<PreventiveSystemModel> preventiveSystem = preventiveSystemService.findById(id);
        if (preventiveSystem.isEmpty()){
            return new ResponseEntity<>("Preventiva de sistema não encontrado", HttpStatus.NOT_FOUND);
        }
        preventiveSystemService.delete(preventiveSystem.get());
        return new ResponseEntity<>("Preventiva de sistema apagada", HttpStatus.OK);
    }


    @PutMapping("/system/{id}")
    public ResponseEntity<Object> updatePreventiveSystem(@PathVariable(value = "id") Long id, @RequestBody PreventiveSystemDTO preventiveSystemDTO) {
        Optional<PreventiveSystemModel> preventiveSystem = preventiveSystemService.findById(id);

        if (preventiveSystem.isEmpty()){
            return new ResponseEntity<>("Preventiva não encontrada", HttpStatus.NOT_FOUND);
        }
        else {
            var preventiveSystemModel = new PreventiveSystemModel();

            preventiveSystemModel.setPeriod(preventiveSystemDTO.getPeriod());
            preventiveSystemModel.setSystem(preventiveSystemDTO.getSystem());
            preventiveSystemModel.setEdifice(preventiveSystemDTO.getEdifice());
            preventiveSystemModel.setObservation(preventiveSystemDTO.getObservation());
            preventiveSystemModel.setPrevision(preventiveSystemDTO.getPrevision());

            return ResponseEntity.status(HttpStatus.OK).body(preventiveSystemService.save(preventiveSystemModel));
        }
    }
}
