package iuh.fit.connectee.service;

import iuh.fit.connectee.model.Account;
import iuh.fit.connectee.model.AppUser;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<Account> findAll();
    AppUser findAppUser(String accID);
//    AppUser findAppUserByAbsoluteNickname(String nickname);
    AppUser findAppUserByNickname(String nickname);
//    AppUser findById(int theId);
    boolean isUserExist(String username);
    boolean isNicknameExist(String nickname);
/// /////////////////////////
    Account saveAccount(Account appUser);
    AppUser saveAppUser(AppUser appUser);

    /// /////////////////////////
    String verify (String username, String password);
    String verifyWithoutAuth(String accId);

    /// /////////////////////////
    void deleteById(String theId);
    void disconnect (String nickname);
    void connect (String nickname);
    boolean isOnline (String username);

    List<String> findConnectedUsernames(String username);
    List<String> findFriendByUsernames(String username);
    List<AppUser> findAppUserFriendByUsernames(String username);
    List<AppUser> findFriends(String nickname);

}
