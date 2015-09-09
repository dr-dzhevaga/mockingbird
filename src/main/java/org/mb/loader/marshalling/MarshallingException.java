package org.mb.loader.marshalling;

/**
 * Created by Dmitriy Dzhevaga on 27.06.2015.
 */
public class MarshallingException extends Exception {
    public MarshallingException(Throwable e) {
        super(e);
    }

    public MarshallingException(String message) {
        super(message);
    }
}
