package com.disem.API.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
@Service
public class WebService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.webservice.token.url}")
    private String tokenUrl;

    @Value("${spring.webservice.buscarVinculo.url}")
    private String buscarVinculoUrl;

    @Value("${spring.webservice.login.url}")
    private String loginUrl;


    public String getToken() {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, null, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                String newToken = response.getBody().replace("\"", "").trim();
                return newToken;
            } else {
                System.err.println("⚠️ Erro ao gerar token: " + response.getStatusCode());
                throw new RuntimeException("Erro ao gerar token.");
            }
        } catch (Exception e) {
            System.err.println("⚠️ Erro ao gerar token: " + e.getMessage());
            throw new RuntimeException("Erro ao gerar token.", e);
        }
    }

    public Map<String, Object> buscarPessoaComVinculo(String login) {
        String token = getToken();
        String url = buscarVinculoUrl + "?login=" + login + "&token=" + token;

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new RuntimeException("Erro ao buscar pessoa por vínculo. Código: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar pessoa por vínculo.", e);
        }
    }

    public String autenticarUsuario(String login, String senha) {
        String token = getToken();
        String senhaBase64 = Base64.getEncoder().encodeToString(senha.getBytes(StandardCharsets.UTF_8));

        String finalUrl = loginUrl + "?login=" + login + "&senha=" + senhaBase64 + "&token=" + token;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        System.out.println("🔐 Chamando API com a URL: " + finalUrl);

        try {
            ResponseEntity<String> response = restTemplate.exchange(finalUrl, HttpMethod.POST, requestEntity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new RuntimeException("Erro ao autenticar usuário. Código: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao autenticar usuário.", e);
        }
    }
}
