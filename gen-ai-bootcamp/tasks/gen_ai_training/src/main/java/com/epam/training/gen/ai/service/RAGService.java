package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.vector.SimpleVectorActions;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionArguments;
import com.microsoft.semantickernel.semanticfunctions.PromptTemplateConfig;
import com.microsoft.semantickernel.semanticfunctions.PromptTemplateFactory;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import io.qdrant.client.grpc.Points;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RAGService {

    public static final String PROMPT_TEMPLATE = """
            <ask>You are an assistant for question-answering tasks. Answer the question from the given context. If the context doesn't contain the answer say I don't know. Use three sentences maximum and keep the answer concise.</ask>
            <userQuestion> {{$question}} </userQuestion>
            <context> {{$context}} </context>
            Answer:""".stripIndent();
    private final SimpleVectorActions simpleVectorActions;
    private final PromptService simplePromptService;
    private final Kernel kernel;


    public String askQuestion(String question) throws ExecutionException, InterruptedException, ServiceNotFoundException {
        // form the context from similar vectors
        List<String> results = searchSimilarVectors(question);
        String context = String.join("\n", results);
        // form a prompt with context
        String renderedPrompt = getRenderedPrompt(question, context);
        return simplePromptService.getChatCompletion(renderedPrompt);
    }

    private String getRenderedPrompt(String question, String context) {
        var arguments = KernelFunctionArguments
                .builder()
                .withVariable("context", context)
                .withVariable("question", question)
                .build();

        String renderedPrompt = PromptTemplateFactory
                .build(PromptTemplateConfig.builder().withTemplate(PROMPT_TEMPLATE).build())
                .renderAsync(kernel, arguments, null)
                .block();

//        log.info("Rendered prompt: [{}]", renderedPrompt);
        return renderedPrompt;
    }

    private List<String> searchSimilarVectors(String question) throws ExecutionException, InterruptedException {
        List<Points.ScoredPoint> scoredPointList = simpleVectorActions.search(question);
        return scoredPointList.stream().map(point -> point.getPayloadOrThrow("info").getStringValue())
                .toList();
    }

    public void uploadKnowledge(String text) throws ExecutionException, InterruptedException {
        log.info("Processing and saving text: [{}]", text);
        simpleVectorActions.processAndSaveText(text);
    }
}
