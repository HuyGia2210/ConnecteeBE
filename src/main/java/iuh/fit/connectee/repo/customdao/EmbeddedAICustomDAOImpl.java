package iuh.fit.connectee.repo.customdao;

import iuh.fit.connectee.model.Account;
import iuh.fit.connectee.model.EmbeddedAI;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 02/05/2025 - 12:58 PM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.repo.customdao
 */
public class EmbeddedAICustomDAOImpl implements EmbeddedAICustomDAO {
    private final MongoTemplate mongoTemplate;

    public EmbeddedAICustomDAOImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<EmbeddedAI> findAllEmbeddedAIByNickname(String nickname) {
        Query embeddedAIQuery = new Query();
        embeddedAIQuery.addCriteria(Criteria.where("nickname").is(nickname));
        return mongoTemplate.find(embeddedAIQuery, EmbeddedAI.class);
    }
}
