package iuh.fit.connectee.repo;

import iuh.fit.connectee.model.Account;
import iuh.fit.connectee.repo.customdao.AccountCustomDAO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends MongoRepository<Account, String>, AccountCustomDAO {
    Account findByUsername(String username);
}
