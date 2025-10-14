package com.rouby.assistant.prompt.infrastructure.ai.config;

import java.time.Duration;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class AssistantClientConfig {

    @Value("${assistant.api.read-timeout-seconds:60}")
    private int readTimeoutSeconds;
    @Value("${assistant.api.connect-timeout-seconds:5}")
    private int connectTimeoutSeconds;

    @Bean(name="llmRestClientBuilder")
    public RestClient.Builder restClientBuilder() {

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(connectTimeoutSeconds).toMillisPart());   // 연결 시도 제한
        factory.setReadTimeout(Duration.ofSeconds(readTimeoutSeconds).toMillisPart());     // 응답 대기 제한

        return RestClient.builder().requestFactory(factory);
    }

    @Bean
    public OpenAiApi openAiApi(
        @Value("${spring.ai.openai.api-key}") String apiKey,
        @Value("${spring.ai.openai.chat.base-url:https://api.openai.com}") String baseUrl,
        @Value("${spring.ai.openai.chat.completions-path:/v1/chat/completions}") String completionsPath,
        @Qualifier("llmRestClientBuilder") RestClient.Builder rest
    ) {
        return OpenAiApi.builder()
            .baseUrl(baseUrl)
            .apiKey(apiKey)
            .restClientBuilder(rest)
            .completionsPath(completionsPath)
            .build();
    }

    @Bean
    public OpenAiChatModel openAiChatModel(
        OpenAiApi openAiApi,
        @Value("${spring.ai.openai.chat.options.model}") String model,
        @Value("${spring.ai.openai.chat.options.temperature:0.0}") double temp
    ) {
        var options = OpenAiChatOptions.builder()
            .model(model)
            .temperature(temp)
            .build();

        return OpenAiChatModel.builder()
            .openAiApi(openAiApi)
            .defaultOptions(options)
            .build();
    }

    @Bean
    public ChatClient chatClient(OpenAiChatModel model) {
        return ChatClient.builder(model).build();
    }
}