package org.mb.parsing;

/**
 * Created by Dmitriy Dzhevaga on 11.09.2015.
 */
public final class ParserFactory {
    private ParserFactory() { }

    public static Parser newParser(PathType pathType, String inputText) {
        switch (pathType) {
            case Xpath:
                return new XpathParser(inputText);
            case RegExp:
                return new RegExpParser(inputText);
            case JSONPath:
                return new JsonPathParser(inputText);
            default:
                throw new IllegalArgumentException("Unsupported parser path type: " + pathType.toString());
        }
    }
}