package com.deathmetalmania.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GroqAiService implements AiService {

    private static final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String MODEL = "llama-3.3-70b-versatile";

    @Value("${groq.api.key}")
    private String apiKey;

    @Override
    public String chat(String systemPrompt, List<Map<String, String>> userMessages) {
        if (apiKey == null || apiKey.isBlank()) {
            return "GROQ_API_KEY not set — add it as an environment variable in Koyeb to activate the Oracle.";
        }

        List<Message> messages = new ArrayList<>();
        messages.add(new Message("system", systemPrompt));
        for (Map<String, String> m : userMessages) {
            messages.add(new Message(m.get("role"), m.get("content")));
        }

        GroqRequest request = new GroqRequest(MODEL, messages, 600, 0.85);

        RestClient client = RestClient.create();
        GroqResponse response = client.post()
                .uri(GROQ_URL)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(request)
                .retrieve()
                .body(GroqResponse.class);

        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return "The Oracle could not speak. Try again.";
        }

        return response.getChoices().get(0).getMessage().getContent();
    }

    // --- Request models (annotations on getters so Jackson serializes them correctly) ---

    static class GroqRequest {
        private final String model;
        private final List<Message> messages;
        private final int maxTokens;
        private final double temperature;

        GroqRequest(String model, List<Message> messages, int maxTokens, double temperature) {
            this.model = model;
            this.messages = messages;
            this.maxTokens = maxTokens;
            this.temperature = temperature;
        }

        @JsonProperty("model")
        public String getModel() { return model; }

        @JsonProperty("messages")
        public List<Message> getMessages() { return messages; }

        @JsonProperty("max_tokens")
        public int getMaxTokens() { return maxTokens; }

        @JsonProperty("temperature")
        public double getTemperature() { return temperature; }
    }

    static class Message {
        private final String role;
        private final String content;

        Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        @JsonProperty("role")
        public String getRole() { return role; }

        @JsonProperty("content")
        public String getContent() { return content; }
    }

    // --- Response models ---

    static class GroqResponse {
        private List<Choice> choices;

        @JsonProperty("choices")
        public List<Choice> getChoices() { return choices; }
        public void setChoices(List<Choice> choices) { this.choices = choices; }
    }

    static class Choice {
        private ChoiceMessage message;

        @JsonProperty("message")
        public ChoiceMessage getMessage() { return message; }
        public void setMessage(ChoiceMessage message) { this.message = message; }
    }

    static class ChoiceMessage {
        private String content;

        @JsonProperty("content")
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}
