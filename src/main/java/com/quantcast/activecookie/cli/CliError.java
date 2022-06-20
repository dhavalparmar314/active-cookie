package com.quantcast.activecookie.cli;

import com.quantcast.activecookie.error.IError;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CliError implements IError
{
    CLI_PARSING_EXCEPTION("CPE-1001"),

    INVALID_DATE_FORMAT("IDF-1002"),

    INVALID_CSV_FILE("ICF-1003");

    @Getter
    private String errorCode;
}
