package com.quantcast.activecookie;

import com.quantcast.activecookie.processor.ICookiesProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Author : Dhaval Parmar
 * Email : dhavalparmar3.14@gmail.com
 */
@SpringBootApplication
public class ActiveCookieApplication implements CommandLineRunner
{
    private static final Logger logger = LoggerFactory.getLogger(ActiveCookieApplication.class);
    private static final Logger consoleLogger = LoggerFactory.getLogger("console");

    private final ICookiesProcessor cookiesProcessor;

    public ActiveCookieApplication(ICookiesProcessor cookiesProcessor)
    {
        this.cookiesProcessor = cookiesProcessor;
    }

    public static void main(String[] args)
    {
        try
        {
            SpringApplication.run(ActiveCookieApplication.class, args);
        } catch (Exception e)
        {
            logger.error("Something went wrong!", e);
            consoleLogger.error("Something went wrong!");
        }
    }

    @Override
    public void run(String... args)
    {
        cookiesProcessor.process(args);
    }
}
