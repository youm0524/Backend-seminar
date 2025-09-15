package com.example.springAI;


import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    private final Advisor advisor;

    private final CustomerService aiToolService;

    @GetMapping("/ask")
    public String ask(@RequestParam("message") String message) {
        return chatService.askToOllama(message);
    }
    // ✅ entity() 방식
    @GetMapping("/weather")
    public WeatherInfo weather(@RequestParam("city") String city) {
        return chatService.askWeatherInfo(city);
    }

    // ✅ raw Response 방식
    @GetMapping("/raw")
    public ChatResponse raw(@RequestParam("message") String message) {
        return chatService.askRaw(message);
    }
    @GetMapping("/advisor")
    public String advisor(@RequestParam("message") String message) { return advisor.useAdvisor(message);}

    @GetMapping("/ai-tool")
    public String aiTool(@RequestParam("message") String message) {return aiToolService.askAi(message);}

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@RequestParam("message") String message){
        return chatService.askToOllamaStreaming(message);
    }

    @GetMapping("/memory")
    public String memoryGeneration(@RequestParam("message") String message,
                                   @RequestParam("id") String id){
        return chatService.memoryGeneration(message, id);
    }
}
