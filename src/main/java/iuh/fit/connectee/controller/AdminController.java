package iuh.fit.connectee.controller;

import iuh.fit.connectee.model.Admin;
import iuh.fit.connectee.repo.AdminRepository;
import iuh.fit.connectee.utils.AESUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Le Tran Gia Huy
 * @created 04/05/2025 - 8:31 PM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.controller
 */

@RestController @RequiredArgsConstructor @RequestMapping("/api/admin")
public class AdminController {
    private final AdminRepository adminRepository;

    @GetMapping("/add-admin")
    public ResponseEntity<Boolean> addAdmin(@RequestParam String username, @RequestParam String password) {
        try {
            Admin admin = new Admin();
            admin.setUsername(username);
            admin.setPassword(AESUtil.encrypt(password));
            adminRepository.save(admin);
            return ResponseEntity.ok(true);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.ok(false); // Username đã tồn tại
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Lỗi khác
        }
    }


    @GetMapping("/check-valid-admin-acc")
    public ResponseEntity<Boolean> checkValid(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok(adminRepository.checkValidAdminAcc(username, password));
    }
}
