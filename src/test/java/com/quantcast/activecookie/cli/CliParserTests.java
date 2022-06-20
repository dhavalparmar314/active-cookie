package com.quantcast.activecookie.cli;

import org.apache.commons.cli.MissingOptionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Paths;
import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class CliParserTests
{
    @InjectMocks
    private CliParserImpl cliParser;

    @Test
    public void invalidArgsTest()
    {
        CliDto cliDto = cliParser.parse(null);
        Assertions.assertEquals(CliError.CLI_PARSING_EXCEPTION, cliDto.getErrorWrapper().getError());
        cliDto = cliParser.parse(new String[]{"1"});
        Assertions.assertEquals(CliError.CLI_PARSING_EXCEPTION, cliDto.getErrorWrapper().getError());
    }

    @Test
    public void noDateArgTest()
    {
        CliDto cliDto = cliParser.parse(new String[]{"-f", "src/test/resources/cookies.csv"});
        Assertions.assertEquals(CliError.CLI_PARSING_EXCEPTION, cliDto.getErrorWrapper().getError());
        Assertions.assertEquals(MissingOptionException.class, cliDto.getErrorWrapper().getException().getClass());
    }

    @Test
    public void noFilePathArgTest()
    {
        CliDto cliDto = cliParser.parse(new String[]{"-d", "2022-06-20"});
        Assertions.assertEquals(CliError.CLI_PARSING_EXCEPTION, cliDto.getErrorWrapper().getError());
        Assertions.assertEquals(MissingOptionException.class, cliDto.getErrorWrapper().getException().getClass());
    }

    @Test
    public void invalidFilePathTest()
    {
        CliDto cliDto = cliParser.parse(new String[]{"-d", "2022-06-20","-f", "some/invalid/path"});
        Assertions.assertEquals(CliError.INVALID_CSV_FILE, cliDto.getErrorWrapper().getError());
    }

    @Test
    public void invalidDateTest()
    {
        CliDto cliDto = cliParser.parse(new String[]{"-d", "abc","-f", "src/test/resources/cookies.csv"});
        Assertions.assertEquals(CliError.INVALID_DATE_FORMAT, cliDto.getErrorWrapper().getError());
    }

    @Test
    public void validArgsTest()
    {
        CliDto cliDto = cliParser.parse(new String[]{"-d", "2022-06-20","-f", "src/test/resources/cookies.csv"});
        Assertions.assertNull(cliDto.getErrorWrapper());
        Assertions.assertEquals(LocalDate.parse("2022-06-20"), cliDto.getDate());
        Assertions.assertEquals(Paths.get("src/test/resources/cookies.csv"), cliDto.getFilePath());
    }
}
