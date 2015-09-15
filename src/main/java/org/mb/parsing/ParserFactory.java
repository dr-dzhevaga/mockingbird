package org.mb.parsing;

/**
 * Created by Dmitriy Dzhevaga on 11.09.2015.
 */
public class ParserFactory {
    public static Parser newParser(InputFormat inputFormat, String inputText) throws IllegalArgumentException {
        switch (inputFormat) {
            case XML:
                return new XmlParser(inputText);
            case TEXT:
                return new TextParser(inputText);
            case JSON:
                return new JsonParser(inputText);
        }
        throw new IllegalArgumentException("Unsupported parser type: " + inputFormat.toString());
    }
}