package com.quantcast.activecookie.file;

import java.nio.file.Path;

public interface IFileReader
{
    FileDto read(Path path);
}
