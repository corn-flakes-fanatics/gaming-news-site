package corn.flakes.fanatics.ggs.security.service;

import com.google.common.collect.Sets;
import corn.flakes.fanatics.ggs.model.UserModel;
import corn.flakes.fanatics.ggs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class MongoUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserModel user = userRepository.findByLogin(login);
        if ( user == null ) {
            throw new UsernameNotFoundException("User with login: " + login + " was not found");
        }
        
        return new User(user.getUsername(), user.getPassword(), createAuthorities(user));
    }
    
    private Set<SimpleGrantedAuthority> createAuthorities(UserModel user) {
        return Sets.newHashSet(new SimpleGrantedAuthority(user.getRole()
                .getValue()
                .toString()));
    }
    
}
