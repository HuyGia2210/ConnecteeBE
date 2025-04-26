package iuh.fit.connectee.service;

import iuh.fit.connectee.model.Account;
import iuh.fit.connectee.model.AppUser;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<Account> findAll();
//    AppUser findUser(String username);
//    AppUser findById(int theId);
/// /////////////////////////
    Account saveAccount(Account appUser);
    AppUser saveAppUser(AppUser appUser);

    /// /////////////////////////
    String verify (String username, String password);
    String verifyWithoutAuth(String username);

    /// /////////////////////////
    void deleteById(String theId);
    void disconnect (String username);
    void connect (String username);
    boolean isOnline (String username);

    List<String> findConnectedUsernames(String username);
    List<String> findFriendByUsernames(String username);
    List<AppUser> findAppUserFriendByUsernames(String username);
}
