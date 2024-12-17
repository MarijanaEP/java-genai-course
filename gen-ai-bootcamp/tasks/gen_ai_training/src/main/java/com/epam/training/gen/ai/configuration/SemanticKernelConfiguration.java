package com.epam.training.gen.ai.configuration;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.epam.training.gen.ai.plugin.LightsPlugin;
import com.epam.training.gen.ai.plugin.WeatherPlugin;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up Semantic Kernel components.
 * <p>
 * This configuration provides several beans necessary for the interaction with
 * Azure OpenAI services and the creation of kernel plugins. It defines beans for
 * chat completion services, kernel plugins, kernel instance, invocation context,
 * and prompt execution settings.
 */
@Configuration
public class SemanticKernelConfiguration {

    /**
     * Creates a {@link ChatCompletionService} bean for handling chat completions using Azure OpenAI.
     *
     * @param deploymentOrModelName the Azure OpenAI deployment or model name
     * @param openAIAsyncClient the {@link OpenAIAsyncClient} to communicate with Azure OpenAI
     * @return an instance of {@link ChatCompletionService}
     */
    @Bean
    public ChatCompletionService chatCompletionService(@Value("${client-azureopenai-deployment-name}") String deploymentOrModelName,
                                                       OpenAIAsyncClient openAIAsyncClient) {
        return OpenAIChatCompletion.builder()
                .withModelId(deploymentOrModelName)
                .withOpenAIAsyncClient(openAIAsyncClient)
                .build();
    }

    /**
     * Creates a {@link Kernel} bean to manage AI services and plugins.
     *
     * @param chatCompletionService the {@link ChatCompletionService} for handling completions
     * @return an instance of {@link Kernel}
     */
    @Bean
    public Kernel kernel(ChatCompletionService chatCompletionService) {
        return Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService)
                .build();
    }

    /**
     * Creates a {@link PromptExecutionSettings} bean with default settings.
     *
     * @return an instance of {@link PromptExecutionSettings}
     */
    @Bean
    public PromptExecutionSettings promptExecutionSettings() {
        return PromptExecutionSettings.builder()
                .withTemperature(0.8)
                .withMaxTokens(300)
                .withTopP(0.7)
                .build();
    }

    /**
     * Creates an {@link InvocationContext} bean with default prompt execution settings.
     *
     * @return an instance of {@link InvocationContext}
     */
    @Bean
    public InvocationContext invocationContext(PromptExecutionSettings promptExecutionSettings) {
        return InvocationContext.builder()
                .withPromptExecutionSettings(promptExecutionSettings)
                .build();
    }

    @Bean("weatherPlugin")
    public KernelPlugin weatherPlugin() {
        return KernelPluginFactory.createFromObject(new WeatherPlugin(), "WeatherPlugin");
    }

    @Bean("lightsPlugin")
    public KernelPlugin lightPlugin() {
        return KernelPluginFactory.createFromObject(new LightsPlugin(), "LightsPlugin");
    }

    @Bean
    public ChatHistory chatHistory() {
        // Can add here a system message to the chat history
        return new ChatHistory();
    }
}

