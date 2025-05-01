package iuh.fit.connectee.repo.customdao;

import iuh.fit.connectee.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 01/05/2025 - 2:16 PM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.repo.customdao
 */

@Repository
public class MessageCustomDAOImpl implements MessageCustomDAO {
    private final MongoTemplate mongoTemplate;

    public MessageCustomDAOImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Message> findChatBetween(String user1, String user2) {
        Query query = new Query();
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("sender").is(user1).and("receiver").is(user2),
                Criteria.where("sender").is(user2).and("receiver").is(user1)
        ));
        query.with(Sort.by(Sort.Direction.ASC, "timestamp")); // Sắp xếp theo thời gian tăng dần
        return mongoTemplate.find(query, Message.class);
    }

}
