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

    private String currentToken;

    public String getToken() {
        if (currentToken == null) {
            refreshToken();
        }
        return currentToken;
    }

    public Map<String, Object> buscarPessoaComVinculo(String login) {
        String token = getToken().replace("\"", "").trim();
        String url = buscarVinculoUrl + "?login=" + login + "&token=" + token;

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                System.err.println("Erro na resposta da API: " + response.getStatusCode());
                throw new RuntimeException("Erro ao buscar pessoa por vínculo. Código: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Erro ao chamar API: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar pessoa por vínculo.", e);
        }
    }

    public String autenticarUsuario(String login, String senha) {
        String token = getToken().replace("\"", "").trim();
        String senhaBase64 = Base64.getEncoder().encodeToString(senha.getBytes(StandardCharsets.UTF_8));

        String finalUrl = loginUrl + "?login=" + login + "&senha=" + senhaBase64 + "&token=" + token;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = "login=" + login + "&senha=" + senhaBase64 + "&token=" + token;

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        System.out.println("Chamando API com a URL: " + finalUrl);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    finalUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Sucesso na autenticação: " + response.getBody());
                return response.getBody();
            } else {
                System.err.println("Erro na resposta da API: " + response.getStatusCode());
                throw new RuntimeException("Erro ao autenticar usuário. Código: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Erro ao chamar API de autenticação: " + e.getMessage());
            throw new RuntimeException("Erro ao autenticar usuário.", e);
        }
    }

    @Scheduled(fixedRate = 30 * 60 * 1000) // gera a cad 30 min
    public void refreshToken() {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, null, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                currentToken = response.getBody().replace("\"", "").trim();
                System.out.println("Novo token gerado: " + currentToken);
            } else {
                System.err.println("Erro ao gerar token: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Erro ao gerar token: " + e.getMessage());
        }
    }
}
