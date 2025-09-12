package com.example.springAI;


import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ChatService {

     private final OllamaChatModel chatModel;
//        private final ChatClient chatClient;

    public String askToOllama(String message) {
        Prompt prompt = new Prompt(
                List.of(
                        new SystemMessage("너는 친절한 AI 비서야. 절대 추론 과정을 보여주지 말고 요약만 응답해. 답변은 반드시 한국어로만 해야 하며, 영어 표현을 사용하지 마. 단답으로 하지말라고"),
                        new UserMessage(message)
                ));

        return ChatClient.create(chatModel)
                .prompt(prompt)
                .call()
                .content();
        //String response = String.valueOf(chatModel.call(prompt));
        //String answer = String.valueOf(response.getResult().getOutput());
        //return response;
        //System.out.println(response.getResult().getOutput());

//        return chatModel
//                .call(prompt);    // Prompt 객체를 그대로 사용 ;// 응답 메시지 컨테이너
    }

//    public String askToOllama(String message){
//        Prompt prompt = new Prompt(
//                List.of(
//                        new SystemMessage("너는 친절한 AI 비서야. 절대 추론 과정을 보여주지 말고 요약만 응답해."),
//                        new UserMessage(message)
//                ));
//
//        return chatClient.prompt(prompt)
//                .call()
//                .content();
//
//    }
}
