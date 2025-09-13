package com.example.springAI;

import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;

import java.util.List;

public class PromptModel {

    private final Prompt prompt;

    public PromptModel(String message) {
        Prompt prompt = new Prompt(
                List.of(
                        new SystemMessage("너는 친절한 AI 비서야. 절대 추론과정을 보여주지 말고 요약만 응답해."),
                        new UserMessage( message )
                ));

        this.prompt = prompt;
    }

    public Prompt getPrompt() {
        return prompt;
    }
}
