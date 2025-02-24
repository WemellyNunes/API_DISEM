package com.disem.API.controllers;

import com.disem.API.services.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/webservice")
public class WebServiceController {
    @Autowired
    WebService webService;

    @GetMapping
    public String getToken() {
        return webService.getToken();
    }

    @GetMapping("buscar-usuario")
    public Map<String, Object> getUser(@RequestParam String login) {
        return webService.buscarPessoaComVinculo(login);
    }
}
