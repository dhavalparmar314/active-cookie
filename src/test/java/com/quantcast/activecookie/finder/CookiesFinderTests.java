package com.quantcast.activecookie.finder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CookiesFinderTests
{
    private List<String> cookieLogs;

    @BeforeAll
    public void setup()
    {
        cookieLogs = new ArrayList<>();
        cookieLogs.add("AtY0laUfhglK3lC7,2018-12-11T14:19:00+00:00");
        cookieLogs.add("AtY0laUfhglK3lC7,2018-12-09T14:19:00+00:00");
        cookieLogs.add("SAZuXPGUrfbcn5UA,2018-12-09T10:13:00+00:00");
        cookieLogs.add("5UAVanZf6UtGyKVS,2018-12-09T07:25:00+00:00");
        cookieLogs.add("AtY0laUfhglK3lC7,2018-12-09T06:19:00+00:00");
        cookieLogs.add("SAZuXPGUrfbcn5UA,2018-12-08T22:03:00+00:00");
        cookieLogs.add("4sMM2LxV07bPJzwf,2018-12-08T21:30:00+00:00");
        cookieLogs.add("fbcn5UAVanZf6UtG,2018-12-08T09:30:00+00:00");
        cookieLogs.add("4sMM2LxV07bPJzwf,2018-12-07T23:30:00+00:00");
        cookieLogs.add("4sMM2LxV07bPJzwf,2018-12-07T23:30:00+00:00");
        cookieLogs.add("fbcn5UAVanZf6UtG,2018-12-07T23:30:00+00:00");
        cookieLogs.add("fbcn5UAVanZf6UtG,2018-12-07T23:30:00+00:00");
    }

    @Test
    public void targetDateNotExistsTest()
    {
        LocalDate targetDateAfter = LocalDate.parse("2022-06-19");
        ICookiesFinderStrategy finderStrategy = CookiesFinderStrategyImpl.LINEAR_SEARCH;
        CookiesFinderDto mostActiveCookies = finderStrategy.getMostActiveCookies(cookieLogs, targetDateAfter);
        Assertions.assertEquals(CookiesFinderError.NO_COOKIES_FOUND, mostActiveCookies.getErrorWrapper().getError());
        LocalDate targetDateBefore = LocalDate.parse("2012-06-19");
        mostActiveCookies = finderStrategy.getMostActiveCookies(cookieLogs, targetDateBefore);
        Assertions.assertEquals(CookiesFinderError.NO_COOKIES_FOUND, mostActiveCookies.getErrorWrapper().getError());

        LocalDate targetDateBetween = LocalDate.parse("2018-12-10");
        mostActiveCookies = finderStrategy.getMostActiveCookies(cookieLogs, targetDateBetween);
        Assertions.assertEquals(CookiesFinderError.NO_COOKIES_FOUND, mostActiveCookies.getErrorWrapper().getError());
    }

    @Test
    public void cookieLogsDateInvalidTest()
    {
        LocalDate targetDate = LocalDate.parse("2022-06-19");
        ICookiesFinderStrategy finderStrategy = CookiesFinderStrategyImpl.BINARY_SEARCH;
        List<String> invalidCookiesLog = Collections.singletonList("aaa");
        CookiesFinderDto mostActiveCookies = finderStrategy.getMostActiveCookies(invalidCookiesLog, targetDate);
        Assertions.assertEquals(CookiesFinderError.INVALID_INPUT_DATA, mostActiveCookies.getErrorWrapper().getError());
    }

    @Test
    public void linearSearchSingleMatchTest()
    {
        finderStrategySingleMatchTest(CookiesFinderStrategyImpl.LINEAR_SEARCH);
    }

    @Test
    public void binarySearchSingleMatchTest()
    {
        finderStrategySingleMatchTest(CookiesFinderStrategyImpl.BINARY_SEARCH);
    }

    private void finderStrategySingleMatchTest(CookiesFinderStrategyImpl finderStrategy)
    {
        LocalDate date = LocalDate.parse("2018-12-09");
        CookiesFinderDto mostActiveCookies = finderStrategy.getMostActiveCookies(cookieLogs, date);
        Assertions.assertNull(mostActiveCookies.getErrorWrapper());
        Assertions.assertEquals(1, mostActiveCookies.getCookies().size());
        Assertions.assertEquals("AtY0laUfhglK3lC7", mostActiveCookies.getCookies().get(0));
    }

    @Test
    public void linearSearchMultipleMatchTest()
    {
        finderStrategyMultipleMatchTest(CookiesFinderStrategyImpl.LINEAR_SEARCH);
    }

    @Test
    public void binarySearchMultipleMatchTest()
    {
        finderStrategyMultipleMatchTest(CookiesFinderStrategyImpl.BINARY_SEARCH);
    }

    private void finderStrategyMultipleMatchTest(CookiesFinderStrategyImpl finderStrategy)
    {
        LocalDate date = LocalDate.parse("2018-12-07");
        CookiesFinderDto mostActiveCookies = finderStrategy.getMostActiveCookies(cookieLogs, date);
        Assertions.assertNull(mostActiveCookies.getErrorWrapper());
        Assertions.assertEquals(2, mostActiveCookies.getCookies().size());
        Assertions.assertEquals(Arrays.asList("4sMM2LxV07bPJzwf", "fbcn5UAVanZf6UtG"), mostActiveCookies.getCookies());
    }

}
