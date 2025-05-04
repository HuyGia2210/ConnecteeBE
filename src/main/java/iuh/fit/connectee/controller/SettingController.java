package iuh.fit.connectee.controller;

import iuh.fit.connectee.model.Account;
import iuh.fit.connectee.repo.AccountRepository;
import iuh.fit.connectee.repo.AppUserRepository;
import iuh.fit.connectee.repo.MessageRepository;
import iuh.fit.connectee.service.JwtService;
import iuh.fit.connectee.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Le Tran Gia Huy
 * @created 02/05/2025 - 11:57 AM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.controller
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/setting")
public class SettingController {
    private final AccountRepository accountRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final AppUserRepository appUserRepository;
    private final MessageRepository messageRepository;

    @GetMapping("/get-setting")
    public ResponseEntity<?> getSetting(HttpServletRequest request) {
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

        Account account = accountRepository.findByUsername(username);
        return ResponseEntity.ok(account.getSetting());
    }
}
