package iuh.fit.connectee.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * @author Le Tran Gia Huy
 * @created 02/05/2025 - 1:06 PM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.service
 */
@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateContent(String prompt) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + geminiApiKey;

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(Map.of("text", prompt)))
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

        // Lấy nội dung từ response
        if (response.getStatusCode().is2xxSuccessful()) {
            Map content = (Map) ((List) response.getBody().get("candidates")).get(0);
            Map part = (Map) ((List) ((Map) content.get("content")).get("parts")).get(0);
            return (String) part.get("text");
        }

        return "Lỗi khi gọi Gemini API";
    }
}

