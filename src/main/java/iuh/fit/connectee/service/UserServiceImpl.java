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

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        AppUser appUser = appUserRepository.findByUsername(username);
//        if(appUser == null) {
//            log.error("User not found in the database");
//            throw new UsernameNotFoundException("User not found in the database");
//        }else{
//            log.info("User found in the database: " + username);
//            Collection<SimpleGrantedAuthority> auths = new ArrayList<>();
//            appUser.getRoles().forEach(role -> {
//                auths.add(new SimpleGrantedAuthority(role.getRoleName()));
//            });
//            return new org.springframework.security.core.userdetails.User(appUser.getUsername(), appUser.getPassword(), auths);
//        }
//    }

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

//    @Override
//    public Role saveRole(Role role) {
//        log.info("Saving role: " + role);
//        return roleRepository.save(role);
//    }

    public void disconnect (String username) {
        var account = accountRepository.findByUsername(username);
        if(account != null){
            account.setStatus(Status.OFFLINE);
            accountRepository.save(account);
        }
    }

    public List<Optional<AppUser>> findFriendUsers(String username) {
        AppUser appUser = appUserRepository.findAppUserByUsername(username);
        List<String> friendNickNames = appUser.getFriendNickNames();
        List<Optional<AppUser>> temp = new ArrayList<>();
        friendNickNames.forEach(name -> {
            temp.add(appUserRepository.findById(name));
        });
        return temp;
    }

    public List<Optional<AppUser>> findConnectedUsers(String username) {
        AppUser appUser = appUserRepository.findAppUserByUsername(username);
        List<String> friendNickNames = appUser.getFriendNickNames();
        List<Optional<AppUser>> temp = new ArrayList<>();
        friendNickNames.forEach(name -> {
            temp.add(appUserRepository.findById(name));
        });
        return temp;
    }

    //    void addRoleToUser(String username, String roleName);
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

//    @Override
//    public void addRoleToUser(String username, String roleName) {
//        log.info("Adding role " + roleName +" to user: " + username);
//        AppUser appUser = appUserRepository.findByUsername(username);
//        Role role = roleRepository.findByRoleName(roleName);
//        appUser.getRoles().add(role);
//    }

    @Override
    public void deleteById(String theId) {
        accountRepository.deleteById(theId);
    }
}