package corn.flakes.fanatics.ggs.repository;

import corn.flakes.fanatics.ggs.model.Role;
import corn.flakes.fanatics.ggs.model.RoleModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends MongoRepository<RoleModel, String> {
    
    Optional<RoleModel> findByValue(Role value);
    
}
