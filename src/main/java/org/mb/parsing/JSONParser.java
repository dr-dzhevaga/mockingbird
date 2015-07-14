package org.mb.parsing;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.Reader;

/**
 * Created by Dmitriy Dzhevaga on 27.06.2015.
 */
public class JSONParser implements Parser {

    private final Gson parser;

    public JSONParser() {
        this.parser = new Gson();
    }

    @Override
    public Object parse(Reader inputFile) throws FileNotFoundException {
        return parser.fromJson(inputFile, Object.class);
    }
}