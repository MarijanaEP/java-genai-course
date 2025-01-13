package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.service.RAGService;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/rag")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RAGController {

    private final RAGService ragService;

    // Upload Knowledge Base
    @PostMapping("knowledge")
    public ResponseEntity<?> uploadKnowledge(@RequestParam String text) throws ExecutionException, InterruptedException {
        ragService.uploadKnowledge(text);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Ask Question
    @PostMapping("ask-question")
    public ResponseEntity<?> askQuestion(@RequestParam String question) throws ServiceNotFoundException, ExecutionException, InterruptedException {
        String answer = ragService.askQuestion(question);
        return ResponseEntity.ok(answer);
    }


}
