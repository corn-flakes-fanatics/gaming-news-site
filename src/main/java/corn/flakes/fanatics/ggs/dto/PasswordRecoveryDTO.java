package corn.flakes.fanatics.ggs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordRecoveryDTO {
    
    private String token;
    
    private String password;
    
}
