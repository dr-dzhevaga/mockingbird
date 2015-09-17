package org.mb.scripting;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;

/**
 * Created by Dmitriy Dzhevaga on 16.09.2015.
 */
public class JSEngine implements Engine {
    private final static ScriptEngineManager factory = new ScriptEngineManager();
    private final ScriptEngine engine;

    private JSEngine(Writer writer) {
        engine = factory.getEngineByName("JavaScript");
        engine.getContext().setWriter(writer);
    }

    public static Engine newInstance(Writer writer) {
        return new JSEngine(writer);
    }

    public static Engine newInstance(OutputStream outputStream) {
        return new JSEngine(new OutputStreamWriter(outputStream));
    }

    @Override
    public void eval(Reader reader) {
        try {
            engine.eval(reader);
        } catch (ScriptException e) {
            throw new ScriptingException(e);
        }
    }

    @Override
    public void put(String name, String value) {
        engine.put(name, value);
    }
}
