package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.UserRequest;
import com.epam.training.gen.ai.dto.UserResponse;
import com.epam.training.gen.ai.service.PromptService;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/completion")
@RequiredArgsConstructor()
public class ChatCompletionController {

    private final PromptService simplePromptService;
    private final PromptService promptWithHistoryService;
    private final PromptService promptWithPluginsService;

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

    @PostMapping("/promptWithPlugins")
    public ResponseEntity<UserResponse> promptWithPlugins(@RequestBody UserRequest userRequest) throws ServiceNotFoundException {
        String chatCompletion = promptWithPluginsService.getChatCompletion(userRequest.getUserPrompt());
        return ResponseEntity.ok(new UserResponse(chatCompletion));
    }

}
