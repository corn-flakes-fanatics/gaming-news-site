package corn.flakes.fanatics.ggs.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import corn.flakes.fanatics.ggs.security.dto.LoginDTO;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Filter for authenticating user using JSON object
 *
 * @see corn.flakes.fanatics.ggs.security.dto.LoginDTO
 */
public class JsonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginDTO loginRequest = OBJECT_MAPPER.readValue(getRequestAsString(request), LoginDTO.class);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword());
        setDetails(request, token);
        return getAuthenticationManager().authenticate(token);
    }
    
    private String getRequestAsString(HttpServletRequest request) throws IOException {
        BufferedReader bufferedReader = request.getReader();
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ( (line = bufferedReader.readLine()) != null ) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
    
}
