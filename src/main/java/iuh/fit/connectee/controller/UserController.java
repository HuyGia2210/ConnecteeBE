package iuh.fit.connectee.controller;

import iuh.fit.connectee.model.AppUser;
import iuh.fit.connectee.model.Message;
import iuh.fit.connectee.repo.AppUserRepository;
import iuh.fit.connectee.repo.MessageRepository;
import iuh.fit.connectee.service.JwtService;
import iuh.fit.connectee.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

/**
 * @author Le Tran Gia Huy
 * @created 30/03/2025 - 7:19 PM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.controller
 */

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AppUserRepository appUserRepository;
    private final JwtService jwtService;
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage handleChat(@Payload ChatMessage chatMessage) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        chatMessage.setSender(username);
        return chatMessage;
    }

    @MessageMapping("/chat.private")
    public void handlePrivateChat(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // Lấy username từ SecurityContextHolder (JWT)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sender = authentication.getName();

        // Lưu tin nhắn vào database
        Message message = new Message(sender, chatMessage.getReceiver(), chatMessage.getContent());
        messageRepository.save(message);

        // Gửi tin nhắn đến người nhận qua WebSocket
        messagingTemplate.convertAndSendToUser(chatMessage.getReceiver(), "/queue/messages", chatMessage);
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo({"/user/topic", "/queue/messages"})
    public String disconnect() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        userService.disconnect(username);
        return username;
    }

    @Data
    private static class ChatMessage{
        private String sender;
        private String receiver;
        private String content;
    }
}
