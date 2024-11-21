package com.disem.API.controllers;

import com.disem.API.dtos.OrderServiceDTO;
import com.disem.API.enums.OrdersServices.*;
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


import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrderServiceController {

    @Autowired
    OrderServiceService orderServiceService;


    @PostMapping("/serviceOrder")
    public ResponseEntity<Object> saveOrderService(@RequestBody @Valid OrderServiceDTO orderServiceDTO){
        var orderServiceModel = new OrderServiceModel();

        BeanUtils.copyProperties(orderServiceDTO, orderServiceModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderServiceService.save(orderServiceModel));
    }


    @GetMapping("/serviceOrders")
    public ResponseEntity<Object> getAllOrderService() {
        List<OrderServiceModel> ordersList = orderServiceService.findAll();
        if (ordersList.isEmpty()) {
            return new ResponseEntity<>("Ordens de serviço não encontradas", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ordersList, HttpStatus.OK);
    }


    @GetMapping("/serviceOrder/{id}")
    public ResponseEntity<Object> getOneOrderService(@PathVariable(value = "id") Long id) {
        Optional<OrderServiceModel> orderService = orderServiceService.findById(id);
        if (orderService.isEmpty()){
            return new ResponseEntity<>("Ordem de serviço não encontrada", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orderService.get(), HttpStatus.OK);
    }


    @DeleteMapping("/serviceOrder/{id}")
    public ResponseEntity<Object> deleteOneOrderService(@PathVariable(value = "id") Long id) {
        Optional<OrderServiceModel> orderService = orderServiceService.findById(id);
        if (orderService.isEmpty()){
            return new ResponseEntity<>("Ordem de serviço não encontrada", HttpStatus.NOT_FOUND);
        }
        else {
            orderServiceService.delete(orderService.get());
            return new ResponseEntity<>("Ordem de serviço apagada", HttpStatus.OK);
        }
    }


    @PutMapping("/serviceOrder/{id}")
    public ResponseEntity<Object> updateOrderService(@PathVariable(value = "id") Long id, @RequestBody @Valid OrderServiceDTO orderServiceDTO) {
        Optional<OrderServiceModel> orderServiceModelOptional = orderServiceService.findById(id);

        if (orderServiceModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ordem de serviço não encontrada");
        }
        else {
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
            orderServiceModel.setDate(orderServiceDTO.getDate());
            orderServiceModel.setModificationDate(orderServiceDTO.getModificationDate());

            return ResponseEntity.status(HttpStatus.OK).body(orderServiceService.save(orderServiceModel));
        }
    }

    //CONSULTAS ESPECIFICAS

    @GetMapping("/serviceOrder/requisition/{requisition}")
    public ResponseEntity<Object> getByRequisition(@PathVariable(value = "requisition") Integer requisition){
        Optional<OrderServiceModel> orderServiceModelOptional = orderServiceService.findByRequisition(requisition);
        if (orderServiceModelOptional.isEmpty()){
            return new ResponseEntity<>("Ordem de serviço não encontrada", HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(orderServiceModelOptional.get(), HttpStatus.OK);
        }
    }


    @GetMapping("/serviceOrder/origin/{origin}")
    public ResponseEntity<Object> getByOrigin(@PathVariable(value = "origin") OriginEnum origin){
        List<OrderServiceModel> orderServiceModelList = orderServiceService.findByOrigin(origin);
        if (orderServiceModelList.isEmpty()){
            return new ResponseEntity<>("Ordem de serviço não encontrada", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orderServiceModelList, HttpStatus.OK);
    }


    @GetMapping("/serviceOrder/status/{status}")
    public ResponseEntity<Object> getByStatus(@PathVariable(value = "status")StatusEnum status){
        List<OrderServiceModel> orderServiceModelList = orderServiceService.findByStatus(status);
        if (orderServiceModelList.isEmpty()){
            return new ResponseEntity<>("Ordem de serviço não encontrada", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orderServiceModelList, HttpStatus.OK);
    }


    @GetMapping("/serviceOrder/requester/{requester}")
    public ResponseEntity<Object> getByRequester(@PathVariable(value = "requester") String requester){
        List<OrderServiceModel> orderServiceModelList = orderServiceService.findByRequester(requester);
        if (orderServiceModelList.isEmpty()){
            return new ResponseEntity<>("Ordem de serviço não encontrada", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orderServiceModelList, HttpStatus.OK);
    }


    @GetMapping("/serviceOrder/unit/{unit}")
    public ResponseEntity<Object> getByUnit(@PathVariable(value = "unit") String unit){
        List<OrderServiceModel> orderServiceModelList = orderServiceService.findByUnit(unit);
        if (orderServiceModelList.isEmpty()){
            return new ResponseEntity<>("Ordem de serviço não encontrada", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orderServiceModelList, HttpStatus.OK);
    }


    @GetMapping("/serviceOrder/system/{system}")
    public ResponseEntity<Object> getBySystem(@PathVariable(value = "system") SystemEnum system){
        List<OrderServiceModel> orderServiceModelList = orderServiceService.findBySystem(system);
        if (orderServiceModelList.isEmpty()){
            return new ResponseEntity<>("Ordem de serviço não enocntrada", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orderServiceModelList, HttpStatus.OK);
    }


    @GetMapping("/serviceOrder/classification/{classification}")
    public ResponseEntity<Object> getByClass(@PathVariable(value = "classification")ClassEnum classification){
        List<OrderServiceModel> orderServiceModelList = orderServiceService.findByClassification(classification);
        if (orderServiceModelList.isEmpty()){
            return new ResponseEntity<>("Ordem de serviço não enocntrada", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orderServiceModelList, HttpStatus.OK);
    }


    @GetMapping("serviceOrder/typeMaintenance/{type}")
    public ResponseEntity<Object> getByType(@PathVariable(value = "type")TypeMaintenanceEnum type){
        List<OrderServiceModel> orderServiceModelList = orderServiceService.findByTypeMaintenance(type);
        if (orderServiceModelList.isEmpty()){
            return new ResponseEntity<>("Ordem de serviço não encontrada", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orderServiceModelList, HttpStatus.OK);
    }


    /*@GetMapping("orders/creation/{date}")
    public ResponseEntity<Object> getByCreationDate(@PathVariable(value = "date") LocalDate date){
        List<OrderServiceModel> orderServiceModelList = orderServiceService.findByCreationDate(date);
        if (orderServiceModelList.isEmpty()){
            return new ResponseEntity<>("Ordem de serviço não encontrada", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orderServiceModelList, HttpStatus.OK);
    }*/

}



