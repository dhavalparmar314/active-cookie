package com.quantcast.activecookie.finder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CookiesFinderStrategyFactoryTests
{
    @Test
    public void invalidStrategyReturnsDefaultStrategyTest(){
        CookiesFinderStrategyFactory factory= new CookiesFinderStrategyFactory("invalid factory string");
        Assertions.assertEquals(CookiesFinderStrategyImpl.LINEAR_SEARCH, factory.getCookiesFinderStrategy());
    }

    @Test
    public void linearSearchStrategyTest(){
        CookiesFinderStrategyFactory factory= new CookiesFinderStrategyFactory("linear");
        Assertions.assertEquals(CookiesFinderStrategyImpl.LINEAR_SEARCH, factory.getCookiesFinderStrategy());
        factory= new CookiesFinderStrategyFactory("linear_search");
        Assertions.assertEquals(CookiesFinderStrategyImpl.LINEAR_SEARCH, factory.getCookiesFinderStrategy());
        factory= new CookiesFinderStrategyFactory("linEAR");
        Assertions.assertEquals(CookiesFinderStrategyImpl.LINEAR_SEARCH, factory.getCookiesFinderStrategy());
    }

    @Test
    public void binarySearchStrategyTest(){
        CookiesFinderStrategyFactory factory= new CookiesFinderStrategyFactory("binary");
        Assertions.assertEquals(CookiesFinderStrategyImpl.BINARY_SEARCH, factory.getCookiesFinderStrategy());
        factory= new CookiesFinderStrategyFactory("binary_search");
        Assertions.assertEquals(CookiesFinderStrategyImpl.BINARY_SEARCH, factory.getCookiesFinderStrategy());
        factory= new CookiesFinderStrategyFactory("binARY");
        Assertions.assertEquals(CookiesFinderStrategyImpl.BINARY_SEARCH, factory.getCookiesFinderStrategy());
    }
}
