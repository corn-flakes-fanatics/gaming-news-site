package corn.flakes.fanatics.ggs.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationUtils {
    
    private static final Pattern PATTERN_PASSWORD = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
    
    public static boolean isPasswordValid(String password) {
        return PATTERN_PASSWORD.matcher(password)
                .matches();
    }
    
}
