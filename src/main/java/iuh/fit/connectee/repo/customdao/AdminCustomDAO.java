package iuh.fit.connectee.repo.customdao;

import iuh.fit.connectee.model.Admin;

/**
 * @author Le Tran Gia Huy
 * @created 04/05/2025 - 8:20 PM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.repo.customdao
 */
public interface AdminCustomDAO {
    boolean checkValidAdminAcc(String username, String password);
}

