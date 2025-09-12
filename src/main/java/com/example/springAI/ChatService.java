package com.example.springAI;


import lombok.RequiredArgsConstructor;
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


    public String askToOllama(String message) {
//        Prompt prompt = new Prompt(
//                List.of(
//                        new SystemMessage("너는 친절한 AI 비서야. 절대 추론 과정을 보여주지 말고 요약만 응답해."),
//                        new UserMessage(message)
//                ));
        String response = chatModel.call(message);
        //String answer = String.valueOf(response.getResult().getOutput());
        return response;
        //System.out.println(response.getResult().getOutput());

//        return chatModel
//                .call(prompt);    // Prompt 객체를 그대로 사용 ;// 응답 메시지 컨테이너
    }
}
