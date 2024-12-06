package com.epam.training.gen.ai.controller;


import com.azure.ai.openai.OpenAIAsyncClient;
import com.epam.training.gen.ai.dto.UserRequest;
import com.epam.training.gen.ai.dto.UserResponse;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/modelTesting")
@RequiredArgsConstructor
public class ModelTestingController {

    private final OpenAIAsyncClient openAIAsyncClient;
    private final InvocationContext invocationContext;

    @PostMapping("/model/{model}/prompt")
    public ResponseEntity<UserResponse> promptWithModel(@RequestBody UserRequest userRequest, @PathVariable("model") String model) throws ServiceNotFoundException {
        ChatCompletionService promptWithModelService = getChatCompletionService(model);

        Kernel kernel = getKernel(promptWithModelService);

        ChatHistory chatHistory = new ChatHistory();
        chatHistory.addUserMessage(userRequest.getUserPrompt());

        List<ChatMessageContent<?>> response =
                promptWithModelService.getChatMessageContentsAsync(chatHistory, kernel, invocationContext).block();

        ChatMessageContent<?> chatMessageContent = response.stream().filter(content -> content.getAuthorRole() == AuthorRole.ASSISTANT).findFirst().get();
        String modelCompletion = chatMessageContent.getContent();

        return ResponseEntity.ok(new UserResponse(modelCompletion));
    }

    private static Kernel getKernel(ChatCompletionService promptWithModelService) {
        return Kernel.builder()
                .withAIService(ChatCompletionService.class, promptWithModelService)
                .build();
    }

    private ChatCompletionService getChatCompletionService(String model) {
        return OpenAIChatCompletion.builder()
                .withModelId(model)
                .withOpenAIAsyncClient(openAIAsyncClient)
                .build();
    }
}
