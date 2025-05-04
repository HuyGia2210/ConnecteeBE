package iuh.fit.connectee.service;


import iuh.fit.connectee.model.Account;
import iuh.fit.connectee.model.AppUserDetails;
import iuh.fit.connectee.repo.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
*@created 14/02/2025 - 7:33 PM
*@project gslendarBK
*@package gslendar.gslendarbk.service
*@author Le Tran Gia Huy
*/

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private AccountRepository accountRepository;

    @Autowired
    public UserDetailsServiceImpl(AccountRepository theAccountRepository) {
        accountRepository = theAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account user = accountRepository.findByUsername(username);

        if(user == null){
            throw new UsernameNotFoundException("User "+username+" not found");
        }

        return new AppUserDetails(user);
    }
}
