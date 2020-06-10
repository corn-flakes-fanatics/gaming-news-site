package corn.flakes.fanatics.ggs.service;

import corn.flakes.fanatics.ggs.dto.RegistrationDTO;
import corn.flakes.fanatics.ggs.model.UserModel;
import corn.flakes.fanatics.ggs.validator.ValidationException;

public interface RegistrationService {
    
    String REGISTER_MAPPING = "/register";
    
    String CHECK_LOGIN_MAPPING = REGISTER_MAPPING + "/login/{login}";
    
    String CHECK_USERNAME_MAPPING = REGISTER_MAPPING + "/username/{username}";
    
    String CHECK_EMAIL_MAPPING = REGISTER_MAPPING + "/email/{email}";
    
    /**
     * Register new user with hashed password
     *
     * @param registrationDTO registrationDTO containing login, username, email and password
     * @return saved user model
     * @throws ValidationException containing all validation messages from validator
     * @see corn.flakes.fanatics.ggs.messages.ValidationMessageContainer
     */
    UserModel registerUser(RegistrationDTO registrationDTO) throws ValidationException;
    
    /**
     * Check if user with provided login already exists
     *
     * @param login login
     * @return true if user exists, otherwise false
     */
    boolean loginExists(String login);
    
    /**
     * Check if user with provided username already exists
     *
     * @param username username
     * @return true if user exists, otherwise false
     */
    boolean usernameExists(String username);
    
    /**
     * Check if user with provided email already exists
     *
     * @param email email
     * @return true if user exists, otherwise false
     */
    boolean emailExists(String email);
    
}
