package com.example;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class Host {
    private final ChatClient chatClient;

    public void printAnswerToUser(String question) {
        AssistantMessage assistantMessage = Objects.requireNonNull(
                        chatClient.prompt()
                                .user(question)
                                .call()
                                .chatResponse())
                .getResult()
                .getOutput();
        System.out.println(assistantMessage.getText());
    }
}
