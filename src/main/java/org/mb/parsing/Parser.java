package org.mb.parsing;

import java.util.List;

/**
 * Created by Dmitriy Dzhevaga on 11.09.2015.
 */
public interface Parser {
    String parse(String text, String path) throws ParsingException;
    List<String> parse(String text, List<String> path) throws ParsingException;
}