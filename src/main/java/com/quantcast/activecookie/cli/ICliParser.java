package com.quantcast.activecookie.cli;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DateTimeException;
import java.time.LocalDate;

public interface ICliParser
{
    CliDto parse(String[] args);

    void printUsageToConsole();

    default Path getFile(String filePath)
    {
        Path path = Paths.get(filePath);
        boolean isValidFile = Files.isReadable(path) && path.getFileName().toString().endsWith(".csv");
        if (!isValidFile)
        {
            return null;
        }
        return path;
    }

    default LocalDate getDate(String inputDate)
    {
        try
        {
            return LocalDate.parse(inputDate);
        } catch (DateTimeException e)
        {
            //Do nothing
        }
        return null;
    }
}
