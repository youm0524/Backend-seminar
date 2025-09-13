package com.example.springAI;


import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    private final Advisor advisor;

    @GetMapping("/ask")
    public String ask(@RequestParam("message") String message) {
        return chatService.askToOllama(message);
    }

    @GetMapping("/advisor")
    public String advisor(@RequestParam("message") String message) { return advisor.useAdvisor(message);}

}
