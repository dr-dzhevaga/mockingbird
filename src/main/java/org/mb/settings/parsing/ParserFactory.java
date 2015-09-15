package org.mb.settings.parsing;

/**
 * Created by Dmitriy Dzhevaga on 27.06.2015.
 */
public class ParserFactory {

    public static Parser newParser(FileFormat format) throws IllegalArgumentException {
        switch (format) {
            case JSON:
                return new JSONParser();
            case YAML:
                return new YAMLParser();
        }
        throw new IllegalArgumentException("Unsupported file format: " + format.toString());
    }
}