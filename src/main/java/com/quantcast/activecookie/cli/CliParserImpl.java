package com.quantcast.activecookie.cli;

import com.quantcast.activecookie.error.ErrorWrapper;
import org.apache.commons.cli.*;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.time.LocalDate;

@Component
public class CliParserImpl implements ICliParser
{
    private static final String DATE_ARG = "d";
    private static final String FILE_NAME_ARG = "f";

    private final CommandLineParser commandLineParser;
    private final Options commandLineOptions;

    /**
     * Initializes the CommandLine parser and sets the required options
     */
    public CliParserImpl()
    {
        commandLineParser = new DefaultParser();
        commandLineOptions = new Options();
        commandLineOptions.addOption(Option.builder(DATE_ARG)
                .desc("Date to filter in yyyy-MM-dd format")
                .hasArg()
                .argName("date")
                .required(true)
                .build());
        commandLineOptions.addOption(Option.builder(FILE_NAME_ARG)
                .desc("Absolute file path of the csv file to process (only csv files with .csv extension is accepted")
                .hasArg()
                .argName("filepath")
                .required(true)
                .build());
    }

    /**
     * Parses the command line arguments and validates the input file and date
     *
     * @param args The command line arguments
     * @return CliDto
     */
    @Override
    public CliDto parse(String[] args)
    {
        try
        {
            CommandLine line = commandLineParser.parse(commandLineOptions, args);
            LocalDate date = getDate(line.getOptionValue(DATE_ARG));
            if (date == null)
            {
                return new CliDto(new ErrorWrapper(CliError.INVALID_DATE_FORMAT));
            }
            Path filePath = getFile(line.getOptionValue(FILE_NAME_ARG));
            if (filePath == null)
            {
                return new CliDto(new ErrorWrapper(CliError.INVALID_CSV_FILE));
            }
            return new CliDto(date, filePath);
        } catch (ParseException e)
        {
            return new CliDto(new ErrorWrapper(CliError.CLI_PARSING_EXCEPTION, e));
        }
    }

    /**
     * Writes Usage help to STD_OUT
     */
    @Override
    public void printUsageToConsole()
    {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("active-cookie", commandLineOptions);
    }
}
