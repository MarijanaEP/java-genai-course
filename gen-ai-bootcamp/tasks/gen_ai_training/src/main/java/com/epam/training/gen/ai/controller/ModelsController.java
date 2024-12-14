package com.epam.training.gen.ai.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
@RequestMapping("/api/v1/models")
public class ModelsController {

    @Value("${client-azureopenai-key}")
    private String azureOpenAiKey;

    @Value("${client-azureopenai-endpoint}")
    private String azureOpenAiUrl;

    @GetMapping
    public ResponseEntity<String> getModels() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(azureOpenAiUrl + "/openai/deployments"))
                    .setHeader("Api-Key", azureOpenAiKey)
                    .GET()
                    .build();
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            return ResponseEntity.ok(httpResponse.body());
        } catch (IOException | InterruptedException e) {
            // Log the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching models");
        }
    }
}
