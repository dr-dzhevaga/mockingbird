package org.mb.scripting;

/**
 * Created by Dmitriy Dzhevaga on 16.09.2015.
 */
public class ScriptingException extends Exception {
    public ScriptingException(final Throwable e) {
        super(e);
    }

    public ScriptingException(final String message) {
        super(message);
    }
}