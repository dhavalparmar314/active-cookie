package com.quantcast.activecookie.file;

import com.quantcast.activecookie.error.ErrorWrapper;
import lombok.Getter;

import java.util.List;

@Getter
public class FileDto
{
    private List<String> fileContent;
    private ErrorWrapper errorWrapper;

    public FileDto(List<String> fileContent)
    {
        this.fileContent = fileContent;
    }

    public FileDto(ErrorWrapper errorWrapper)
    {
        this.errorWrapper = errorWrapper;
    }
}
