package com.quantcast.activecookie.error;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Used for localization of error messages
 */
@Component
public class MessageSourceUtil
{
    private static MessageSource messageSource = null;

    public MessageSourceUtil(MessageSource messageSource)
    {
        this.messageSource = messageSource;
    }

    public static String getMessage(String code, Object[] args, String defaultMessage, Locale locale)
    {
        if (messageSource == null)
        {
            return "";
        }
        return messageSource.getMessage(code, args, defaultMessage, locale);
    }
}
