package com.quantcast.activecookie.error;

import org.springframework.context.i18n.LocaleContextHolder;

public interface IError
{
    String getErrorCode();

    default String getErrorMessage()
    {
        return MessageSourceUtil.getMessage(this.getErrorCode(), null, "", LocaleContextHolder.getLocale());
    }
}
