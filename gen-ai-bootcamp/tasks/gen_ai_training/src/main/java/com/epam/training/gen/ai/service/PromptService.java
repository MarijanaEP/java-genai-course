package com.epam.training.gen.ai.service;

import com.microsoft.semantickernel.services.ServiceNotFoundException;

public interface PromptService {
    String getChatCompletion(String userPrompt) throws ServiceNotFoundException;
}
