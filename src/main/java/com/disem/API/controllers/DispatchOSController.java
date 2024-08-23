package com.disem.API.controllers;

import com.disem.API.dtos.DispatchOSDTO;
import com.disem.API.models.DispatchOSModel;
import com.disem.API.models.OrderServiceModel;
import com.disem.API.services.DispatchOSService;
import com.disem.API.services.OrderServiceService;
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
public class DispatchOSController {

    @Autowired
    DispatchOSService dispatchOSService;

    @Autowired
    OrderServiceService orderServiceService;

    @PostMapping("/dispatches")
    public ResponseEntity<Object> createDispatch(@RequestBody @Valid DispatchOSDTO dispatchOSDTO){
        Optional<OrderServiceModel> orderServiceModelOptional = orderServiceService.findById(dispatchOSDTO.getOrderService_id());

        if (orderServiceModelOptional.isEmpty()){
            return new ResponseEntity<>("Ordem de serviço não encontrada", HttpStatus.NOT_FOUND);
        }
        else {
            var dispatch = new DispatchOSModel();
            BeanUtils.copyProperties(dispatchOSDTO, dispatch);
            dispatch.setOrderService(orderServiceModelOptional.get());
            return ResponseEntity.status(HttpStatus.CREATED).body(dispatchOSService.save(dispatch));
        }
    }

    @GetMapping("/dispatches")
    public ResponseEntity<Object> getAllDispatches(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC)Pageable pageable){
        Page<DispatchOSModel> dispatchOSModelPage = dispatchOSService.findAll(pageable);

        if (dispatchOSModelPage.isEmpty()){
            return new ResponseEntity<>("Despacho não encontrada", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dispatchOSModelPage, HttpStatus.OK);
    }

    @GetMapping("/dispatch/{id}")
    public ResponseEntity<Object> getOneDispatch(@PathVariable(value = "id") Long id){
        Optional<DispatchOSModel> dispatchOSModelOptional = dispatchOSService.findById(id);

        if (dispatchOSModelOptional.isEmpty()){
            return new ResponseEntity<>("Despacho não encontrada", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dispatchOSModelOptional.get(), HttpStatus.OK);
    }

    @DeleteMapping("/dispatch/{id}")
    public ResponseEntity<Object> deleteDispatch(@PathVariable(value = "id") Long id){
        Optional<DispatchOSModel> dispatchOSModelOptional = dispatchOSService.findById(id);

        if (dispatchOSModelOptional.isEmpty()){
            return new ResponseEntity<>("Despacho não encontrada", HttpStatus.NOT_FOUND);
        }
        else {
            dispatchOSService.delete(dispatchOSModelOptional.get());
            return new ResponseEntity<>("Dispacho apagado com sucesso", HttpStatus.OK);
        }
    }

    @PutMapping("/dispatch/{id}")
    public ResponseEntity<Object> updateDispatch(@PathVariable(value = "id") Long id, @RequestBody @Valid DispatchOSDTO dispatchOSDTO){
        Optional<DispatchOSModel> dispatchOSModelOptional = dispatchOSService.findById(id);

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
