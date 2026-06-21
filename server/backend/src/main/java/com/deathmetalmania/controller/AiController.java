package com.deathmetalmania.controller;

import com.deathmetalmania.service.AiService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    private static final String ORACLE_SYSTEM = """
You are the Death Metal Oracle — an ancient, all-knowing entity that has witnessed every riff, every blastbeat, and every guttural vocal since the dawn of extreme music. You speak with brutal authority and genuine passion about death metal, slam, goregrind, grindcore, crust punk, blackened death, technical death, and all extreme music subgenres. You know every band, album, label, and scene from Florida to Stockholm to Tokyo. You are specific, knowledgeable, and enthusiastic — never vague, never generic. Keep answers under 200 words unless the user explicitly asks for a deep dive. Speak like a true metalhead, not a textbook.
""";

    private static final String RECOMMEND_SYSTEM = """
You are a death metal expert recommender. The user will describe a vibe, mood, or sound they want, and you will recommend exactly 5 real extreme metal bands that match. Use this format for each:
**Band Name** (subgenre, country) — one sentence on why they match.
Be specific. Include a mix of classics and newer acts. Only recommend real bands with real albums.
""";

    @PostMapping("/oracle")
    public Map<String, String> oracle(@RequestBody Map<String, Object> body) {
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, String>> history = (List<Map<String, String>>) body.getOrDefault("history", new ArrayList<>());
            String message = (String) body.getOrDefault("message", "");

            if (message.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message required");

            List<Map<String, String>> messages = new ArrayList<>(history);
            Map<String, String> userMsg = new HashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", message);
            messages.add(userMsg);

            String response = aiService.chat(ORACLE_SYSTEM, messages);
            Map<String, String> result = new HashMap<>();
            result.put("response", response);
            return result;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oracle error: " + e.getMessage());
        }
    }

    @PostMapping("/recommend")
    public Map<String, String> recommend(@RequestBody Map<String, String> body) {
        try {
            String description = body.getOrDefault("description", "").trim();
            if (description.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description required");

            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> userMsg = new HashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", description);
            messages.add(userMsg);

            String response = aiService.chat(RECOMMEND_SYSTEM, messages);
            Map<String, String> result = new HashMap<>();
            result.put("response", response);
            return result;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Recommend error: " + e.getMessage());
        }
    }
}
