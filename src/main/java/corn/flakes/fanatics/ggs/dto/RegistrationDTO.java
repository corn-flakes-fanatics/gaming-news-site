package corn.flakes.fanatics.ggs.dto;

import corn.flakes.fanatics.ggs.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDTO {
    
    private String login;
    
    private String username;
    
    private String email;
    
    private String password;
    
    public UserModel toModel() {
        return UserModel.builder()
                .login(this.login)
                .username(this.username)
                .email(this.email)
                .password(this.password)
                .build();
    }
    
}
