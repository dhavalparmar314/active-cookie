package com.quantcast.activecookie.file;

import com.quantcast.activecookie.error.IError;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum FileError implements IError
{
    FILE_PROCESSING_ERROR("FPE-2001");

    @Getter
    private String errorCode;
}
