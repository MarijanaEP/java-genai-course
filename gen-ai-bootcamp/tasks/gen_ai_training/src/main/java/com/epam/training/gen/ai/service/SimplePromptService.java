package com.epam.training.gen.ai.service;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("simplePromptService")
@RequiredArgsConstructor()
public class SimplePromptService implements PromptService {

    private final Kernel kernel;

    @Override
    public String getChatCompletion(String userPrompt) throws ServiceNotFoundException {
        var block = kernel.invokePromptAsync(userPrompt).block();
        Object result = block.getResult();
        return (result instanceof String s) ? s : null;
    }
}
