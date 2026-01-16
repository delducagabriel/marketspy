package com.marketspy.api.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ScraperService {

    // O RestTemplate é o cliente HTTP do Spring para chamadas síncronas
    private final RestTemplate restTemplate = new RestTemplate();
    private final String PYTHON_API_URL = "http://localhost:5000/scrape";

    public Map<String, Object> getProductDataFromPython(String url) {
        try {
            // Prepara o corpo da requisição (o JSON que o Flask espera)
            Map<String, String> request = new HashMap<>();
            request.put("url", url);

            // Faz o POST para o Python e recebe um Map (JSON)
            return restTemplate.postForObject(PYTHON_API_URL, request, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao conectar com o microserviço Python: " + e.getMessage());
        }
    }
}