package org.mb.parsing;

import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 11.09.2015.
 */
public interface Parser {
    String parse(String path) throws ParsingException;
    Map<String, String> parse(Map<String, String> paths) throws ParsingException;
}