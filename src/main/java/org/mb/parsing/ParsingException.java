package org.mb.parsing;

/**
 * Created by Dmitriy Dzhevaga on 12.09.2015.
 */
public class ParsingException extends Exception {
    public ParsingException(final Throwable e) {
        super(e);
    }

    public ParsingException(final String message) {
        super(message);
    }
}
