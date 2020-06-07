package corn.flakes.fanatics.ggs.repository;

import corn.flakes.fanatics.ggs.model.Role;
import corn.flakes.fanatics.ggs.model.RoleModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends MongoRepository<RoleModel, String> {
    
    RoleModel findByValue(Role value);
    
}
