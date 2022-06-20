package com.quantcast.activecookie.file;

import com.quantcast.activecookie.error.ErrorWrapper;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CsvFileReaderImpl implements IFileReader
{
    /**
     * Reads the csv file and skips the first line(header) and parses into a list
     *
     * @param path the path to the csv file
     * @return FileDto
     */
    @Override
    public FileDto read(Path path)
    {
        try (BufferedReader reader = Files.newBufferedReader(path))
        {
            List<String> contents = reader.lines()
                    .skip(1)
                    .collect(Collectors.toList());
            return new FileDto(contents);
        } catch (IOException e)
        {
            return new FileDto(new ErrorWrapper(FileError.FILE_PROCESSING_ERROR, e));
        }
    }
}
