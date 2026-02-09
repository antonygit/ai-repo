package com.example.demo.chat.controller;

import com.example.demo.chat.dto.ImageChatRequest;
import com.example.demo.chat.dto.TextChatRequest;
import com.example.demo.chat.util.ImageUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatClient chatClient;

    @Value("${spring.ai.openai.api-key:}")
    private String apiKey;

    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        if (apiKey == null || apiKey.isEmpty() || "your-api-key-here".equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("OpenAI API key is not configured. Please set the OPENAI_API_KEY environment variable.");
        }
        return ResponseEntity.ok("Chat service is ready!");
    }

    @PostMapping("/text")
    public ResponseEntity<String> textChat(@RequestBody TextChatRequest request) {
        // Validate API key first
        if (apiKey == null || apiKey.isEmpty() || "your-api-key-here".equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("OpenAI API key is not configured. Please set the OPENAI_API_KEY environment variable with your API key from https://platform.openai.com/account/api-keys");
        }

        try {
            String reply = this.chatClient.prompt()
                    .user(request.question())
                    .call()
                    .content();
            return ResponseEntity.ok(reply);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (errorMessage != null && errorMessage.contains("Incorrect API key")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid OpenAI API key. Please check your API key at https://platform.openai.com/account/api-keys");
            } else if (errorMessage != null && errorMessage.contains("quota")) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                        .body("OpenAI API quota exceeded. Please check your usage at https://platform.openai.com/usage");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing text chat: " + errorMessage);
        }
    }

    @PostMapping("/image")
    public ResponseEntity<String> imageChat(@RequestBody ImageChatRequest request) {
        // Validate API key first
        if (apiKey == null || apiKey.isEmpty() || "your-api-key-here".equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("OpenAI API key is not configured. Please set the OPENAI_API_KEY environment variable with your API key from https://platform.openai.com/account/api-keys");
        }

        try {
            // Validate image URL
            if (request.imageUrl() == null || request.imageUrl().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Image URL is required");
            }

            if (!ImageUtils.isValidImageUrl(request.imageUrl())) {
                return ResponseEntity.badRequest().body("Invalid image URL or unsupported image format. Supported formats: JPEG, PNG, GIF, WebP");
            }

            // Download image from URL and convert to base64 (required by OpenAI)
            String base64String = ImageUtils.convertUrlToBase64String(request.imageUrl());
            byte[] imageBytes = Base64.getDecoder().decode(base64String);
            ByteArrayResource imageResource = new ByteArrayResource(imageBytes);
            String mimeType = ImageUtils.getMimeTypeFromUrl(request.imageUrl());

            // Process the chat request with base64 image data
            String reply = this.chatClient.prompt()
                    .user(userSpec -> userSpec
                            .text(request.question())
                            .media(MimeTypeUtils.parseMimeType(mimeType), imageResource))
                    .call()
                    .content();

            return ResponseEntity.ok(reply);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error downloading or processing image from URL: " + e.getMessage());
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (errorMessage != null && errorMessage.contains("Incorrect API key")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid OpenAI API key. Please check your API key at https://platform.openai.com/account/api-keys");
            } else if (errorMessage != null && errorMessage.contains("quota")) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                        .body("OpenAI API quota exceeded. Please check your usage at https://platform.openai.com/usage");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing image chat: " + errorMessage);
        }
    }
}
