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


}
