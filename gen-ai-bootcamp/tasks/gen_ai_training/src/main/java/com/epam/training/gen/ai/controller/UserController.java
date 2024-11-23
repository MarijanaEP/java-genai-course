package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.UserRequest;
import com.epam.training.gen.ai.dto.UserResponse;
import com.epam.training.gen.ai.prompt.NewPromptService;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/v1/user")
public class UserController {

    private final NewPromptService newPromptService;

    public UserController(NewPromptService newPromptService) {
        this.newPromptService = newPromptService;
    }

    @PostMapping("/prompt")
    public ResponseEntity<UserResponse> simplePrompt(@RequestBody UserRequest userRequest) throws ServiceNotFoundException {
        String chatCompletion = newPromptService.getChatCompletionsWithHistory(userRequest.getUserPrompt());
        return ResponseEntity.ok(new UserResponse(chatCompletion));
    }
}
