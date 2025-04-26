package iuh.fit.connectee.config;

import iuh.fit.connectee.service.JwtService;
import iuh.fit.connectee.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author Le Tran Gia Huy
 * @created 03/03/2025 - 8:09 PM
 * @project gslendarBK
 * @package gslendar.gslendarbk.config
 */

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        /**
         * Dòng code ở đây kiểm tra cookie có chứa jwt token không, nếu có thì lấy ra token và username.
         * Khác với dòng code trên khi chúng ta chỉ sử dụng JWT Token, dòng code dưới đây sẽ sẽ lấy token từ
         * cookie và kiểm tra xem token có hợp lệ không, nếu hợp lệ thì sẽ set thông tin user vào SecurityContextHolder.
         */
        // Bearer token
        String token = null;
        String username = null;

        String path = request.getRequestURI();
        if (path.contains("/api/user/register") || path.contains("/api/user/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }else{
            System.out.println("Cookie is null");
        }

        if(token != null){
            username = jwtService.extractUsername(token);
        }else{
            System.out.println("Token is null");
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = applicationContext.getBean(UserDetailsServiceImpl.class).loadUserByUsername(username);

            if(jwtService.validateToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken authToken = new
                        UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authToken.
                        setDetails(new WebAuthenticationDetailsSource().
                                buildDetails(request));

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authToken);
            }
        }else{
            System.out.println("Username is null");
        }

        filterChain.doFilter(request, response);
    }
}
