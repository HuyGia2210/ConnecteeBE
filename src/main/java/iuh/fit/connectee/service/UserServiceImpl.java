package iuh.fit.connectee.service;

import iuh.fit.connectee.model.Account;
import iuh.fit.connectee.model.AppUser;
import iuh.fit.connectee.model.misc.Status;
import iuh.fit.connectee.repo.AccountRepository;
import iuh.fit.connectee.repo.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Le Tran Gia Huy
 * @created 06/02/2025 - 5:13 PM
 * @project gslendarBK
 * @package gslendar.gslendarbk.service
 */


@Service
@RequiredArgsConstructor @Transactional @Slf4j
public class UserServiceImpl implements UserService{

    private AccountRepository accountRepository;
    private AppUserRepository appUserRepository;
    private AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private JwtService jwtService;
    private final Set<String> onlineUsers = ConcurrentHashMap.newKeySet();


    @Autowired
    public UserServiceImpl(
            AccountRepository theAccountRepository,
            AuthenticationManager theAuthenticationManager,
            JwtService theJwtService,
            AppUserRepository theAppUserRepository
    ) {
        accountRepository = theAccountRepository;
        authenticationManager = theAuthenticationManager;
        jwtService = theJwtService;
        appUserRepository = theAppUserRepository;
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account saveAccount(Account account) {
        account.setPassword(encoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }

    @Override
    public AppUser saveAppUser(AppUser appUser) {
        log.info("Saving user: " + appUser);
        return appUserRepository.save(appUser);
    }

    public void disconnect (String username) {
        var account = accountRepository.findByUsername(username);
        onlineUsers.remove(username);
        if(account != null){
            account.setStatus(Status.OFFLINE);
            accountRepository.save(account);
        }
    }

    public void connect (String username) {
        var account = accountRepository.findByUsername(username);
        onlineUsers.add(username);
        if(account != null){
            account.setStatus(Status.ONLINE);
            accountRepository.save(account);
        }
    }

    public boolean isOnline(String username) {
        return onlineUsers.contains(username);
    }

    public List<AppUser> findAppUserFriendByUsernames(String username) {
        AppUser appUser = appUserRepository.findAppUserByUsername(username);
        if (appUser == null) return List.of();

        List<String> friendNicknames = appUser.getFriendNickNames();
        return appUserRepository.findAllById(friendNicknames);
    }


    public List<String> findFriendByUsernames(String username) {
        // 1. Tìm AppUser từ username → accId → AppUser
        AppUser appUser = appUserRepository.findAppUserByUsername(username);
        if (appUser == null) return List.of();

        List<String> friendNicknames = appUser.getFriendNickNames();
        List<String> friendUsernames = new ArrayList<>();

        for (String nickname : friendNicknames) {
            Optional<AppUser> friendOpt = appUserRepository.findById(nickname);

            if (friendOpt.isPresent()) {
                String accId = friendOpt.get().getAccId();
                Optional<Account> accountOpt = accountRepository.findById(accId);

                accountOpt.ifPresent(account ->
                        friendUsernames.add(account.getUsername())
                );
            }
        }

        return friendUsernames;
    }


    public List<String> findConnectedUsernames(String username) {
        // 1. Tìm AppUser qua username (qua accId)
        AppUser appUser = appUserRepository.findAppUserByUsername(username);
        if (appUser == null) return List.of();

        List<String> friendNicknames = appUser.getFriendNickNames();
        List<String> connectedUsernames = new ArrayList<>();

        // 2. Duyệt từng nickname bạn bè
        for (String nickname : friendNicknames) {
            Optional<AppUser> friendOpt = appUserRepository.findById(nickname);

            if (friendOpt.isPresent()) {
                String accId = friendOpt.get().getAccId();

                // Tìm Account qua accId dùng findById()
                Optional<Account> accountOpt = accountRepository.findById(accId);

                if (accountOpt.isPresent()) {
                    String friendUsername = accountOpt.get().getUsername();

                    if (isOnline(friendUsername)) {
                        connectedUsernames.add(friendUsername);
                    }
                }
            }
        }

        return connectedUsernames;
    }

    @Override
    public String verify(String username, String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        if (auth.isAuthenticated()) {
            log.info("User authenticated");
            return jwtService.generateToken(username);
        } else {
            log.error("User not authenticated");
            return "400";
        }
    }

    @Override
    public String verifyWithoutAuth(String username) {
        return jwtService.generateToken(username);
    }


    @Override
    public void deleteById(String theId) {
        accountRepository.deleteById(theId);
    }
}