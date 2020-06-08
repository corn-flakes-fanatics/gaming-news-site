package corn.flakes.fanatics.ggs;

import corn.flakes.fanatics.ggs.messages.MessageCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageCodeProvider {
    
    public static void setMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        MessageCode.setMessageSource(messageSource);
    }
    
}
