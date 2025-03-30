package iuh.fit.connectee.config;

import iuh.fit.connectee.model.MessageType;
import iuh.fit.connectee.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * @author Le Tran Gia Huy
 * @created 29/03/2025 - 11:51 AM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.config
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;
    private final JwtService jwtService;

//    @EventListener
//    public void handelDisconnectEvent(
//            SessionDisconnectEvent disconnectEvent
//    ) {
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(disconnectEvent.getMessage());
//        String username = (String) headerAccessor.getSessionAttributes().get("username");
//
//        if(username != null) {
//            log.info("AppUser Disconnected: " + username);
//            var chatMessage = ChatMessage.builder()
//                    .type(MessageType.LEAVE)
//                    .sender(username)
//                    .build();
//            messagingTemplate.convertAndSend("/topic/public", chatMessage);
//        }
//    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        HttpServletRequest request = ((ServletServerHttpRequest) headerAccessor.getSessionAttributes().get("HTTP_REQUEST")).getServletRequest();

        Cookie[] cookies = request.getCookies();
        String jwtToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                    break;
                }
            }
        }

        if (jwtToken != null) {
            String username = jwtService.extractUsername(jwtToken);
            System.out.println("User connected: " + username);
        }
    }

}
