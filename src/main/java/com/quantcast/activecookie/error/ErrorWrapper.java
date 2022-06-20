package com.quantcast.activecookie.error;

import lombok.Getter;

@Getter
public class ErrorWrapper
{
    private IError error;
    private Exception exception;

    public ErrorWrapper(IError error)
    {
        this.error = error;
    }

    public ErrorWrapper(IError error, Exception exception)
    {
        this.error = error;
        this.exception = exception;
    }
}
