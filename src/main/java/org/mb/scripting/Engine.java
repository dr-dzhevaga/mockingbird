package org.mb.scripting;

import java.io.Reader;
import java.io.Writer;

/**
 * Created by Dmitriy Dzhevaga on 16.09.2015.
 */
public interface Engine {
    Engine eval(Reader reader);
    Engine put(String key, Object value);
    Engine setWriter(Writer writer);
    ScriptPrinter getScriptPrinter();
}