package org.mb.parsing;

import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 11.09.2015.
 */
public class DummyParser implements Parser {
    @Override
    public String parse(String text, String path) throws ParsingException {
        return path;
    }

    @Override
    public List<String> parse(String text, List<String> path) throws ParsingException {
        return path;
    }
}