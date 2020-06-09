package corn.flakes.fanatics.ggs.messages;

import org.springframework.context.MessageSource;

import java.util.Locale;

public enum MessageCode {
    
    EMAIL_INVALID_FORMAT,
    EMAIL_NOT_FOUND,
    EMAIL_SENT,
    FIELD_ALREADY_TAKEN,
    FIELD_EMPTY,
    PASSWORD_NOT_STRONG_ENOUGH,
    PASSWORD_RESET,
    REGISTERED,
    SERVER_SIDE_ERROR_OCCURRED,
    OBJECT_REQUEST_EMPTY,
    TOKEN_EXPIRED,
    TOKEN_NOT_FOUND,
    VALIDATION_DID_NOT_PASS;
    
    private static MessageSource messageSource;
    
    public static void setMessageSource(MessageSource messageSource) {
        MessageCode.messageSource = messageSource;
    }
    
    public String getMessage(Object... args) {
        return messageSource.getMessage(replaceUnderscores(this.toString()), args, Locale.US);
    }
    
    private String replaceUnderscores(String message) {
        return message.replace("_", ".")
                .toLowerCase();
    }
    
}
