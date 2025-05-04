package iuh.fit.connectee.controller;

import iuh.fit.connectee.model.AppUser;
import iuh.fit.connectee.model.misc.Status;
import iuh.fit.connectee.repo.AccountRepository;
import iuh.fit.connectee.repo.AppUserRepository;
import iuh.fit.connectee.repo.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 04/05/2025 - 4:53 PM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.controller
 */

@RestController @RequiredArgsConstructor @RequestMapping("/api/stat")
public class StatisticController {
    private final AppUserRepository appUserRepository;
    private final AccountRepository accountRepository;
    private final MessageRepository messageRepository;

    @GetMapping("/get-number-of-app-user")
    public ResponseEntity<Long> getNumberOfAppUser() {
        return ResponseEntity.ok(appUserRepository.count());
    }

    @GetMapping("/get-number-of-messages")
    public ResponseEntity<Long> getNumberOfMessage() {
        return ResponseEntity.ok(messageRepository.count());
    }

    @GetMapping("/get-number-of-online-user")
    public ResponseEntity<?> getNumberOfOnlineUser() {
        long onlineUserCount = accountRepository.findAll().stream()
                .filter(acc -> acc.getStatus() == Status.ONLINE)
                .count();
        return ResponseEntity.ok(onlineUserCount);
    }

    @GetMapping("/get-all-app-user")
    public ResponseEntity<List<AppUser>> getAllAppUser() {
        return ResponseEntity.ok(appUserRepository.findAll());
    }
}
