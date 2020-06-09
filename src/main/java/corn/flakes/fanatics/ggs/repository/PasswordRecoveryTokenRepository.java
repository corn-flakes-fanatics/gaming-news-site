package corn.flakes.fanatics.ggs.repository;

import corn.flakes.fanatics.ggs.model.PasswordRecoveryToken;
import corn.flakes.fanatics.ggs.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRecoveryTokenRepository extends MongoRepository<PasswordRecoveryToken, String> {
    
    PasswordRecoveryToken findByToken(String token);
    
    PasswordRecoveryToken findByUser(UserModel user);
    
}
