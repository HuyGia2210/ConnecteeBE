package iuh.fit.connectee.config;

import iuh.fit.connectee.model.AppUser;
import iuh.fit.connectee.repo.AppUserRepository;
import iuh.fit.connectee.service.JwtService;
import iuh.fit.connectee.service.UserDetailsServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Le Tran Gia Huy
 * @created 11/04/2025 - 10:57 AM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.config
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String query = request.getURI().getQuery(); // nickname=...

        if (query != null) {
            Map<String,String> params = Arrays.stream(query.split("&"))
                    .map(s -> s.split("=",2))
                    .filter(a -> a.length==2)
                    .collect(Collectors.toMap(a->a[0], a->a[1]));

            String nickname = params.get("nickname");
            if (nickname != null && !nickname.isBlank()) {
                System.out.println("Đã nhận nickname: " + nickname);
                attributes.put("nickname", nickname);
                System.out.println("Nickname trong attributes: " + attributes.get("nickname"));
                return true;
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {}
}

