package corn.flakes.fanatics.ggs.repository;

import corn.flakes.fanatics.ggs.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserModel, String> {
    
    UserModel findByLogin(String login);
    
}
