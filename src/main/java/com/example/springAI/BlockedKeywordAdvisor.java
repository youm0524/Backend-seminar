package com.example.springAI;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;

import java.util.ArrayList;
import java.util.List;

public class BlockedKeywordAdvisor implements CallAdvisor {

    private List<String> blockedWords = List.of("죽음", "자살");
    private int order = 0;
    public BlockedKeywordAdvisor(){
    }
    public BlockedKeywordAdvisor(List<String> keywords){
        this.blockedWords = keywords;
    }
    public BlockedKeywordAdvisor(List<String> keywords, int order){
        this.blockedWords = keywords;
        this.order = order;
    }


    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        ChatClientResponse chatClientResponse =callAdvisorChain.nextCall(chatClientRequest);

        //Generation는 llm의 응답을 담은 객체
        List<Generation> sanitizedGenerations = new ArrayList<>();
        for(Generation generation : chatClientResponse.chatResponse().getResults()){
            String outputText= generation.getOutput().getText();
            for(String blockedWord : blockedWords){
                // 응답에서 금칙어 ***로 변경하는 코드
                outputText = outputText.replaceAll(blockedWord,"***");
            }
            sanitizedGenerations.add(new Generation(new AssistantMessage(outputText),generation.getMetadata()));
        }
        // 변경한 답변으로 새로운 ChatResponse를 만듬
        ChatResponse chatResponse = ChatResponse.builder()
                .from(chatClientResponse.chatResponse())
                .generations(sanitizedGenerations)
                .build();
        return ChatClientResponse.builder()
                .chatResponse(chatResponse)
                .context(chatClientResponse.context())
                .build();
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
