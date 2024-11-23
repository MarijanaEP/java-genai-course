package com.epam.training.gen.ai.prompt;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewPromptService {

    private final Kernel kernel;
    private final InvocationContext invocationContext;

    public NewPromptService(Kernel kernel, InvocationContext invocationContext) {
        this.kernel = kernel;
        this.invocationContext = invocationContext;
    }

    private final ChatHistory chatHistory = new ChatHistory();

    public String getSimpleChatCompletions(String userPrompt) {
        var block = kernel.invokePromptAsync(userPrompt).block();
        Object result = block.getResult();
        return (result instanceof String s) ? s : null;
    }


    public String getChatCompletionsWithHistory(String userPrompt) throws ServiceNotFoundException {
        chatHistory.addUserMessage(userPrompt);
        ChatCompletionService service = kernel.getService(ChatCompletionService.class);
        List<ChatMessageContent<?>> results = service.getChatMessageContentsAsync(chatHistory, kernel, invocationContext).block();
        chatHistory.addAll(results);
        ChatMessageContent<?> last = results.get(results.size() - 1);
        return (last.getAuthorRole() == AuthorRole.ASSISTANT) ? last.getContent() : null;
    }
}
