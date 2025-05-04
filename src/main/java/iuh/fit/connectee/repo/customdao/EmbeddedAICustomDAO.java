package iuh.fit.connectee.repo.customdao;

import iuh.fit.connectee.model.EmbeddedAI;

import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 02/05/2025 - 12:58 PM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.repo.customdao
 */
public interface EmbeddedAICustomDAO {
    List<EmbeddedAI> findAllEmbeddedAIByNickname(String nickname);
}
