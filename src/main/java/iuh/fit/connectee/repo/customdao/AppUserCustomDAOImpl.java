package iuh.fit.connectee.repo.customdao;

import iuh.fit.connectee.model.Account;
import iuh.fit.connectee.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

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
        String accId = mongoTemplate.findById(username, Account.class).getAccId();
        Query query = new Query();
        query.addCriteria(Criteria.where("accId").is(accId));
        return mongoTemplate.findOne(query, AppUser.class);
    }

    @Override
    public AppUser findAppUserByAccId(String accId) {
        return null;
    }
}
