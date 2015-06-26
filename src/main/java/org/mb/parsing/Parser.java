package org.mb.parsing;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by Dmitriy Dzhevaga on 27.06.2015.
 */
public interface Parser {
    Object parse(FileReader inputFile) throws FileNotFoundException, ParsingException;
}

