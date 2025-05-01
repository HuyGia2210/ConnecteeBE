package iuh.fit.connectee.repo.customdao;

import iuh.fit.connectee.model.Account;
import iuh.fit.connectee.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Le Tran Gia Huy
 * @created 30/03/2025 - 7:06 PM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.repo.customdao
 */

@Repository
public class AppUserCustomDAOImpl implements AppUserCustomDAO {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public AppUserCustomDAOImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public AppUser findAppUserByUsername(String username) {
        Query accountQuery = new Query();
        accountQuery.addCriteria(Criteria.where("username").is(username));
        Account account = mongoTemplate.findOne(accountQuery, Account.class);

        if (account == null) {
            throw new UsernameNotFoundException("Account not found with username: " + username);
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("accId").is(account.getAccId()));
        return mongoTemplate.findOne(query, AppUser.class);
    }


    @Override
    public List<AppUser> findAppUserByRelativeNickname(String nickname) {
        Query query = new Query();
        query.addCriteria(Criteria.where("nickname").regex(".*" + Pattern.quote(nickname) + ".*", "i")); // "i" = ignore case

        return mongoTemplate.find(query, AppUser.class);
    }

    @Override
    public AppUser findAppUserByAbsoluteNickname(String nickname) {
        Query appUserQuery = new Query();
        appUserQuery.addCriteria(Criteria.where("nickname").is(nickname));
        return mongoTemplate.findOne(appUserQuery, AppUser.class);
    }


    @Override
    public AppUser findAppUserByAccId(String accId) {
        Query appUserQuery = new Query();
        appUserQuery.addCriteria(Criteria.where("accId").is(accId));
        return mongoTemplate.findOne(appUserQuery, AppUser.class);
    }
}
