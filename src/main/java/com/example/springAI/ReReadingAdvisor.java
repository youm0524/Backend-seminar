package com.example.springAI;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.function.Consumer;

public class ReReadingAdvisor implements CallAdvisor, StreamAdvisor {

    private int order = 1;
    public ReReadingAdvisor(){
    }
    public ReReadingAdvisor(int order){
        this.order = order;
    }

    // 추론 능력을 향상시키는 다시 읽기(Re-Reading, Re2)라는 기법을 적용(Spring ai에서 예시로 소개함)
    private ChatClientRequest before(ChatClientRequest advisedRequest) {
        // 입력한 사용자 프롬프트를 가져온다.
        String userText = advisedRequest.prompt().getUserMessage().getText();
        // 프롬프트에 추가적인 내용을 적어서 전달
        String augmented = userText + "\nRead the question again: " + userText;

        return advisedRequest.builder()
                .prompt(advisedRequest.prompt().augmentUserMessage(augmented))
                .context(advisedRequest.context())
                .build();
    }
    // 한번에 응답해주는 함수
    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        // llm에 요청전 Re-Reading, Re2라는 기법 적용
        ChatClientRequest newChatClientRequest = this.before(chatClientRequest);
        // llm 호출
        ChatClientResponse chatClientResponse =callAdvisorChain.nextCall(newChatClientRequest);
        return chatClientResponse;
    }

    // 스트리밍식으로 사용되는 함수
    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {

        return streamAdvisorChain.nextStream(this.before(chatClientRequest));
    }

    @Override
    public String getName() {
        // advisor 이름
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        // 실행되는 우선 순위
        return this.order;
    }
}
