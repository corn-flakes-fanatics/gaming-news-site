package corn.flakes.fanatics.ggs.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("role")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleModel {
    
    @Id
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String id;
    
    @Indexed(unique = true)
    private Role value;
    
}
