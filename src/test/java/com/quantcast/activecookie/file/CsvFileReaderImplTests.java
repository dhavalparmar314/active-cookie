package com.quantcast.activecookie.file;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

@ExtendWith(MockitoExtension.class)
public class CsvFileReaderImplTests
{
    @InjectMocks
    private CsvFileReaderImpl fileReader;

    @Test
    public void emptyCsvFileTest()
    {
        Path path = Paths.get("src", "test", "resources", "empty.csv");
        FileDto fileDto = fileReader.read(path);
        Assertions.assertNull(fileDto.getErrorWrapper());
        Assertions.assertEquals(0, fileDto.getFileContent().size());
    }

    @Test
    public void invalidInputPathTest()
    {
        Path path = Paths.get("src", "test", "resources1");
        FileDto fileDto = fileReader.read(path);
        Assertions.assertEquals(FileError.FILE_PROCESSING_ERROR, fileDto.getErrorWrapper().getError());
        Assertions.assertEquals(NoSuchFileException.class, fileDto.getErrorWrapper().getException().getClass());
    }

    @Test
    public void validCsvFileTest()
    {
        Path path = Paths.get("src", "test", "resources", "cookies.csv");
        FileDto fileDto = fileReader.read(path);
        Assertions.assertNull(fileDto.getErrorWrapper());
        Assertions.assertEquals(8, fileDto.getFileContent().size());
        Assertions.assertEquals("AtY0laUfhglK3lC7,2018-12-09T06:19:00+00:00", fileDto.getFileContent().get(3));
    }
}
