package com.epam.training.gen.ai.service;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("promptWithHistoryService")
@RequiredArgsConstructor
public class ChatWithHistoryService implements PromptService {

    private final Kernel kernel;
    private final ChatHistory chatHistory;
    private final InvocationContext invocationContext;

    public String getChatCompletion(String userPrompt ) throws ServiceNotFoundException {
        chatHistory.addUserMessage(userPrompt);
        ChatCompletionService service = kernel.getService(ChatCompletionService.class);
        List<ChatMessageContent<?>> results = service.getChatMessageContentsAsync(chatHistory, kernel, invocationContext).block();
        chatHistory.addAll(results);
        ChatMessageContent<?> last = results.get(results.size() - 1);
        //print to console
        System.out.println("User: " + userPrompt);
        System.out.println("AI: " + last.getContent());
        return (last.getAuthorRole() == AuthorRole.ASSISTANT) ? last.getContent() : null;
    }
}
