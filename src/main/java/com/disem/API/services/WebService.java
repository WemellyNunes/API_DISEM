package com.disem.API.services;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class WebService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.webservice.token.url}")
    private String tokenUrl;

    @Value("${spring.webservice.link.url}")
    private String buscarVinculoUrl;

    private String currentToken;

    public String getToken() {
        if (currentToken == null) {
            refreshToken();
        }
        return currentToken;
    }

    public Map<String, Object> buscarPessoaComVinculo(String login) {
        String url = buscarVinculoUrl + "?login=" + login + "&token=" + getToken();

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Erro ao buscar pessoa por vinculo.");
        }
    }

    @Scheduled(fixedRate = 30 * 60 * 1000) // Renova a cada 30 min
    public void refreshToken() {
        ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, null, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            currentToken = response.getBody();
            System.out.println("Novo token gerado: " + currentToken);
        } else {
            System.err.println("Erro ao gerar token.");
        }
    }
}
