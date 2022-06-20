package com.quantcast.activecookie.finder;

import com.quantcast.activecookie.error.ErrorWrapper;
import lombok.Getter;

import java.util.List;

@Getter
public class CookiesFinderDto
{
    private List<String> cookies;
    private ErrorWrapper errorWrapper;

    public CookiesFinderDto(List<String> cookies)
    {
        this.cookies = cookies;
    }

    public CookiesFinderDto(ErrorWrapper errorWrapper)
    {
        this.errorWrapper = errorWrapper;
    }
}
