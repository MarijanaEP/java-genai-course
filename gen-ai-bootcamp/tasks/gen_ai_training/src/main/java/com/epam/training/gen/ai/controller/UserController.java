package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.UserRequest;
import com.epam.training.gen.ai.dto.UserResponse;

import com.epam.training.gen.ai.service.PromptService;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor()
public class UserController {

    private final PromptService simplePromptService;
    private final PromptService promptWithHistoryService;

    @PostMapping("/prompt")
    public ResponseEntity<UserResponse> simplePrompt(@RequestBody UserRequest userRequest) throws ServiceNotFoundException {
        String chatCompletion = simplePromptService.getChatCompletion(userRequest.getUserPrompt());
        return ResponseEntity.ok(new UserResponse(chatCompletion));
    }

    @PostMapping("/promptWithHistory")
    public ResponseEntity<UserResponse> promptWithHistory(@RequestBody UserRequest userRequest) throws ServiceNotFoundException {
        String chatCompletion = promptWithHistoryService.getChatCompletion(userRequest.getUserPrompt());
        return ResponseEntity.ok(new UserResponse(chatCompletion));
    }
}
