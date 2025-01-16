package com.disem.API.controllers;

import com.disem.API.dtos.InstituteDTO;
import com.disem.API.models.InstituteModel;
import com.disem.API.services.InstituteService;
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
public class InstituteController {

    @Autowired
    InstituteService instituteService;

    @PostMapping("/institute")
    public ResponseEntity<Object> saveInstitute(@RequestBody @Valid InstituteDTO instituteDTO) {
        var instituteModel = new InstituteModel();
        BeanUtils.copyProperties(instituteDTO, instituteModel);

        var savedInstitute = instituteService.save(instituteModel);
        return new ResponseEntity<>(savedInstitute, HttpStatus.CREATED);
    }

    @PostMapping("/institute/upload")
    public ResponseEntity<Object> uploadInstitute(@RequestBody List<InstituteDTO> instituteDTOList) {
        try {
            List<InstituteModel> instituteModels = instituteDTOList.stream().map(dto -> {
                var instituteModel = new InstituteModel();
                BeanUtils.copyProperties(dto, instituteModel);
                return instituteModel;
            }).toList();

            var savedInstitute = instituteService.saveAll(instituteModels);
            return new ResponseEntity<>(savedInstitute, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao salvar o instituto" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("institutes")
    public ResponseEntity<Object> getInstitutes() {
        List<InstituteModel> instituteModels = instituteService.findAll();
        if (instituteModels.isEmpty()) {
            return new ResponseEntity<>("Institutos não encontrados" ,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(instituteModels, HttpStatus.OK);
    }

    @GetMapping("institute/{id}")
    public ResponseEntity<Object> getInstituteById(@PathVariable(value = "id") Long id) {
        Optional<InstituteModel> instituteModel = instituteService.findById(id);
        if (instituteModel.isEmpty()) {
            return new ResponseEntity<>("Instituto não encontrado", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(instituteModel.get(), HttpStatus.OK);
    }

    @DeleteMapping("institute/{id}")
    public ResponseEntity<Object> deleteInstitute(@PathVariable(value = "id") Long id) {
        Optional<InstituteModel> instituteModel = instituteService.findById(id);
        if (instituteModel.isEmpty()) {
            return new ResponseEntity<>("Instituto não encontrado", HttpStatus.NOT_FOUND);
        }
        instituteService.delete(instituteModel.get());
        return new ResponseEntity<>("Instituto deletado com sucesso", HttpStatus.OK);
    }

    @PutMapping("institute/{id}")
    public ResponseEntity<Object> updateInstitute(@PathVariable(value = "id") Long id, @RequestBody InstituteDTO instituteDTO) {
        Optional<InstituteModel> instituteModel = instituteService.findById(id);

        if (instituteModel.isEmpty()) {
            return new ResponseEntity<>("Instituto não encontrado", HttpStatus.NOT_FOUND);
        }

        var updatedInstituteModel = instituteModel.get();
        updatedInstituteModel.setName(instituteDTO.getName());
        updatedInstituteModel.setAcronym(instituteDTO.getAcronym());
        updatedInstituteModel.setUnit(instituteDTO.getUnit());
        updatedInstituteModel.setCampus(instituteDTO.getCampus());

        return new ResponseEntity<>(instituteService.save(updatedInstituteModel), HttpStatus.OK);
    }
}
