package org.mb.cli;

/**
 * Created by Dmitriy Dzhevaga on 27.06.2015.
 */
public class ParsingException extends Exception {
    public ParsingException(final Throwable e) {
        super(e);
    }

    public ParsingException(final String message) {
        super(message);
    }
}
