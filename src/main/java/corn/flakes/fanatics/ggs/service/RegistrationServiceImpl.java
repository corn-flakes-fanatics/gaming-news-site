package corn.flakes.fanatics.ggs.service;

import corn.flakes.fanatics.ggs.dto.RegistrationDTO;
import corn.flakes.fanatics.ggs.messages.ValidationMessageContainer;
import corn.flakes.fanatics.ggs.model.Role;
import corn.flakes.fanatics.ggs.model.RoleModel;
import corn.flakes.fanatics.ggs.model.UserModel;
import corn.flakes.fanatics.ggs.repository.RoleRepository;
import corn.flakes.fanatics.ggs.repository.UserRepository;
import corn.flakes.fanatics.ggs.validator.RegistrationValidator;
import corn.flakes.fanatics.ggs.validator.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    
    private final UserRepository userRepository;
    
    private final RoleRepository roleRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    private final RegistrationValidator registrationValidator;
    
    @Override
    public UserModel registerUser(RegistrationDTO registrationDTO) throws ValidationException {
        final ValidationMessageContainer validationResult = registrationValidator.validate(registrationDTO);
        if ( validationResult.hasMessages() ) {
            throw new ValidationException(validationResult.getMessages());
        }
        return userRepository.save(createUserWithHashedPasswordAndRole(registrationDTO));
    }
    
    @Override
    public boolean loginExists(String login) {
        return userRepository.findByLogin(login) != null;
    }
    
    @Override
    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }
    
    @Override
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }
    
    private UserModel createUserWithHashedPasswordAndRole(RegistrationDTO registrationDTO) {
        final RoleModel roleModel = roleRepository.findByValue(Role.ROLE_USER);
        UserModel userModel = registrationDTO.toModel();
        userModel.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        userModel.setRole(roleModel);
        return userModel;
    }
    
}
