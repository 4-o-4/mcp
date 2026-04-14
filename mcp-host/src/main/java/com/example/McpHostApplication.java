package com.example;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootApplication
public class McpHostApplication {
    @Bean
    public OpenAiApi openAiApi(
            @Value("${spring.ai.openai.base-url}") String baseUrl,
            @Value("${spring.ai.openai.api-key}") String apiKey,
            @Value("${router.http-referer}") String httpReferer,
            @Value("${router.site-title}") String siteTitle) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("HTTP-Referer", httpReferer);
        headers.add("X-Title", siteTitle);
        return OpenAiApi.builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .headers(headers)
                .build();
    }

    @Bean
    public ChatModel chatModel(
            OpenAiApi openAiApi,
            @Value("${spring.ai.openai.chat.options.model}") String model) {
        return OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(model)
                        .build())
                .build();
    }

    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultOptions(OpenAiChatOptions.builder()
                        .temperature(0.1)
                        .topP(0.95)
                        .build())
                .build();
    }

    public static void main(String[] args) {
        Host host = SpringApplication.run(McpHostApplication.class, args).getBean(Host.class);
        host.printAnswerToUser("");
    }
}
