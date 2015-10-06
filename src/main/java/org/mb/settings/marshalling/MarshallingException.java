package org.mb.settings.marshalling;

/**
 * Created by Dmitriy Dzhevaga on 27.06.2015.
 */
public class MarshallingException extends Exception {
    public MarshallingException(final Throwable e) {
        super(e);
    }

    public MarshallingException(final String message) {
        super(message);
    }
}
