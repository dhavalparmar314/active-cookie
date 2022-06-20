package com.quantcast.activecookie.cli;

import com.quantcast.activecookie.error.ErrorWrapper;
import lombok.Getter;

import java.nio.file.Path;
import java.time.LocalDate;

@Getter
public class CliDto
{
    private LocalDate date;
    private Path filePath;
    private ErrorWrapper errorWrapper;

    public CliDto(LocalDate date, Path filePath)
    {
        this.date = date;
        this.filePath = filePath;
    }

    public CliDto(ErrorWrapper errorWrapper)
    {
        this.errorWrapper = errorWrapper;
    }
}
