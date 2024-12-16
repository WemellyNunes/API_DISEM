package com.disem.API.controllers;

import com.disem.API.dtos.NegationDTO;
import com.disem.API.enums.OrdersServices.StatusEnum;
import com.disem.API.models.NegationModel;
import com.disem.API.models.OrderServiceModel;
import com.disem.API.services.NegationService;
import com.disem.API.services.OrderServiceService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class NegationController {

    @Autowired
    NegationService negationService;

    @Autowired
    OrderServiceService orderService;


    @PostMapping("/negation")
    public ResponseEntity<Object> createNegation(@RequestBody @Valid NegationDTO negationDTO){
        System.out.println("Recebendo DTO: " + negationDTO);
        Optional<OrderServiceModel> orderServiceModelOptional = orderService.findById(negationDTO.getOrderService_id());

        if (orderServiceModelOptional.isEmpty()){
            return new ResponseEntity<>("Ordem de serviço não encontrada", HttpStatus.NOT_FOUND);
        }
        else {
            var negationModel = new NegationModel();
            BeanUtils.copyProperties(negationDTO, negationModel);
            negationModel.setOrderServiceId(orderServiceModelOptional.get());
            System.out.println("Salvando NegationModel: " + negationModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(negationService.save(negationModel));
        }
    }

    @GetMapping("/negation")
    public ResponseEntity<Object> getNegation(){
        List<NegationModel> negationModelList = negationService.findAll();
        if (negationModelList.isEmpty()){
            return new ResponseEntity<>("Negação não encontrada", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(negationModelList, HttpStatus.OK);
    }

    @GetMapping("/negation/{id}")
    public ResponseEntity<Object> getNegationById(@PathVariable(value = "id") Long id){
        Optional<NegationModel> negationModelOptional = negationService.findById(id);
        if (negationModelOptional.isEmpty()){
            return new ResponseEntity<>("Negação não encontrada", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(negationModelOptional.get(), HttpStatus.OK);
    }

    @GetMapping("negation/byOrderService/{orderServiceId}")
    public ResponseEntity<Object> getNegationByOrderService(@PathVariable(value = "orderServiceId") Long orderServiceId) {
        Optional<NegationModel> negationModelOptional = negationService.findByOrderServiceId(orderServiceId);

        if (negationModelOptional.isEmpty()) {
            return new ResponseEntity<>("Negação não encontrada", HttpStatus.NOT_FOUND);
        }

        NegationModel negationModel = negationModelOptional.get();

        Map<String, Object> response = new HashMap<>();
        response.put("id", negationModel.getId());
        response.put("content", negationModel.getContent());
        response.put("date", negationModel.getDate());
        response.put("orderServiceId", negationModel.getOrderServiceId().getId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("negation/{id}")
    public ResponseEntity<Object> deleteNegation(@PathVariable(value = "id") Long id){
        Optional<NegationModel> negationModelOptional = negationService.findById(id);
        if (negationModelOptional.isEmpty()){
            return new ResponseEntity<>("Negação não encontrada", HttpStatus.NOT_FOUND);
        }
        negationService.delete(negationModelOptional.get());
        return new ResponseEntity<>("Negação apagada com sucesso", HttpStatus.OK);
    }

    @PutMapping("negation/{id}")
    public ResponseEntity<Object> updateNegation(@PathVariable(value = "id") Long id, @RequestBody @Valid NegationDTO negationDTO){
        Optional<NegationModel> negationModelOptional = negationService.findById(id);
        if (negationModelOptional.isEmpty()){
            return new ResponseEntity<>("Negação não encontrada", HttpStatus.NOT_FOUND);
        }

        var negationModel = new NegationModel();

        negationModel.setContent(negationDTO.getContent());
        negationModel.setDate(negationDTO.getDate());

        return new ResponseEntity<>(negationService.save(negationModel), HttpStatus.OK);
    }


}
