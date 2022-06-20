package com.quantcast.activecookie.finder;

import com.quantcast.activecookie.error.ErrorWrapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public enum CookiesFinderStrategyImpl implements ICookiesFinderStrategy
{
    LINEAR_SEARCH
            {
                /**
                 * Linear search scans the logs linearly to find the matching date
                 * Since the logs are sorted in descending order, it breaks the search once it finds that the next record is not target date
                 *
                 * @param cookieLogs the logs that needs to be scanned
                 * @param targetDate the target date for which the results needs to be filtered
                 * @return most active cookie(s) for the target date
                 */
                @Override
                List<String> searchMostActiveCookies(List<String> cookieLogs, LocalDate targetDate)
                {
                    Map<String, Integer> cookiesCount = new LinkedHashMap<>();
                    boolean matchFound = false;
                    int maxFrequency = 0;
                    for (String cookieLog : cookieLogs)
                    {
                        String[] cookieLogSplit = cookieLog.split(",");
                        if (parseLocalDate(cookieLogSplit[1]).isEqual(targetDate))
                        {
                            int count = cookiesCount.merge(cookieLogSplit[0], 1, Integer::sum);
                            maxFrequency = Math.max(maxFrequency, count);
                            matchFound = true;
                        } else if (matchFound)
                        {
                            break;
                        }
                    }
                    return getMostFrequentCookies(cookiesCount, maxFrequency);
                }
            },

    BINARY_SEARCH
            {
                /**
                 * Binary search scans the logs using binary search to find the matching date
                 * Since the logs are sorted in descending order
                 * It first finds the start index of the date and then the end index of the date to create a slice
                 * for filtration
                 *
                 * note:
                 * Binary search may perform poor compared to linear search depending the size of the input list
                 * and the underlying hardware due to random access of the array and cpu cache miss + incorrect prefecthing cachelines into cpu cache
                 *
                 * @param cookieLogs the logs that needs to be scanned
                 * @param targetDate the target date for which the results needs to be filtered
                 * @return most active cookie(s) for the target date
                 */
                @Override
                List<String> searchMostActiveCookies(List<String> cookieLogs, LocalDate targetDate)
                {
                    int startIndex = getStartIndex(cookieLogs, targetDate);
                    int endIndex = getEndIndex(cookieLogs, targetDate);
                    Map<String, Integer> cookiesCount = new LinkedHashMap<>();
                    int maxFrequency = 0;
                    for (int i = startIndex; i <= endIndex; i++)
                    {
                        String[] cookieLogSplit = cookieLogs.get(i).split(",");
                        int count = cookiesCount.merge(cookieLogSplit[0], 1, Integer::sum);
                        maxFrequency = Math.max(maxFrequency, count);
                    }
                    return getMostFrequentCookies(cookiesCount, maxFrequency);
                }

                private int getStartIndex(List<String> cookieLogs, LocalDate targetDate)
                {
                    int left = 0;
                    int right = cookieLogs.size();
                    int index = left;
                    while (left <= right)
                    {
                        int mid = left + ((right - left) / 2);
                        LocalDate cookieDate = getDateFromCookieLog(cookieLogs, mid);
                        if (cookieDate.isBefore(targetDate))
                        {
                            //Go down in search
                            right = mid - 1;
                        } else if (cookieDate.isAfter(targetDate))
                        {
                            //Go up in search
                            left = mid + 1;
                        } else
                        {
                            //cookie date matches
                            if (mid == 0 || !CookiesFinderStrategyImpl.getDateFromCookieLog(cookieLogs, mid - 1).isEqual(targetDate))
                            {
                                //Since it is at 1st index, we set index var as mid i.e 0
                                //OR
                                //mid is > 0 hence we check if the previous element is not the same then we know that this is start
                                index = mid;
                                break;
                            } else
                            {
                                //previous date is same hence we need to go back up more to find the start index
                                right = mid - 1;
                            }
                        }
                    }
                    return index;
                }

                private int getEndIndex(List<String> cookieLogs, LocalDate targetDate)
                {
                    int left = 0;
                    int right = cookieLogs.size();
                    int index = right;
                    while (left <= right)
                    {
                        int mid = left + ((right - left) / 2);
                        LocalDate cookieDate = getDateFromCookieLog(cookieLogs, mid);
                        if (cookieDate.isBefore(targetDate))
                        {
                            //Go up in search
                            right = mid - 1;
                        } else if (cookieDate.isAfter(targetDate))
                        {
                            //Go down in search
                            left = mid + 1;
                        } else
                        {
                            //cookie date matches
                            if (mid == cookieLogs.size() - 1 || !CookiesFinderStrategyImpl.getDateFromCookieLog(cookieLogs, mid + 1).isEqual(targetDate))
                            {
                                //Since it is at last index, we set index var as mid i.e cookieLogs.size() - 1
                                //OR
                                //mid is < cookieLogs.size() - 1, hence we check if the next element is not the same then we know that this is start
                                index = mid;
                                break;
                            } else
                            {
                                //next date is same hence we need to go down more to find the end index
                                left = mid + 1;
                            }
                        }
                    }
                    return index;
                }
            };

    /**
     * Template method to Verify entry conditions and exception handling
     */
    @Override
    public CookiesFinderDto getMostActiveCookies(List<String> cookieLogs, LocalDate targetDate)
    {
        try
        {
            if (targetDate.isAfter(getDateFromCookieLog(cookieLogs, 0))
                    || targetDate.isBefore(getDateFromCookieLog(cookieLogs, cookieLogs.size() - 1)))
            {
                return new CookiesFinderDto(new ErrorWrapper(CookiesFinderError.NO_COOKIES_FOUND));
            }
            List<String> cookies = searchMostActiveCookies(cookieLogs, targetDate);
            if (cookies.isEmpty())
            {
                return new CookiesFinderDto(new ErrorWrapper(CookiesFinderError.NO_COOKIES_FOUND));
            }
            return new CookiesFinderDto(cookies);
        } catch (Exception e)
        {
            return new CookiesFinderDto(new ErrorWrapper(CookiesFinderError.INVALID_INPUT_DATA, e));
        }
    }

    private static LocalDate getDateFromCookieLog(List<String> cookieLogs, int index)
    {
        String[] cookieLogSplit = cookieLogs.get(index).split(",");
        return parseLocalDate(cookieLogSplit[1]);
    }

    private static LocalDate parseLocalDate(String text)
    {
        return LocalDateTime.parse(text, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalDate();
    }

    /**
     * Returns the cookies whose count is equal to maxFrequency
     */
    private static List<String> getMostFrequentCookies(Map<String, Integer> cookiesCount, int maxFrequency)
    {
        List<String> mostFrequentCookies = new ArrayList<>();
        for (Map.Entry<String, Integer> cookieEntry : cookiesCount.entrySet())
        {
            if (cookieEntry.getValue() == maxFrequency)
            {
                mostFrequentCookies.add(cookieEntry.getKey());
            }
        }
        return mostFrequentCookies;
    }

    abstract List<String> searchMostActiveCookies(List<String> cookieLogs, LocalDate targetDate);
}
