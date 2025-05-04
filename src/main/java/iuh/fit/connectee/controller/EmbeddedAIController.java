package iuh.fit.connectee.controller;

import iuh.fit.connectee.model.AppUser;
import iuh.fit.connectee.model.EmbeddedAI;
import iuh.fit.connectee.repo.AppUserRepository;
import iuh.fit.connectee.repo.EmbeddedAIRepository;
import iuh.fit.connectee.service.GeminiService;
import iuh.fit.connectee.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Le Tran Gia Huy
 * @created 02/05/2025 - 12:53 PM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.controller
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class EmbeddedAIController {

    @Autowired
    private GeminiService geminiService;
    private final JwtService jwtService;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private EmbeddedAIRepository embeddedAIRepository;



    @GetMapping("/generate")
    public ResponseEntity<?> generate(@RequestParam String prompt, HttpServletRequest request) {
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        // 2️⃣ Kiểm tra token có tồn tại không
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Không có JWT token");
        }

        // 3️⃣ Giải mã JWT token để lấy username
        String username;
        try {
            username = jwtService.extractUsername(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT không hợp lệ");
        }

        String nickname = appUserRepository.findAppUserByUsername(username).getNickname();
        String result = geminiService.generateContent(prompt);
        EmbeddedAI embeddedAI = new EmbeddedAI(nickname, prompt, result);
        embeddedAIRepository.save(embeddedAI);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/get-history")
    public ResponseEntity<?> getHistory(HttpServletRequest request) {
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        // 2️⃣ Kiểm tra token có tồn tại không
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Không có JWT token");
        }

        // 3️⃣ Giải mã JWT token để lấy username
        String username;
        try {
            username = jwtService.extractUsername(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT không hợp lệ");
        }

        AppUser appUser = appUserRepository.findAppUserByUsername(username);
        return ResponseEntity.ok(embeddedAIRepository.findAllEmbeddedAIByNickname(appUser.getNickname()));
    }
}
