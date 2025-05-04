package iuh.fit.connectee.repo;

import iuh.fit.connectee.model.EmbeddedAI;
import iuh.fit.connectee.repo.customdao.EmbeddedAICustomDAO;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Le Tran Gia Huy
 * @created 02/05/2025 - 12:57 PM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.repo
 */
public interface EmbeddedAIRepository extends MongoRepository<EmbeddedAI, String>, EmbeddedAICustomDAO {
}
