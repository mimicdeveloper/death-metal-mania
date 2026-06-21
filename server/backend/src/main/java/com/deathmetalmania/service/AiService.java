package com.deathmetalmania.service;

import java.util.List;
import java.util.Map;

public interface AiService {
    String chat(String systemPrompt, List<Map<String, String>> messages);
}
