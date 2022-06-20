package com.quantcast.activecookie.processor;

import com.quantcast.activecookie.cli.CliDto;
import com.quantcast.activecookie.cli.ICliParser;
import com.quantcast.activecookie.error.ErrorWrapper;
import com.quantcast.activecookie.error.IError;
import com.quantcast.activecookie.file.FileDto;
import com.quantcast.activecookie.file.IFileReader;
import com.quantcast.activecookie.finder.CookiesFinderDto;
import com.quantcast.activecookie.finder.CookiesFinderStrategyFactory;
import com.quantcast.activecookie.finder.ICookiesFinderStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CookiesProcessorImpl implements ICookiesProcessor
{
    private static final Logger consoleLogger = LoggerFactory.getLogger("console");

    private final ICliParser cliParser;
    private final IFileReader fileReader;
    private final CookiesFinderStrategyFactory cookiesFinderStrategyFactory;

    public CookiesProcessorImpl(ICliParser cliParser, IFileReader fileReader, CookiesFinderStrategyFactory cookiesFinderStrategyFactory)
    {
        this.cliParser = cliParser;
        this.fileReader = fileReader;
        this.cookiesFinderStrategyFactory = cookiesFinderStrategyFactory;
    }

    /**
     * Processes the command line arguments and mediates the control flow to print the most active cookies to console
     *
     * @param args Command line arguments
     */
    @Override
    public void process(String[] args)
    {
        CliDto cliDto = cliParser.parse(args);
        if (cliDto.getErrorWrapper() != null)
        {
            logToConsole(cliDto.getErrorWrapper());
            cliParser.printUsageToConsole();
            return;
        }

        FileDto fileDto = fileReader.read(cliDto.getFilePath());
        if (fileDto.getErrorWrapper() != null)
        {
            logToConsole(fileDto.getErrorWrapper());
            return;
        }

        ICookiesFinderStrategy cookieFinder = cookiesFinderStrategyFactory.getCookiesFinderStrategy();
        CookiesFinderDto mostActiveCookies = cookieFinder.getMostActiveCookies(fileDto.getFileContent(), cliDto.getDate());
        if (mostActiveCookies.getErrorWrapper() != null)
        {
            logToConsole(mostActiveCookies.getErrorWrapper());
            return;
        }
        mostActiveCookies.getCookies()
                .forEach(consoleLogger::info);
    }

    /**
     * Logs the error to console
     *
     * @param errorWrapper the object containing the error(s)
     */
    private void logToConsole(ErrorWrapper errorWrapper)
    {
        IError error = errorWrapper.getError();
        if (errorWrapper.getException() != null)
        {
            consoleLogger.error("{}\n{} : {}", errorWrapper.getException(), error.getErrorCode(), error.getErrorMessage());
        } else
        {
            consoleLogger.error("{} : {}", error.getErrorCode(), error.getErrorMessage());
        }
    }
}
