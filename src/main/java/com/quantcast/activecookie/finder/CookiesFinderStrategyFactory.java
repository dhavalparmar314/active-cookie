package com.quantcast.activecookie.finder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookiesFinderStrategyFactory
{
    private String strategy;

    public CookiesFinderStrategyFactory(@Value("${cookie-finder.strategy:linear}") String strategy)
    {
        this.strategy = strategy.toUpperCase();
    }

    public ICookiesFinderStrategy getCookiesFinderStrategy()
    {
        switch (strategy)
        {
            case "BINARY":
            case "BINARY_SEARCH":
                return CookiesFinderStrategyImpl.BINARY_SEARCH;
            case "LINEAR":
            case "LINEAR_SEARCH":
            default:
                return CookiesFinderStrategyImpl.LINEAR_SEARCH;
        }
    }
}
