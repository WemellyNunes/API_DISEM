package com.disem.API.controllers;

import com.disem.API.dtos.PreventiveEquipamentDTO;
import com.disem.API.models.OrderServiceModel;
import com.disem.API.models.PreventiveEquipamentModel;
import com.disem.API.repositories.OrderServiceRepository;
import com.disem.API.services.PreventiveEquipamentService;
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
public class PreventiveEquipamentController {

    @Autowired
    PreventiveEquipamentService preventiveEquipamentService;

    @Autowired
    OrderServiceRepository orderServiceRepository;

    @PostMapping("/preventiveEquipament")
    public ResponseEntity<Object> savePreventiveSystem(@RequestBody @Valid PreventiveEquipamentDTO preventiveEquipamentDTO) {
        var preventiveEquipament = new PreventiveEquipamentModel();

        BeanUtils.copyProperties(preventiveEquipamentDTO, preventiveEquipament);

        if (preventiveEquipamentDTO.getOrderService_id() != null){
            OrderServiceModel orderServiceModel = orderServiceRepository.findById(preventiveEquipamentDTO.getOrderService_id())
                    .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ordem de serviço não encontrada"));
            preventiveEquipament.setOrderService(orderServiceModel);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(preventiveEquipamentService.save(preventiveEquipament));
    }


    @GetMapping("/preventiveEquipament")
    public ResponseEntity<Object> getAllPreventiveEquipament(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<PreventiveEquipamentModel> preventiveEquipamentModelPage = preventiveEquipamentService.findAll(pageable);

        if (preventiveEquipamentModelPage.isEmpty()) {
            return new ResponseEntity<>("Preventiva de equipamento não encontrado", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(preventiveEquipamentModelPage, HttpStatus.OK);
    }


    @GetMapping("/preventiveEquipament/{id}")
    public ResponseEntity<Object> getPreventiveEquipamentById(@PathVariable(value = "id") Long id) {
        Optional<PreventiveEquipamentModel> preventiveEquipamentModel = preventiveEquipamentService.findById(id);

        if (preventiveEquipamentModel.isEmpty()) {
            return new ResponseEntity<>("Preventiva de equipamento não encontrado", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(preventiveEquipamentModel.get(), HttpStatus.OK);
    }


    @DeleteMapping("/preventiveEquipament/{id}")
    public ResponseEntity<Object> deletePreventiveEquipament(@PathVariable(value = "id") Long id) {
        Optional<PreventiveEquipamentModel> preventiveEquipamentModel = preventiveEquipamentService.findById(id);

        if (preventiveEquipamentModel.isEmpty()) {
            return new ResponseEntity<>("Preventiva de equipamento não encontrado", HttpStatus.NOT_FOUND);
        }
        preventiveEquipamentService.delete(preventiveEquipamentModel.get());
        return new ResponseEntity<>("Preventiva de equipamento removido", HttpStatus.OK);
    }


    @PutMapping("preventiveEquipament/{id}")
    public ResponseEntity<Object> updatePreventiveEquipament(@PathVariable(value = "id") Long id, @RequestBody @Valid PreventiveEquipamentDTO preventiveEquipamentDTO){
        Optional<PreventiveEquipamentModel> preventiveEquipamentModel = preventiveEquipamentService.findById(id);

        if (preventiveEquipamentModel.isEmpty()){
            return new ResponseEntity<>("Preventiva de equipamento não encontrado", HttpStatus.NOT_FOUND);
        }
        else {
            var preventiveEquipament = preventiveEquipamentModel.get();

            preventiveEquipament.setOverturning(preventiveEquipamentDTO.getOverturning());
            preventiveEquipament.setPeriod(preventiveEquipamentDTO.getPeriod());
            preventiveEquipament.setUnit(preventiveEquipamentDTO.getUnit());
            preventiveEquipament.setBloc(preventiveEquipamentDTO.getBloc());
            preventiveEquipament.setAmbient(preventiveEquipamentDTO.getAmbient());
            preventiveEquipament.setBrand(preventiveEquipamentDTO.getBrand());
            preventiveEquipament.setType(preventiveEquipamentDTO.getType());
            preventiveEquipament.setDescription(preventiveEquipamentDTO.getDescription());
            preventiveEquipament.setModel(preventiveEquipamentDTO.getModel());
            preventiveEquipament.setCondition(preventiveEquipamentDTO.getCondition());
            preventiveEquipament.setPrevision(preventiveEquipamentDTO.getPrevision());
            preventiveEquipament.setCreationDate(preventiveEquipamentDTO.getCreationDate());
            preventiveEquipament.setModificationDate(preventiveEquipamentDTO.getModificationDate());

            return ResponseEntity.status(HttpStatus.OK).body(preventiveEquipamentService.save(preventiveEquipament));
        }
    }

}
