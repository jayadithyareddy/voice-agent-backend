package DDL.LAB.Backend.chatbot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    @Value("${ANTHROPIC_API_KEY:}")
    private String anthropicApiKey;

    @Value("${CLAUDE_MODEL:claude-sonnet-4-5}")
    private String claudeModel;

    @Value("${CLAUDE_MAX_TOKENS:1024}")
    private int maxTokens;

    public ChatbotController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newHttpClient();
    }

    @PostMapping
    public ResponseEntity<?> chat(
            @RequestBody Map<String, Object> request) {

        try {
            String message = request.get("message") == null
                    ? ""
                    : request.get("message").toString().trim();

            if (message.isEmpty()) {
                return ResponseEntity.badRequest().body(
                        Map.of(
                                "success", false,
                                "message", "Message is required"
                        )
                );
            }

            if (anthropicApiKey == null || anthropicApiKey.isBlank()) {
                return ResponseEntity.internalServerError().body(
                        Map.of(
                                "success", false,
                                "message", "Claude API key is not configured"
                        )
                );
            }

            Map<String, Object> userContent = new HashMap<>();

            userContent.put(
                    "role",
                    "user"
            );

            userContent.put(
                    "content",
                    message
            );

            Map<String, Object> claudeRequest = new HashMap<>();

            claudeRequest.put(
                    "model",
                    claudeModel
            );

            claudeRequest.put(
                    "max_tokens",
                    maxTokens
            );

            claudeRequest.put(
                    "system",
                    "You are the DDL LAB AI Voice Agent assistant. " +
                    "Help users with customer management, calls, follow-ups, " +
                    "knowledge, notifications, and business questions. " +
                    "Be concise, professional, and helpful."
            );

            claudeRequest.put(
                    "messages",
                    new Object[]{userContent}
            );

            String requestBody =
                    objectMapper.writeValueAsString(claudeRequest);

            HttpRequest httpRequest =
                    HttpRequest.newBuilder()
                            .uri(
                                    URI.create(
                                            "https://api.anthropic.com/v1/messages"
                                    )
                            )
                            .header(
                                    HttpHeaders.CONTENT_TYPE,
                                    MediaType.APPLICATION_JSON_VALUE
                            )
                            .header(
                                    "x-api-key",
                                    anthropicApiKey
                            )
                            .header(
                                    "anthropic-version",
                                    "2023-06-01"
                            )
                            .POST(
                                    HttpRequest.BodyPublishers.ofString(
                                            requestBody
                                    )
                            )
                            .build();

            HttpResponse<String> response =
                    httpClient.send(
                            httpRequest,
                            HttpResponse.BodyHandlers.ofString()
                    );

            if (
                    response.statusCode() < 200
                    || response.statusCode() >= 300
            ) {

                return ResponseEntity
                        .status(response.statusCode())
                        .body(
                                Map.of(
                                        "success",
                                        false,
                                        "message",
                                        "Claude API request failed"
                                )
                        );
            }

            JsonNode root =
                    objectMapper.readTree(
                            response.body()
                    );

            JsonNode content =
                    root.path("content");

            StringBuilder replyBuilder =
                    new StringBuilder();

            if (content.isArray()) {

                for (JsonNode item : content) {

                    if (
                            "text".equals(
                                    item.path("type").asText()
                            )
                    ) {

                        replyBuilder.append(
                                item.path("text").asText()
                        );
                    }
                }
            }

            String reply =
                    replyBuilder.toString();

            if (reply.isBlank()) {
                reply =
                        "I couldn't generate a response.";
            }

            return ResponseEntity.ok(
                    Map.of(
                            "success",
                            true,
                            "message",
                            reply,
                            "reply",
                            reply,
                            "model",
                            claudeModel
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .internalServerError()
                    .body(
                            Map.of(
                                    "success",
                                    false,
                                    "message",
                                    "Chatbot service error"
                            )
                    );
        }
    }
}