package org.mb.parsing;

/**
 * Created by Dmitriy Dzhevaga on 11.09.2015.
 */
public class ParserFactory {
    public static Parser newParser(ParserType parserType, String inputText) throws IllegalArgumentException {
        switch (parserType) {
            case DUMMY:
                return new DummyParser(inputText);
        }
        throw new IllegalArgumentException("Unsupported parser type: " + parserType.toString());
    }
}