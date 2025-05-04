package iuh.fit.connectee.repo.customdao;

import iuh.fit.connectee.model.Account;
import iuh.fit.connectee.model.Admin;
import iuh.fit.connectee.utils.AESUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Optional;

/**
 * @author Le Tran Gia Huy
 * @created 04/05/2025 - 8:20 PM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.repo.customdao
 */
public class AdminCustomDAOImpl implements AdminCustomDAO {

    private final MongoTemplate mongoTemplate;

    public AdminCustomDAOImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public boolean checkValidAdminAcc(String username, String password) {
        Query accountQuery = new Query();
        accountQuery.addCriteria(Criteria.where("username").is(username).and("password").is(AESUtil.encrypt(password)));
        Admin admin = mongoTemplate.findOne(accountQuery, Admin.class);
        return (admin != null);
    }
}
