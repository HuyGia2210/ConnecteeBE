package iuh.fit.connectee.controller;

import iuh.fit.connectee.model.Message;
import iuh.fit.connectee.model.misc.Gender;
import iuh.fit.connectee.repo.AccountRepository;
import iuh.fit.connectee.repo.AppUserRepository;
import iuh.fit.connectee.repo.MessageRepository;
import iuh.fit.connectee.service.JwtService;
import iuh.fit.connectee.service.UserService;
import iuh.fit.connectee.utils.AESUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Le Tran Gia Huy
 * @created 01/05/2025 - 2:06 PM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.controller
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatMessageController {
    private final AccountRepository accountRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final AppUserRepository appUserRepository;
    private final MessageRepository messageRepository;
    private static final String SECRET = System.getenv("ENCRYPTION_KEY");

    @PostMapping("/get-chat")
    public ResponseEntity<?> getChat(@RequestBody Credentials input, HttpServletRequest request) {
        // 1️⃣ Lấy JWT token từ cookie
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


        String username1 = accountRepository.findById(appUserRepository.findById(input.user1).get().getAccId()).get().getUsername();
        String username2 = accountRepository.findById(appUserRepository.findById(input.user2).get().getAccId()).get().getUsername();


        List<Message> returnMessages = messageRepository
                .findChatBetween(username1, username2)
                .stream()
                .peek(x -> {
                    x.setSender(appUserRepository.findAppUserByAccId(accountRepository.findByUsername(x.getSender()).getAccId()).getNickname());
                    x.setReceiver(appUserRepository.findAppUserByAccId(accountRepository.findByUsername(x.getReceiver()).getAccId()).getNickname());
                    x.setContent(AESUtil.decrypt(x.getContent()));
                })
                .collect(Collectors.toList());


        return ResponseEntity.ok(returnMessages);
    }


    @Data
    static class Credentials {
        private String user1;
        private String user2;
    }
}
