package iuh.fit.connectee.repo;

import iuh.fit.connectee.model.Admin;
import iuh.fit.connectee.repo.customdao.AdminCustomDAO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * @author Le Tran Gia Huy
 * @created 04/05/2025 - 8:19 PM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.repo
 */
public interface AdminRepository extends MongoRepository<Admin, String>, AdminCustomDAO {
}
