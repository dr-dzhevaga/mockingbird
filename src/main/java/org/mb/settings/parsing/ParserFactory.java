package org.mb.settings.parsing;

/**
 * Created by Dmitriy Dzhevaga on 27.06.2015.
 */
public final class ParserFactory {
    private ParserFactory() { }

    public static Parser newParser(FileFormat format) {
        switch (format) {
            case JSON:
                return new JSONParser();
            case YAML:
                return new YAMLParser();
            default:
                throw new IllegalArgumentException("Unsupported file format: " + format.toString());
        }
    }
}