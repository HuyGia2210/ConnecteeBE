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

import java.util.Map;

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

    private final JwtService jwtService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {

        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpServletRequest = servletRequest.getServletRequest();
            Cookie[] cookies = httpServletRequest.getCookies();

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("jwt".equals(cookie.getName())) {
                        String token = cookie.getValue();
                        if (jwtService.validateTokenWithoutUserDetail(token)) {
                            String username = jwtService.extractUsername(token);
                            attributes.put("username", username);
                            return true;
                        }
                    }
                }
            }else{
                log.warn("WebSocket handshake rejected: invalid or missing JWT token");
            }
        }

        return false; // không cho phép kết nối nếu không có token hợp lệ
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // không cần xử lý gì ở đây
    }
}

