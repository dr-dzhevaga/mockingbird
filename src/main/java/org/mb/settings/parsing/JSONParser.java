package org.mb.settings.parsing;

import net.minidev.json.JSONValue;

import java.io.Reader;

/**
 * Created by Dmitriy Dzhevaga on 27.06.2015.
 */
public class JSONParser implements Parser {
    public JSONParser() {}

    @Override
    public Object parse(Reader inputFile) throws ParsingException {
        try {
            return JSONValue.parse(inputFile);
        } catch (Throwable e) {
            throw new ParsingException(e);
        }
    }
}