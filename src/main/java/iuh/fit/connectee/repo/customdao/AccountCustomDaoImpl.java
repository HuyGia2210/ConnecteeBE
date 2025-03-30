package iuh.fit.connectee.repo.customdao;

import iuh.fit.connectee.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class AccountCustomDaoImpl implements AccountCustomDAO {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public AccountCustomDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Account findByUsername(String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        return mongoTemplate.findOne(query, Account.class);
    }
}
