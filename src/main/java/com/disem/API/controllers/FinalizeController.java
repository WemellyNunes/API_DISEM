package com.disem.API.controllers;

import com.disem.API.dtos.FinalizeDTO;
import com.disem.API.models.FinalizeModel;
import com.disem.API.models.ProgramingModel;
import com.disem.API.services.FinalizeService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FinalizeController {

    @Autowired
    FinalizeService finalizeService;

    @Autowired
    ProgramingService programingService;

    @PostMapping("/finish")
    public ResponseEntity<Object> createFinished(@RequestBody @Valid FinalizeDTO finalizeDTO){
        Optional<ProgramingModel> programingModel = programingService.findById(finalizeDTO.getPrograming_id());

        if (programingModel.isEmpty()){
            return new ResponseEntity<>("Programação não encontrada", HttpStatus.NOT_FOUND);
        }
        else {
            var finished = new FinalizeModel();
            BeanUtils.copyProperties(finalizeDTO, finished);
            finished.setPrograming(programingModel.get());
            return ResponseEntity.status(HttpStatus.CREATED).body(finalizeService.save(finished));
        }
    }

    @GetMapping("/finished")
    public ResponseEntity<Object> getAllDispatches(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC)Pageable pageable){
        Page<FinalizeModel> dispatchOSModelPage = finalizeService.findAll(pageable);

        if (dispatchOSModelPage.isEmpty()){
            return new ResponseEntity<>("Despacho não encontrada", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dispatchOSModelPage, HttpStatus.OK);
    }

    @GetMapping("/finished/{id}")
    public ResponseEntity<Object> getOneDispatch(@PathVariable(value = "id") Long id){
        Optional<FinalizeModel> dispatchOSModelOptional = finalizeService.findById(id);

        if (dispatchOSModelOptional.isEmpty()){
            return new ResponseEntity<>("Despacho não encontrada", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dispatchOSModelOptional.get(), HttpStatus.OK);
    }

    @GetMapping("finished/byPrograming/{programingId}")
    public ResponseEntity<Object> getFinishedByPrograming(@PathVariable(value = "programingId") Long programingId){
        List<FinalizeModel> finalizeModel = finalizeService.findByProgramingId(programingId);

        if (finalizeModel.isEmpty()){
            return new ResponseEntity<>("Finalização não encontrada para o ID da programação fornecido.", HttpStatus.NOT_FOUND);
        }

        List<Map<String, Object>> response = finalizeModel.stream().map(finalizeModel1 -> {
            Map<String, Object> finalizeMap = new HashMap<>();
            finalizeMap.put("id", finalizeModel1.getId());
            finalizeMap.put("content", finalizeModel1.getContent());
            finalizeMap.put("dateContent", finalizeModel1.getDateContent());
            finalizeMap.put("programingId", finalizeModel1.getPrograming().getId());
            return finalizeMap;
        }).collect(Collectors.toList());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/finished/{id}")
    public ResponseEntity<Object> deleteDispatch(@PathVariable(value = "id") Long id){
        Optional<FinalizeModel> dispatchOSModelOptional = finalizeService.findById(id);

        if (dispatchOSModelOptional.isEmpty()){
            return new ResponseEntity<>("Despacho não encontrada", HttpStatus.NOT_FOUND);
        }
        else {
            finalizeService.delete(dispatchOSModelOptional.get());
            return new ResponseEntity<>("Dispacho apagado com sucesso", HttpStatus.OK);
        }
    }

    @PutMapping("/finished/{id}")
    public ResponseEntity<Object> updateDispatch(@PathVariable(value = "id") Long id, @RequestBody @Valid FinalizeDTO dispatchOSDTO){
        Optional<FinalizeModel> dispatchOSModelOptional = finalizeService.findById(id);

        if (dispatchOSModelOptional.isEmpty()){
            return new ResponseEntity<>("Despacho não encontrada", HttpStatus.NOT_FOUND);
        }
        else {
            var dispatch = dispatchOSModelOptional.get();

            dispatch.setContent(dispatchOSDTO.getContent());
            dispatch.setDateContent(dispatchOSDTO.getDateContent());

            return new ResponseEntity<>(finalizeService.save(dispatch), HttpStatus.OK);
        }
    }

}
