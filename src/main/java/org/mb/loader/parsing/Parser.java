package org.mb.loader.parsing;

import java.io.FileNotFoundException;
import java.io.Reader;

/**
 * Created by Dmitriy Dzhevaga on 27.06.2015.
 */
public interface Parser {
    Object parse(Reader inputFile) throws FileNotFoundException, ParsingException;
}

