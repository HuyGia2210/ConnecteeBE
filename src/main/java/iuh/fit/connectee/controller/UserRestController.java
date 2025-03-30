package iuh.fit.connectee.controller;

import iuh.fit.connectee.model.Account;
import iuh.fit.connectee.model.AppUser;
import iuh.fit.connectee.model.misc.Status;
import iuh.fit.connectee.repo.AccountRepository;
import iuh.fit.connectee.service.JwtService;
import iuh.fit.connectee.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Le Tran Gia Huy
 * @created 14/02/2025 - 5:27 PM
 * @project gslendarBK
 * @package gslendar.gslendarbk.controller
 */

@Controller @RestController @RequiredArgsConstructor @RequestMapping("/api/user")
public class UserRestController {
    private final AccountRepository accountRepository;
    private final UserService userService;
    private final JwtService jwtService;

    @GetMapping("/connected-users")
    public ResponseEntity<?> findConnectedUser(HttpServletRequest request) {
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

        // 4️⃣ Tìm danh sách user đang kết nối
        List<AppUser> connectedUsers = userService.findConnectedUsers(username)
                .stream()
                .flatMap(Optional::stream) // Tránh lỗi nếu Optional rỗng
                .toList();
        return ResponseEntity.ok(connectedUsers);
    }


    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody RegisterRequest requestForm, HttpServletResponse response) {
//        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/register").toUriString());
        Account tempAcc = new Account();
        tempAcc.setUsername(requestForm.username);
        tempAcc.setPassword(requestForm.password);
        tempAcc.setStatus(Status.ONLINE);
        Account createdAcc = userService.saveAccount(tempAcc);

        AppUser tempUser = new AppUser();
        tempUser.setNickName(requestForm.nickName);
        tempUser.setFullName(requestForm.fullName);
        tempUser.setEmail(requestForm.email);
        tempUser.setAccId(createdAcc.getAccId());
        AppUser savedAppUser = userService.saveAppUser(tempUser);

        String token  = userService.verifyWithoutAuth(createdAcc.getUsername());
        // Tạo HTTP-only cookie
        Cookie cookie = new Cookie("jwt", token);

        final ResponseCookie responseCookie = ResponseCookie
                .from("jwtAdvanced", token)
                .secure(true)
                .httpOnly(true)
                .path("/")
                .maxAge(10 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

        /**
         * Đoạn code sau đây không chạy, chỉ để lại để tham khảo sau này
         */

        if(false){
            cookie.setHttpOnly(true);
            cookie.setSecure(true); // Chỉ gửi trên HTTPS
            cookie.setPath("/"); // Cookie áp dụng cho toàn bộ trang web
            cookie.setMaxAge(10 * 24 * 60 * 60); // 10 ngày

            // Đính kèm cookie vào response
            response.setHeader("Set-Cookie", "key=value; HttpOnly; SameSite=strict");
        }
        /// //////////////////////////////////////////////////////////////////////////////////////

        return ResponseEntity.ok(token);

    }

//    @PostMapping("/role/save")
//    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
//        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/role/save").toUriString());
//        return ResponseEntity.created(uri).body(userService.saveRole(role));
//    }

//    @PostMapping("/role/saveRoleToUser")
//    public ResponseEntity<Role> saveRoleToUser(@RequestBody RoleToUserForm form) {
//        userService.addRoleToUser(form.getUsername(), form.getRoleName());
//        return ResponseEntity.ok().build();
//    }

    @GetMapping("/")
    @CrossOrigin(origins = "http://localhost:5173")
    public String greet(HttpServletRequest request) {
        return "Hello, " + request.getSession().getId();
    }

    @GetMapping("/csrf-token")
    @CrossOrigin(origins = "http://localhost:5173")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {

        String token  = userService.verify(request.getUsername(), request.getPassword());
        // Tạo HTTP-only cookie
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // Chỉ gửi trên HTTPS
        cookie.setPath("/"); // Cookie áp dụng cho toàn bộ trang web
        cookie.setMaxAge(10 * 24 * 60 * 60); // 10 ngày

        // Đính kèm cookie vào response
        response.addCookie(cookie);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // Xóa cookie ngay lập tức

        response.addCookie(cookie);
        return ResponseEntity.ok("Logged out successfully");
    }
    @Data
    class RegisterRequest {
        private String nickName;
        private String fullName;
        private String username;
        private String password;
        private String email;
    }
}


@Data
class RoleToUserForm{
    private String username;
    private String roleName;
}

@Data
class LoginRequest {
    private String username;
    private String password;
}

@Data
@AllArgsConstructor
class LoginResponse {
    private Cookie cookie;
}
//
//
//authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
//
//AppUser appUser = appUserRepository.findByUsername(request.getUsername())
//        .orElseThrow(() -> new RuntimeException("AppUser not found"));
//
//String token = jwtUtil.generateToken(appUser.getUsername());
//
//Cookie cookie = new Cookie("token", token);
//        cookie.setHttpOnly(true);
//        cookie.setSecure(true); // Chỉ dùng khi HTTPS
//        cookie.setPath("/");    // Có hiệu lực trên toàn bộ domain
//        cookie.setMaxAge(24 * 60 * 60); // Hết hạn sau 1 ngày
//
//        response.addCookie(cookie);
//
//
//        return ResponseEntity.ok(new LoginResponse(token));
