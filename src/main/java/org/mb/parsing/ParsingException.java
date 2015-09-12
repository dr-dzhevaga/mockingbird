package org.mb.parsing;

/**
 * Created by Dmitriy Dzhevaga on 12.09.2015.
 */
public class ParsingException extends Exception {
    public ParsingException(Throwable e) {
        super(e);
    }

    public ParsingException(String message) {
        super(message);
    }
}
