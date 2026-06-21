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
            return "The Oracle is offline — GROQ_API_KEY not configured.";
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

    // --- Request models ---

    static class GroqRequest {
        @JsonProperty("model") private String model;
        @JsonProperty("messages") private List<Message> messages;
        @JsonProperty("max_tokens") private int maxTokens;
        @JsonProperty("temperature") private double temperature;

        GroqRequest(String model, List<Message> messages, int maxTokens, double temperature) {
            this.model = model;
            this.messages = messages;
            this.maxTokens = maxTokens;
            this.temperature = temperature;
        }

        public String getModel() { return model; }
        public List<Message> getMessages() { return messages; }
        public int getMaxTokens() { return maxTokens; }
        public double getTemperature() { return temperature; }
    }

    static class Message {
        @JsonProperty("role") private String role;
        @JsonProperty("content") private String content;

        Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() { return role; }
        public String getContent() { return content; }
    }

    // --- Response models ---

    static class GroqResponse {
        @JsonProperty("choices") private List<Choice> choices;
        public List<Choice> getChoices() { return choices; }
        public void setChoices(List<Choice> choices) { this.choices = choices; }
    }

    static class Choice {
        @JsonProperty("message") private ChoiceMessage message;
        public ChoiceMessage getMessage() { return message; }
        public void setMessage(ChoiceMessage message) { this.message = message; }
    }

    static class ChoiceMessage {
        @JsonProperty("content") private String content;
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}
