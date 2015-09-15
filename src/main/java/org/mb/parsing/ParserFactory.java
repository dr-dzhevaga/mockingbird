package org.mb.parsing;

/**
 * Created by Dmitriy Dzhevaga on 11.09.2015.
 */
public class ParserFactory {
    public static Parser newParser(PathType pathType, String inputText) throws IllegalArgumentException {
        switch (pathType) {
            case Xpath:
                return new XpathParser(inputText);
            case RegExp:
                return new RegExpParser(inputText);
            case JSONPath:
                return new JsonPathParser(inputText);
        }
        throw new IllegalArgumentException("Unsupported parser path type: " + pathType.toString());
    }
}