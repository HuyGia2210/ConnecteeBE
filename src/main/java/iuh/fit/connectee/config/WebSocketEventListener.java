package iuh.fit.connectee.config;

import iuh.fit.connectee.model.AppUser;
import iuh.fit.connectee.model.MessageType;
import iuh.fit.connectee.repo.customdao.AppUserCustomDAOImpl;
import iuh.fit.connectee.service.JwtService;
import iuh.fit.connectee.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;
    private final AppUserCustomDAOImpl appUserCustomDAOImpl;


//    @EventListener
//    public void handleConnectEvent(SessionConnectedEvent event) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
//        Principal userPrincipal = accessor.getUser();
//
//        if (userPrincipal != null) {
//            String username = userPrincipal.getName();
//            userService.connect(username);
//
//            List<Optional<AppUser>> friends = userService.findConnectedUsers(username);
//            for (Optional<AppUser> friend : friends) {
//                messagingTemplate.convertAndSendToUser(String.valueOf(friend), "/queue/status", username + " is online");
//            }
//        }
//    }

//    @EventListener
//    public void handleConnectEvent(SessionConnectedEvent event) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
//        Principal userPrincipal = accessor.getUser();
//
//        if (userPrincipal != null) {
//            String username = userPrincipal.getName(); // lấy từ JWT
//            userService.connect(username);
//
//            List<String> connectedFriends = userService.findConnectedUsernames(username);
//            for (String friendUsername : connectedFriends) {
//                messagingTemplate.convertAndSendToUser(friendUsername, "/queue/status", username + " is online");
//            }
//        }
//    }

    @EventListener
    public void handleConnectEvent(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = accessor.getUser();

        if (user == null) {
            System.out.println("No principal");
            return;
        }

        AppUser appUser = appUserCustomDAOImpl.findAppUserByUsername(user.getName()); // Giờ sẽ là k1pp1s
        String nickname = appUser.getNickname();
        System.out.println("nickname: " + nickname);
        userService.connect(nickname);

        List<String> friends = userService.findConnectedUsernames(nickname);
        for (String f : friends) {
            messagingTemplate.convertAndSendToUser(f, "/queue/status", nickname + " is online");
        }
    }



    @EventListener
    public void handleDisconnectEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = accessor.getUser();


        if (user == null) {
            System.out.println("No principal");
            return;
        }

        AppUser appUser = appUserCustomDAOImpl.findAppUserByUsername(user.getName());
        String nickname = appUser.getNickname();
        System.out.println("nickname: " + nickname);
        userService.disconnect(nickname);

        List<String> connectedFriends = userService.findConnectedUsernames(nickname);
        for (String friendUsername : connectedFriends) {
            messagingTemplate.convertAndSendToUser(friendUsername, "/queue/status", nickname + " is offline");
        }
    }

}
