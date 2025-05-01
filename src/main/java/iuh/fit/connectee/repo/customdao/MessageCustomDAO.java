package iuh.fit.connectee.repo.customdao;

import iuh.fit.connectee.model.Message;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 01/05/2025 - 2:16 PM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.repo.customdao
 */

@Repository
public interface MessageCustomDAO {
    List<Message> findChatBetween(String user1, String user2);
}
