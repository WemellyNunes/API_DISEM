package com.disem.API.controllers;

import com.disem.API.dtos.UnitDTO;
import com.disem.API.models.UnitModel;
import com.disem.API.services.UnitService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = { "*"
}, allowedHeaders = "*")
public class UnitController {

    @Autowired
    UnitService unitService;

    @PostMapping("/unit")
    public ResponseEntity<Object> saveUnit(@RequestBody @Valid UnitDTO unitDTO) {
        var unitModel = new UnitModel();
        BeanUtils.copyProperties(unitDTO, unitModel);

        var savedUnit = unitService.save(unitModel);
        return new ResponseEntity<>(savedUnit, HttpStatus.CREATED);
    }

    @PostMapping("/unit/upload")
    public ResponseEntity<Object> uploadUnit(@RequestBody List<UnitDTO> unitDTOList) {
        try {
            List<UnitModel> unitModels = unitDTOList.stream().map(dto -> {
                var unitModel = new UnitModel();
                BeanUtils.copyProperties(dto, unitModel);
                return unitModel;
            }).toList();

            var savedUnit = unitService.saveAll(unitModels);
            return new ResponseEntity<>(savedUnit, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/units")
    public ResponseEntity<Object> getAllUnits() {
        List<UnitModel> unitModels = unitService.findAll();
        if (unitModels.isEmpty()) {
            return new ResponseEntity<>("Unidade não encontrada",HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(unitModels, HttpStatus.OK);
    }

    @GetMapping("unit/{id}")
    public ResponseEntity<Object> getUnitById(@PathVariable(value = "id") Long id) {
        Optional<UnitModel> unitModel = unitService.findById(id);
        if (unitModel.isEmpty()) {
            return new ResponseEntity<>( "Unidade não encontrado", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(unitModel.get(), HttpStatus.OK);
    }

    @DeleteMapping("unit/{id}")
    public ResponseEntity<Object> deleteUnit(@PathVariable(value = "id") Long id) {
        Optional<UnitModel> unitModel = unitService.findById(id);
        if (unitModel.isEmpty()) {
            return new ResponseEntity<>("Unidade não encontrada", HttpStatus.NOT_FOUND);
        }
        unitService.delete(unitModel.get());
        return new ResponseEntity<>("Unidade deletado com sucesso", HttpStatus.OK);
    }

    @PutMapping("unit/{id}")
    public ResponseEntity<Object> updateUnit(@PathVariable(value = "id") Long id, @RequestBody UnitDTO unitDTO) {
        Optional<UnitModel> unitModel = unitService.findById(id);
        if (unitModel.isEmpty()) {
            return new ResponseEntity<>("unidade não encontrada", HttpStatus.NOT_FOUND);
        }

        var updateUnitModel = unitModel.get();
        updateUnitModel.setUnit(unitDTO.getUnit());
        updateUnitModel.setCampus(unitDTO.getCampus());

        return new ResponseEntity<>(unitService.save(updateUnitModel), HttpStatus.OK);
    }

}
