package org.mb.scripting;

import java.io.Reader;
import java.io.Writer;

/**
 * Created by Dmitriy Dzhevaga on 16.09.2015.
 */
public interface Engine {
    Engine setOutput(Writer writer);
    Engine putInContext(String key, Object value);
    void eval(Reader reader) throws ScriptingException;
}