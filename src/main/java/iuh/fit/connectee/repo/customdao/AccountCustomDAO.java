package iuh.fit.connectee.repo.customdao;

import iuh.fit.connectee.model.Account;

/**
 * @author Le Tran Gia Huy
 * @created 14/02/2025 - 2:25 PM
 * @project gslendarBK
 * @package gslendar.gslendarbk.repository.customdao
 */
public interface AccountCustomDAO {

    Account findByUsername(String username);

//    Optional<AppUser> findByVerificationCode(String verificationCode);
}
