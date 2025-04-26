package iuh.fit.connectee.repo.customdao;

import iuh.fit.connectee.model.AppUser;

/**
 * @author Le Tran Gia Huy
 * @created 30/03/2025 - 7:06 PM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.repo.customdao
 */
public interface AppUserCustomDAO {
    AppUser findAppUserByUsername(String username);

    AppUser findAppUserByAccId(String accId);
}
