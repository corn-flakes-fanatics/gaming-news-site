package corn.flakes.fanatics.ggs.service;

import corn.flakes.fanatics.ggs.dto.RegistrationDTO;
import corn.flakes.fanatics.ggs.model.UserModel;
import corn.flakes.fanatics.ggs.validator.ValidationException;

public interface RegistrationService {
    
    String REGISTER_MAPPING = "/register";
    
    /**
     * Register new user with hashed password
     *
     * @param registrationDTO registrationDTO containing login, username, email and password
     * @return saved user model
     * @throws ValidationException containing all validation messages from validator
     * @see corn.flakes.fanatics.ggs.messages.ValidationMessageContainer
     */
    UserModel registerUser(RegistrationDTO registrationDTO) throws ValidationException;
    
}
