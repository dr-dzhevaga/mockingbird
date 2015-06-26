package org.mb.parsing;

/**
 * Created by Dmitriy Dzhevaga on 27.06.2015.
 */
public class ParserFactory {

    public static Parser newParser(InputFormat format) throws ParsingException {
        switch (format) {
            case JSON:
                return new JSONParser();
            case YAML:
                return new YAMLParser();
        }
        throw new ParsingException("Unsupported parser input format");
    }
}