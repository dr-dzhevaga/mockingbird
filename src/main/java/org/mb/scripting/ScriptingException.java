package org.mb.scripting;

/**
 * Created by Dmitriy Dzhevaga on 16.09.2015.
 */
public class ScriptingException extends RuntimeException {
    public ScriptingException(Throwable e) {
        super(e);
    }

    public ScriptingException(String message) {
        super(message);
    }
}