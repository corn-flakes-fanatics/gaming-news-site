package corn.flakes.fanatics.ggs.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("password_recovery_token")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordRecoveryToken {
    
    @Id
    private String id;
    
    @DBRef
    private UserModel user;
    
    @Indexed(unique = true)
    private String token;
    
    private Date expirationDate;
    
}
