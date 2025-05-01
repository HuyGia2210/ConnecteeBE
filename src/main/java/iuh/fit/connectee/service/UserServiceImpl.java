package iuh.fit.connectee.service;

import iuh.fit.connectee.model.Account;
import iuh.fit.connectee.model.AppUser;
import iuh.fit.connectee.model.misc.Status;
import iuh.fit.connectee.repo.AccountRepository;
import iuh.fit.connectee.repo.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    private final MongoTemplate mongoTemplate;


    @Autowired
    public UserServiceImpl(
            AccountRepository theAccountRepository,
            AuthenticationManager theAuthenticationManager,
            JwtService theJwtService,
            AppUserRepository theAppUserRepository, MongoTemplate mongoTemplate
    ) {
        accountRepository = theAccountRepository;
        authenticationManager = theAuthenticationManager;
        jwtService = theJwtService;
        appUserRepository = theAppUserRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public AppUser findAppUser(String accID) {
        Query query = new Query();
        query.addCriteria(Criteria.where("accId").is(accID));
        return mongoTemplate.findOne(query, AppUser.class);
    }

    public AppUser findAppUserByNickname(String nickname) {
        return appUserRepository.findById(nickname).orElse(null);
    }

//    @Override
//    public AppUser findAppUserByAbsoluteNickname(String nickname) {
//        return appUserRepository.findAppUserByAbsoluteNickname(nickname);
//    }

    public boolean isUserExist(String username) {
        return accountRepository.findByUsername(username) != null;
    }

    public boolean isNicknameExist(String nickname) {
        return appUserRepository.findById(nickname).isPresent();
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

    public void disconnect (String nickname) {
        var account = accountRepository.findById(
                appUserRepository.findById(nickname).isPresent()?appUserRepository.findById(nickname).get().getAccId():""
        ).orElse(null);
        onlineUsers.remove(nickname);
        if(account != null){
            account.setStatus(Status.OFFLINE);
            accountRepository.save(account);
        }
    }

    public void connect (String nickname) {
        var account = accountRepository.findById(
                appUserRepository.findById(nickname).isPresent()?appUserRepository.findById(nickname).get().getAccId():""
        ).orElse(null);
        onlineUsers.add(nickname);
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

        List<String> friendNicknames = new ArrayList<>(appUser.getFriendList());
        return appUserRepository.findAllById(friendNicknames);
    }


    public List<String> findFriendByUsernames(String username) {
        // 1. Tìm AppUser từ username → accId → AppUser
        AppUser appUser = appUserRepository.findAppUserByUsername(username);
        if (appUser == null) return List.of();

        List<String> friendNicknames = new ArrayList<>(appUser.getFriendList()) ;
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


//    public List<String> findConnectedUsernames(String nickname) {
//        // 1. Tìm AppUser qua username (qua accId)
//        AppUser appUser = appUserRepository.findById(nickname).isPresent()
//                ? appUserRepository.findById(nickname).get()
//                : null;
//
//        assert appUser != null;
//        List<String> friendNicknames = new ArrayList<>(appUser.getFriendList());
//        List<String> connectedUsernames = new ArrayList<>();
//
//        // 2. Duyệt từng nickname bạn bè
//        for (String nick : friendNicknames) {
//            Optional<AppUser> friendOpt = appUserRepository.findById(nick);
//
//            if (friendOpt.isPresent()) {
//                String accId = friendOpt.get().getAccId();
//
//                // Tìm Account qua accId dùng findById()
//                Optional<Account> accountOpt = accountRepository.findById(accId);
//
//                if (accountOpt.isPresent()) {
//                    String friendUsername = accountOpt.get().getUsername();
//
//                    if (isOnline(friendUsername)) {
//                        connectedUsernames.add(friendUsername);
//                    }
//                }
//            }
//        }
//
//        return connectedUsernames;
//    }

    public List<String> findConnectedUsernames(String nickname) {
        Optional<AppUser> optionalUser = appUserRepository.findById(nickname);

        if (optionalUser.isEmpty()) {
            System.out.println("AppUser not found for nickname: " + nickname);
            return Collections.emptyList();
        }

        AppUser appUser = optionalUser.get();

        return new ArrayList<>(appUser.getFriendList());
    }


    public List<AppUser> findFriends(String nickname) {
        return appUserRepository.findAppUserByRelativeNickname(nickname);
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