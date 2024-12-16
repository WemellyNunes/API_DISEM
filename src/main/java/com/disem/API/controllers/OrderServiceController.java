package com.disem.API.controllers;

import com.disem.API.dtos.OrderServiceDTO;
import com.disem.API.enums.OrdersServices.*;
import com.disem.API.models.OrderServiceModel;
import com.disem.API.models.ProgramingModel;
import com.disem.API.services.EmailNotificationService;
import com.disem.API.services.EmailService;
import com.disem.API.services.OrderServiceService;
import jakarta.mail.MessagingException;
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


import java.util.*;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrderServiceController {

    @Autowired
    OrderServiceService orderServiceService;

    @Autowired
    EmailNotificationService emailNotificationService;

    public OrderServiceController(OrderServiceService orderServiceService, EmailNotificationService emailNotificationService) {
        this.orderServiceService = orderServiceService;
        this.emailNotificationService = emailNotificationService;
    }

    @PostMapping("/serviceOrder")
    public ResponseEntity<Object> saveOrderService(@RequestBody @Valid OrderServiceDTO orderServiceDTO){
        var orderServiceModel = new OrderServiceModel();
        BeanUtils.copyProperties(orderServiceDTO, orderServiceModel);

        var savedOrderService = orderServiceService.save(orderServiceModel);


        emailNotificationService.sendEmailAsync("wemellysnunes@gmail.com",
                "DISEM - Informativo de Ordem de Serviços",
                "A ordem de serviço de numero: "  + orderServiceDTO.getRequisition() + " foi cadastrada com sucesso!"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrderService);
    }


    @GetMapping("/serviceOrders")
    public ResponseEntity<Object> getAllOrderService() {
        List<OrderServiceModel> ordersList = orderServiceService.findAll();
        if (ordersList.isEmpty()) {
            return new ResponseEntity<>("Ordens de serviço não encontradas", HttpStatus.NOT_FOUND);
        }

        List<Map<String, Object>> responseList = new ArrayList<>();

        for (OrderServiceModel order : ordersList) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", order.getId());
            response.put("requisition", order.getRequisition());
            response.put("origin", order.getOrigin());
            response.put("classification", order.getClassification());
            response.put("unit", order.getUnit());
            response.put("requester", order.getRequester());
            response.put("contact", order.getContact());
            response.put("preparationObject", order.getPreparationObject());
            response.put("typeMaintenance", order.getTypeMaintenance());
            response.put("system", order.getSystem());
            response.put("maintenanceUnit", order.getMaintenanceUnit());
            response.put("campus", order.getCampus());
            response.put("maintenanceIndicators", order.getMaintenanceIndicators());
            response.put("observation", order.getObservation());
            response.put("typeTreatment", order.getTypeTreatment());
            response.put("status", order.getStatus());
            response.put("date", order.getDate());
            response.put("modificationDate", order.getModificationDate());
            response.put("openDays", order.getOpenDays());

            // Adiciona o `programingId` da programação ativa, se existir
            Optional<ProgramingModel> activeProg = order.getProgramings().stream()
                    .filter(programing -> "true".equals(programing.getActive()))
                    .findFirst();
            activeProg.ifPresent(programing -> response.put("programingId", programing.getId()));

            responseList.add(response);
        }

        // Aqui está o ponto principal: retornar a `responseList` que contém o `programingId`
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }


    @GetMapping("/serviceOrder/{id}")
    public ResponseEntity<Object> getOneOrderService(@PathVariable(value = "id") Long id) {
        Optional<OrderServiceModel> orderService = orderServiceService.findById(id);
        if (orderService.isEmpty()){
            return new ResponseEntity<>("Ordem de serviço não encontrada", HttpStatus.NOT_FOUND);
        }

        OrderServiceModel order = orderService.get();

        Map<String, Object> response = new HashMap<>();
        response.put("id", order.getId());
        response.put("requisition", order.getRequisition());
        response.put("origin", order.getOrigin());
        response.put("classification", order.getClassification());
        response.put("unit", order.getUnit());
        response.put("requester", order.getRequester());
        response.put("contact", order.getContact());
        response.put("preparationObject", order.getPreparationObject());
        response.put("typeMaintenance", order.getTypeMaintenance());
        response.put("system", order.getSystem());
        response.put("maintenanceUnit", order.getMaintenanceUnit());
        response.put("campus", order.getCampus());
        response.put("maintenanceIndicators", order.getMaintenanceIndicators());
        response.put("observation", order.getObservation());
        response.put("typeTreatment", order.getTypeTreatment());
        response.put("status", order.getStatus());
        response.put("date", order.getDate());
        response.put("modificationDate", order.getModificationDate());
        response.put("openDays", order.getOpenDays());

        Optional<ProgramingModel> activeProg = order.getProgramings().stream()
                .filter(programing -> "true".equals(programing.getActive())).findFirst();

        activeProg.ifPresent(programing -> response.put("programingId", programing.getId()));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PutMapping("/serviceOrder/{id}/status")
    public ResponseEntity<Object> updateOrderServiceStatus(
            @PathVariable(value = "id") Long id,
            @RequestBody String descricao) {

        System.out.println("Status recebido: " + descricao); // Log para verificar o valor recebido

        Optional<OrderServiceModel> orderService = orderServiceService.findById(id);
        if (orderService.isEmpty()) {
            return new ResponseEntity<>("Ordem de serviço não encontrada", HttpStatus.NOT_FOUND);
        }

        try {
            // Remove aspas extras, se existirem
            String statusLimpo = descricao.replace("\"", "").trim();
            StatusEnum statusEnum = StatusEnum.fromDescricao(statusLimpo);

            var order = orderService.get();
            order.setStatus(statusEnum);
            orderServiceService.save(order);

            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Status inválido: " + descricao, HttpStatus.BAD_REQUEST);
        }
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



