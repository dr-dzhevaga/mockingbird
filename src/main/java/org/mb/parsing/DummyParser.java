package org.mb.parsing;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 11.09.2015.
 */
public class DummyParser implements Parser {
    private final String text;

    public DummyParser(String text) {
        this.text = text;
    }


    @Override
    public String parse(String path) throws ParsingException {
        return null;
    }

    @Override
    public Map<String, String> parse(Map<String, String> paths) throws ParsingException {
        return null;
    }
}