package com.example.springAI;


import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class ChatService {

     private final OllamaChatModel chatModel;

     @Autowired
    ChatMemory chatMemory;

//     @Bean
//     ChatMemory chatMemory(){
//         return MessageWindowChatMemory.builder().maxMessages(10).build();
//     }

    public String askToOllama(String message) {
        Prompt prompt = new Prompt(
                List.of(
                        new SystemMessage("너는 친절한 한국어 AI 비서입니다. 절대 영어, 스페인어, 중국어 등 외국어를 사용하지 말고, 모든 답변을 순수한 한국어로만 작성해야 합니다. 추론 과정이나 불필요한 설명 없이 요약만 제공합니다.\n"),
                        new UserMessage(message)
                ));

        return ChatClient.create(chatModel)
                .prompt(prompt)
                .call()
                .content();
    }
    /**
     * entity() 방식: JSON → DTO 매핑
     */
    public WeatherInfo askWeatherInfo(String city) {
        PromptTemplate template = new PromptTemplate(
                """
                {city} 의 현재 날씨를 JSON 형식으로만 출력해라.
                반드시 아래 3개의 필드를 포함:
                - city: 입력받은 도시 이름 그대로
                - status: 날씨 상태 (Sunny, Cloudy, Rainy 등)
                - temperature: 섭씨 온도, 정수값
                
                """
        );

        Prompt prompt = template.create(Map.of("city", city));

        return ChatClient.create(chatModel)
                .prompt(prompt)
                .call()
                .entity(WeatherInfo.class);
    }

    /**
     * raw Response 방식
     */
    public ChatResponse askRaw(String message) {
        Prompt prompt = new Prompt(
                List.of(new UserMessage(message))
        );

        return ChatClient.create(chatModel)
                .prompt(prompt)
                .call()
                .chatResponse();
    }

    public Flux<String> askToOllamaStreaming(String message) {
        Prompt prompt = new Prompt(new UserMessage(message));

        return ChatClient.create(chatModel)
                .prompt(prompt)
                .stream()       // ✅ 스트리밍 활성화
                .content();     // Flux<String> 으로 토큰이 순차적으로 흘러옴
    }


    public String memoryGeneration(String userInput, String userId) {
        return ChatClient.create(chatModel).prompt().user(userInput).advisors(MessageChatMemoryAdvisor.builder(chatMemory).conversationId(userId).build())
                .call()
                .content();
    }
}
