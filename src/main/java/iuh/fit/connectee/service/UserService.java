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
//    Role saveRole (Role role);

    AppUser saveAppUser(AppUser appUser);

    /// /////////////////////////
//    void addRoleToUser(String username, String roleName);
    String verify (String username, String password);

    String verifyWithoutAuth(String username);

    /// /////////////////////////
    void deleteById(String theId);
    void disconnect (String username);
    List<Optional<AppUser>> findConnectedUsers(String username);
}
