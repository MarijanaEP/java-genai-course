package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.plugin.dto.LightModel;
import com.google.gson.Gson;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.contextvariables.ContextVariableTypeConverter;
import com.microsoft.semantickernel.contextvariables.ContextVariableTypes;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.InvocationReturnMode;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("promptWithPluginsService")
@RequiredArgsConstructor
@Slf4j
public class ChatWithPluginsService implements PromptService {

    private final OpenAIChatCompletion openAIChatCompletion;
    private final ChatHistory chatHistory;
    private final KernelPlugin weatherPlugin;
    private final KernelPlugin lightsPlugin;


    public String getChatCompletion(String userPrompt) throws ServiceNotFoundException {
        Kernel kernel = Kernel.builder()
                .withAIService(ChatCompletionService.class, openAIChatCompletion)
                .withPlugin(weatherPlugin)
                .withPlugin(lightsPlugin)
                .build();
        ContextVariableTypes.addGlobalConverter(ContextVariableTypeConverter.builder(LightModel.class).toPromptString(new Gson()::toJson).build());

        var chatCompletion = kernel.getService(ChatCompletionService.class);
        InvocationContext invocationContext =
                new InvocationContext.Builder()
                        .withReturnMode(InvocationReturnMode.LAST_MESSAGE_ONLY)
                        .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true))
                        .build();
        chatHistory.addUserMessage(userPrompt);

        var answer = chatCompletion
                .getChatMessageContentsAsync(chatHistory, kernel, invocationContext)
                .block();

        var aiResponse = answer.stream().findFirst().get().getContent();
        log.info("------------------------------------\n{}", aiResponse);
        return aiResponse;
    }
}

