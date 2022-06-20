package com.quantcast.activecookie.finder;

import java.time.LocalDate;
import java.util.List;

public interface ICookiesFinderStrategy
{
    CookiesFinderDto getMostActiveCookies(List<String> cookiesLogs, LocalDate targetDate);
}
