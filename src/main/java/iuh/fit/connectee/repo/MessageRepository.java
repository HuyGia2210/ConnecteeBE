package iuh.fit.connectee.repo;

import iuh.fit.connectee.model.Message;
import iuh.fit.connectee.repo.customdao.MessageCustomDAO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Le Tran Gia Huy
 * @created 30/03/2025 - 8:59 PM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.repo
 */

@Repository
public interface MessageRepository extends MongoRepository<Message, String>, MessageCustomDAO {
}
