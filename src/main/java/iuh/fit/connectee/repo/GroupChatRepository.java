package iuh.fit.connectee.repo;

import iuh.fit.connectee.model.GroupChat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Le Tran Gia Huy
 * @created 10/04/2025 - 4:27 PM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.repo
 */

@Repository
public interface GroupChatRepository extends MongoRepository<GroupChat, String> {
}
