package com.disem.API.controllers;

import com.disem.API.dtos.OrderServiceDTO;
import com.disem.API.models.OrderServiceModel;
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
import java.util.UUID;

@RestController
//@RequestMapping("/api")
public class OrderServiceController {

    @Autowired
    OrderServiceService orderServiceService;

    @PostMapping("/orders")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Object> saveOrderService(@RequestBody @Valid OrderServiceDTO orderServiceDTO){
        var orderServiceModel = new OrderServiceModel();

        BeanUtils.copyProperties(orderServiceDTO, orderServiceModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderServiceService.save(orderServiceModel));
    }

    @GetMapping("/orders")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Object> getAllOrderService(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        Page<OrderServiceModel> ordersPage = orderServiceService.findAll(pageable);
        if (ordersPage.isEmpty()){
            return new ResponseEntity<>("Ordens de serviço não encontrado", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ordersPage, HttpStatus.OK);
    }

    @GetMapping("/orders/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Object> getOneOrderService(@PathVariable(value = "id") UUID id) {
        Optional<OrderServiceModel> orderService = orderServiceService.findById(id);
        if (orderService.isEmpty()){
            return new ResponseEntity<>("Ordem de serviço não encontrada", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orderService.get(), HttpStatus.OK);
    }

    @DeleteMapping("/orders/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Object> deleteoneOrderService(@PathVariable(value = "id") UUID id) {
        Optional<OrderServiceModel> orderService = orderServiceService.findById(id);
        if (orderService.isEmpty()){
            return new ResponseEntity<>("Ordem de serviço não encontrada", HttpStatus.NOT_FOUND);
        } else {
            orderServiceService.delete(orderService.get());
            return new ResponseEntity<>("Ordem de serviço apagada", HttpStatus.OK);
        }
    }

    @PutMapping("/orders/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Object> updateOrderService(@PathVariable(value = "id") UUID id, @RequestBody @Valid OrderServiceDTO orderServiceDTO) {
        Optional<OrderServiceModel> orderServiceModelOptional = orderServiceService.findById(id);
        if (orderServiceModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ordem de serviço não encontrada");
        } else {
            var orderServiceModel = orderServiceModelOptional.get();

            orderServiceModel.setOrigin(orderServiceDTO.getOrigin());
            orderServiceModel.setRequisition(orderServiceDTO.getRequisition());
            orderServiceModel.setClassification(orderServiceDTO.getClassification());
            orderServiceModel.setUnit(orderServiceDTO.getUnit());
            orderServiceModel.setRequester(orderServiceDTO.getRequester());
            orderServiceModel.setContact(orderServiceDTO.getContact());
            orderServiceModel.setPreparationObject(orderServiceDTO.getPreparationObject());
            orderServiceModel.setTypeMaintenance(orderServiceDTO.getTypeMaintenance());
            orderServiceModel.setSystem(orderServiceDTO.getSystem());
            orderServiceModel.setMaintenanceUnit(orderServiceDTO.getMaintenanceUnit());
            orderServiceModel.setMaintenanceIndicators(orderServiceDTO.getMaintenanceIndicators());
            orderServiceModel.setObservation(orderServiceDTO.getObservation());
            orderServiceModel.setTypeTreatment(orderServiceDTO.getTypeTreatment());
            orderServiceModel.setStatus(orderServiceDTO.getStatus());
            orderServiceModel.setCreationDate(orderServiceDTO.getCreationDate());
            orderServiceModel.setModificationDate(orderServiceDTO.getModificationDate());
            orderServiceModel.setOpenDays(orderServiceDTO.getOpenDays());

            return ResponseEntity.status(HttpStatus.OK).body(orderServiceService.save(orderServiceModel));
        }
    }

}
