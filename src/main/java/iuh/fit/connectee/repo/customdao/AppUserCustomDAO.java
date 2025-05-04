package iuh.fit.connectee.repo.customdao;

import iuh.fit.connectee.model.AppUser;

import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 30/03/2025 - 7:06 PM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.repo.customdao
 */
public interface AppUserCustomDAO {
    AppUser findAppUserByUsername(String username);
    List<AppUser> findAppUserByRelativeNickname(String nickname);
    AppUser findAppUserByAbsoluteNickname(String nickname);
    AppUser findAppUserByAccId(String accId);
    List<String> findOnlineFriendsWithNickname(String nickname);
}
