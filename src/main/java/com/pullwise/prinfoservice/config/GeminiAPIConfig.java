package com.pullwise.prinfoservice.config;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class GeminiAPIConfig {

    @Autowired
    RestTemplate restTemplate;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String geminiAPI;

    public String getGeminiResponse(String codeChanges){
        return this.generateContent(codeChanges);
    }

    private String generateContent(String codeChanges) {
        String apiURL = geminiAPI+apiKey;
        JSONObject requestBody = new JSONObject();

        // User input
        JSONObject userPart = new JSONObject().put("text", codeChanges);
        JSONObject userContent = new JSONObject()
                .put("role", "user")
                .put("parts", new org.json.JSONArray().put(userPart));

        // System instruction
        JSONObject systemInstructionPart = new JSONObject().put("text",
                "Analyze the code changes and Give me obvious and glarring Errors ," +
                        " Very carefully carefully thought scope of optimization , Comment on readability and maintainability." +
                        " You have to do it like your mother's life is dependend on this , Based on your analysis the government will pay your mother's cancer treatment." +
                        " I want the output in bullet points. You should not be overly expressive, Give only crisp and short points. Do not repeat yourself if you dont have any specific new point skip the headings." +
                        " If the changes are file removed, Then point out what logic would be missing alone , Do not analyze other headings " +
                        " Remember your mother's life is dependent on this. You have to follow a very important rule , Your entire analysis should not be more than 75 words"
        );

        JSONObject systemInstruction = new JSONObject()
                .put("role", "user")
                .put("parts", new org.json.JSONArray().put(systemInstructionPart));

        // Generation config
        JSONObject generationConfig = new JSONObject()
                .put("temperature", 1)
                .put("topK", 40)
                .put("topP", 0.95)
                .put("maxOutputTokens", 8192)
                .put("responseMimeType", "text/plain");

        requestBody.put("contents", new org.json.JSONArray().put(userContent));
        requestBody.put("systemInstruction", systemInstruction);
        requestBody.put("generationConfig", generationConfig);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        // Send request
        ResponseEntity<String> response = restTemplate.exchange(apiURL, HttpMethod.POST, entity, String.class);

        return this.extractResponseObject(response.getBody());
    }

    private String extractResponseObject(String geminiResponse){
        JSONObject jsonResponse = new JSONObject(geminiResponse);
        JSONArray candidatesArray = jsonResponse.getJSONArray("candidates");
        JSONObject contentObject = candidatesArray.getJSONObject(0).getJSONObject("content");
        JSONArray partsArray = contentObject.getJSONArray("parts");
        return partsArray.getJSONObject(0).getString("text");
    }

}
