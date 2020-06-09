package corn.flakes.fanatics.ggs;

import it.ozimov.springboot.mail.configuration.EnableEmailTools;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEmailTools
public class GamingNewsSiteApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(GamingNewsSiteApplication.class, args);
    }
    
}
