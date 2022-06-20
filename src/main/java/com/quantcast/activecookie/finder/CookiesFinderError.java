package com.quantcast.activecookie.finder;

import com.quantcast.activecookie.error.IError;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CookiesFinderError implements IError
{
    NO_COOKIES_FOUND("NCF-3001"),

    INVALID_INPUT_DATA("IID-3002");

    @Getter
    private String errorCode;

}
