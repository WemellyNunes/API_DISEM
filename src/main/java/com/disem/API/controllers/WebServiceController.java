package com.disem.API.controllers;

import com.disem.API.services.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping("/login")
    public String autenticarUsuario(@RequestParam String login, @RequestParam String senha) {
        return webService.autenticarUsuario(login, senha);
    }
}
