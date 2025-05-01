package iuh.fit.connectee.repo;

import iuh.fit.connectee.model.AppUser;
import iuh.fit.connectee.repo.customdao.AppUserCustomDAO;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Le Tran Gia Huy
 * @created 30/03/2025 - 4:24 PM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.repo
 */

@Repository
public interface AppUserRepository extends MongoRepository<AppUser, String>, AppUserCustomDAO {
    
}
