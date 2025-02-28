package com.disem.API.controllers;

import com.disem.API.models.UserModel;
import com.disem.API.services.UserService;
import com.disem.API.services.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/webservice")
@CrossOrigin(origins = { "*"
}, allowedHeaders = "*")
public class WebServiceController {

    @Autowired
    WebService webService;

    @Autowired
    UserService userService;

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

    @PostMapping("/salvar-usuario")
    public ResponseEntity<String> salvarUsuario(@RequestBody UserModel user) {
        try {
            if (user.getIdUsuario() == null || user.getNome() == null) {
                return ResponseEntity.badRequest().body("ID do usuário e nome são obrigatórios.");
            }

            Optional<UserModel> existingUser = userService.findByIdUsuario(user.getIdUsuario());

            if (existingUser.isPresent()) {
                return ResponseEntity.ok("Usuário já cadastrado.");
            }
            userService.saveUser(user.getIdUsuario(), user.getNome(), user.getEmail(), user.getPapel());
            return ResponseEntity.ok("Usuário salvo com sucesso.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar usuário.");
        }
    }

    @GetMapping("/usuarios")
    public List<UserModel> listarUsuarios() {
        List<UserModel> users = userService.getAllUsers();
        return userService.getAllUsers();
    }
}
