package com.example.springAI;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
public class Advisor {
    private final OllamaChatModel chatModel;

    public String useAdvisor(String message){

//        Prompt prompt = new Prompt(
////                List.of(
////                        new SystemMessage("너는 친절한 AI 비서야. 절대 추론과정을 보여주지 말고 요약만 응답해."),
////                        new UserMessage( message )
////                ));



        ChatClient chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor(1)) //1. default로 advisor 연결
                .build();

        String response = chatClient
                .prompt(new PromptModel(message).getPrompt())
                .advisors(new ReReadingAdvisor(0)) // 2. 실행시 advisor 연결
                .call()
                .content();

        Prompt prompt1 = new Prompt(
                List.of(
                        new SystemMessage("너는 친절한 AI 비서야. 절대 추론과정을 보여주지 말고 요약만 응답해."),
                        new UserMessage( response )
                ));

        String afterResponse = chatClient
                .prompt(prompt1)
                .advisors(new BlockedKeywordAdvisor()) // 2. 실행시 advisor 연결
                .call()
                .content();
        return afterResponse;
    }
}
