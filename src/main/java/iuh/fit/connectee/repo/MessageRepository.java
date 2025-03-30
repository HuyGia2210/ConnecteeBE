package iuh.fit.connectee.repo;

import iuh.fit.connectee.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Le Tran Gia Huy
 * @created 30/03/2025 - 8:59 PM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.repo
 */
public interface MessageRepository extends MongoRepository<Message, String> {
}
