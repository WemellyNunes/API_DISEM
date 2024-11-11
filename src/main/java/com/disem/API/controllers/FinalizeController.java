package com.disem.API.controllers;

import com.disem.API.dtos.FinalizeDTO;
import com.disem.API.models.FinalizeModel;
import com.disem.API.models.ProgramingModel;
import com.disem.API.services.DispatchOSService;
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
public class FinalizeController {

    @Autowired
    DispatchOSService dispatchOSService;

    @Autowired
    ProgramingService programingService;

    @PostMapping("/finished")
    public ResponseEntity<Object> createFinished(@RequestBody @Valid FinalizeDTO finalizeDTO){
        Optional<ProgramingModel> programingModel = programingService.findById(finalizeDTO.getPrograming_id());

        if (programingModel.isEmpty()){
            return new ResponseEntity<>("Ordem de serviço não encontrada", HttpStatus.NOT_FOUND);
        }
        else {
            var finished = new FinalizeModel();
            BeanUtils.copyProperties(finalizeDTO, finished);
            finished.setPrograming(programingModel.get());
            return ResponseEntity.status(HttpStatus.CREATED).body(dispatchOSService.save(finished));
        }
    }

    @GetMapping("/dispatches")
    public ResponseEntity<Object> getAllDispatches(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC)Pageable pageable){
        Page<FinalizeModel> dispatchOSModelPage = dispatchOSService.findAll(pageable);

        if (dispatchOSModelPage.isEmpty()){
            return new ResponseEntity<>("Despacho não encontrada", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dispatchOSModelPage, HttpStatus.OK);
    }

    @GetMapping("/dispatch/{id}")
    public ResponseEntity<Object> getOneDispatch(@PathVariable(value = "id") Long id){
        Optional<FinalizeModel> dispatchOSModelOptional = dispatchOSService.findById(id);

        if (dispatchOSModelOptional.isEmpty()){
            return new ResponseEntity<>("Despacho não encontrada", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dispatchOSModelOptional.get(), HttpStatus.OK);
    }

    @DeleteMapping("/dispatch/{id}")
    public ResponseEntity<Object> deleteDispatch(@PathVariable(value = "id") Long id){
        Optional<FinalizeModel> dispatchOSModelOptional = dispatchOSService.findById(id);

        if (dispatchOSModelOptional.isEmpty()){
            return new ResponseEntity<>("Despacho não encontrada", HttpStatus.NOT_FOUND);
        }
        else {
            dispatchOSService.delete(dispatchOSModelOptional.get());
            return new ResponseEntity<>("Dispacho apagado com sucesso", HttpStatus.OK);
        }
    }

    @PutMapping("/dispatch/{id}")
    public ResponseEntity<Object> updateDispatch(@PathVariable(value = "id") Long id, @RequestBody @Valid FinalizeDTO dispatchOSDTO){
        Optional<FinalizeModel> dispatchOSModelOptional = dispatchOSService.findById(id);

        if (dispatchOSModelOptional.isEmpty()){
            return new ResponseEntity<>("Despacho não encontrada", HttpStatus.NOT_FOUND);
        }
        else {
            var dispatch = dispatchOSModelOptional.get();

            dispatch.setContent(dispatchOSDTO.getContent());
            dispatch.setDateContent(dispatchOSDTO.getDateContent());

            return new ResponseEntity<>(dispatchOSService.save(dispatch), HttpStatus.OK);
        }
    }

}
