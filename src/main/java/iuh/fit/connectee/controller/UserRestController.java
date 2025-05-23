package iuh.fit.connectee.controller;

import iuh.fit.connectee.model.Account;
import iuh.fit.connectee.model.AppUser;
import iuh.fit.connectee.model.Setting;
import iuh.fit.connectee.model.misc.Gender;
import iuh.fit.connectee.model.misc.Status;
import iuh.fit.connectee.repo.AccountRepository;
import iuh.fit.connectee.repo.AppUserRepository;
import iuh.fit.connectee.service.JwtService;
import iuh.fit.connectee.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Le Tran Gia Huy
 * @created 14/02/2025 - 5:27 PM
 * @project gslendarBK
 * @package gslendar.gslendarbk.controller
 */

@RestController @RequiredArgsConstructor @RequestMapping("/api/user")
public class UserRestController {
    private final AccountRepository accountRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final AppUserRepository appUserRepository;

    @GetMapping("/test")
    public ResponseEntity<?> test(@RequestParam("nickname") String nickname) {
        return ResponseEntity.ok(userService.findFriends(nickname).getFirst()) ;
    }

    @GetMapping("/friend-list")
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

        return ResponseEntity.ok(userService.findAppUserFriendByUsernames(username));
    }


    @GetMapping("/find-friend")
    public ResponseEntity<?> findFriend(@RequestParam("input") String input, HttpServletRequest request) {
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

        return ResponseEntity.ok(userService.findFriends(input));
    }

    @PostMapping("/send-friend-request")
    public ResponseEntity<?> sendFriendRequest(@RequestParam("nickname") String nickname, HttpServletRequest request) {
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

        AppUser friend = userService.findFriends(nickname).getFirst();
        String accID = accountRepository.findByUsername(username).getAccId();
        AppUser appUser = userService.findAppUser(accID);
        friend.setFriendRequests(Collections.singleton(appUser.getNickname()));
        appUserRepository.save(friend);

        return ResponseEntity.ok(true);
    }

    @GetMapping("/get-friend-request")
    public ResponseEntity<?> getFriendRequest(HttpServletRequest request) {
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

        AppUser appUser = userService.findAppUser(accountRepository.findByUsername(username).getAccId());
        List<AppUser> friendRequests = new ArrayList<>();
        for (String friendRequest : appUser.getFriendRequests()) {
            AppUser friend = userService.findFriends(friendRequest).getFirst();
            friendRequests.add(friend);
        }

        return ResponseEntity.ok(friendRequests);
    }

    @GetMapping("/accept-friend-request")
    public ResponseEntity<?> acceptFriendRequest(@RequestParam("nickname") String nickName, HttpServletRequest request) {
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

        AppUser appUser = appUserRepository
                .findAppUserByUsername(username);

        String friendRequestsNickname = appUser
                .getFriendRequests()
                .stream().filter(x-> x.equals(nickName))
                .findFirst().orElse(null);

        appUser.getFriendList().add(friendRequestsNickname);
        appUser.getFriendRequests().remove(friendRequestsNickname);
        appUserRepository.save(appUser);

        AppUser friend = userService.findFriends(friendRequestsNickname).getFirst();
        friend.getFriendList().add(appUser.getNickname());
        appUserRepository.save(friend);


        return ResponseEntity.ok(true);
    }

    @GetMapping("/check-valid-username")
    public ResponseEntity<?> checkValidUsername(@RequestParam("username") String username) {
        boolean isExist = userService.isUserExist(username);
        return ResponseEntity.ok(isExist);
    }

    @GetMapping("/check-valid-nickname")
    public ResponseEntity<?> checkValidNickname(@RequestParam("nickname") String nickname) {
        boolean isExist = userService.isNicknameExist(nickname);
        return ResponseEntity.ok(isExist);
    }


    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody RegisterRequest requestForm, HttpServletResponse response) {
        Account tempAcc = new Account();
        tempAcc.setUsername(requestForm.username);
        tempAcc.setPassword(requestForm.password);
        tempAcc.setStatus(Status.ONLINE);
        Account createdAcc = userService.saveAccount(tempAcc);

        AppUser tempUser = new AppUser();
        tempUser.setNickname(requestForm.nickName);
        tempUser.setFullName(requestForm.fullName);
        tempUser.setEmail(requestForm.email);
        tempUser.setAccId(createdAcc.getAccId());
        tempUser.setGender(requestForm.gender);
        tempUser.setDob(requestForm.dob);
        AppUser savedAppUser = userService.saveAppUser(tempUser);

        String token  = userService.verifyWithoutAuth(createdAcc.getUsername());
        // Tạo HTTP-only cookie
        Cookie cookie = new Cookie("jwt", token);

        final ResponseCookie responseCookie = ResponseCookie
                .from("jwt", token)
                .secure(true)
                .httpOnly(true)
                .path("/")
                .maxAge(10 * 24 * 60 * 60)
                .sameSite("None")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

        return ResponseEntity.ok(token);

    }

    @GetMapping("/auth/check")
    @CrossOrigin(origins = {"http://localhost:5173", "https://connectee-beta.vercel.app"}, allowCredentials = "true")

    public ResponseEntity<?> checkAuth(HttpServletRequest request) {
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
        if (token != null && jwtService.validateTokenWithoutUserDetail(token)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


    @GetMapping("/")
    @CrossOrigin(origins = {"http://localhost:5173", "https://connectee-beta.vercel.app"})

    public String greet(HttpServletRequest request) {
        return "Hello, " + request.getSession().getId();
    }

    @GetMapping("/csrf-token")
    @CrossOrigin(origins = {"http://localhost:5173", "https://connectee-beta.vercel.app"})

    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        String token  = userService.verify(request.getUsername(), request.getPassword());
        final ResponseCookie responseCookie = ResponseCookie
                .from("jwt", token)
                .secure(true)
                .httpOnly(true)
                .path("/")
                .maxAge(10 * 24 * 60 * 60)
                .sameSite("None")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

        return ResponseEntity.ok(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        final ResponseCookie responseCookie = ResponseCookie
                .from("jwt", "")
                .secure(true)
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/get-nickname")
    public ResponseEntity<?> getNickname(HttpServletRequest request) {
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

        AppUser appUser = userService.findAppUser(accountRepository.findByUsername(username).getAccId());
        return ResponseEntity.ok(appUser.getNickname());
    }

    @GetMapping("/get-nickname-by-username")
    public ResponseEntity<?> getNicknameByUsername(@RequestParam("username") String username) {
        String accId = accountRepository.findByUsername(username).getAccId();

        AppUser appUser = userService.findAppUser(accId);

        return ResponseEntity.ok(appUser.getNickname());
    }

    @GetMapping("/get-username-by-nickname")
    public ResponseEntity<?> getUsernameByNickname(@RequestParam("nickname") String nickname) {
        AppUser appUser = appUserRepository.findById(nickname).isPresent()?appUserRepository.findById(nickname).get():null;

        if(appUser==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng");
        }
        String accId = appUser.getAccId();
        Account account = accountRepository.findById(accId).orElse(null);
        if(account==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy tài khoản");
        }
        return ResponseEntity.ok(account.getUsername());
    }

    @GetMapping("/get-fullName-by-nickname")
    public ResponseEntity<?> getFullNameByNickname(@RequestParam("nickname") String nickname) {
        AppUser appUser = appUserRepository.findById(nickname).isPresent()?appUserRepository.findById(nickname).get():null;


        if(appUser==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng");
        }
        return ResponseEntity.ok(appUser.getFullName());
    }

    @GetMapping("/get-appUser-by-nickname")
    public ResponseEntity<?> getAppUserByNickname(@RequestParam("nickname") String nickname, HttpServletRequest request) {
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

        AppUser appUser = appUserRepository.findById(nickname).isPresent()?appUserRepository.findById(nickname).get():null;

        if(appUser==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng");
        }
        return ResponseEntity.ok(appUser);
    }

    @GetMapping("/get-online-friends-by-nickname")
    public ResponseEntity<?> getOnlineFriendsByNickname(@RequestParam("nickname") String nickname, HttpServletRequest request) {
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

        AppUser appUser = appUserRepository.findById(nickname).isPresent()?appUserRepository.findById(nickname).get():null;
        if(appUser==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng");
        }

        return ResponseEntity.ok(appUserRepository.findOnlineFriendsWithNickname(nickname));
    }

    @GetMapping("/get-settings")
    public ResponseEntity<?> getSettings(HttpServletRequest request) {
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

        Setting setting = accountRepository.findById(accountRepository.findByUsername(username).getAccId()).get().getSetting();
        return ResponseEntity.ok(setting);
    }

    @PostMapping("/update-settings")
    public ResponseEntity<?> updateSettings(@RequestBody Setting setting, HttpServletRequest request) {
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
        account.setSetting(setting);
        accountRepository.save(account);

        return ResponseEntity.ok(true);
    }

    @PostMapping("/update-information")
    public ResponseEntity<?> updateInformation(@RequestBody ChangingInfomationForm info, HttpServletRequest request){
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

        appUser.setFullName(info.getFullName());
        appUser.setEmail(info.getEmail());
        appUser.setDob(info.getDob());
        appUser.setGender(info.getGender());

        appUserRepository.save(appUser);
        return ResponseEntity.ok(true);
    }



    @Data
    static class RegisterRequest {
        private String username;
        private String fullName;
        private String nickName;
        private LocalDate dob;
        private Gender gender;
        private String email;
        private String password;
    }

    @Data
    static class ChangingInfomationForm{
        private String fullName;
        private LocalDate dob;
        private Gender gender;
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

@Data
@AllArgsConstructor
@Getter
@Setter
class FriendRequestDTO {
    private String nickName;
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
