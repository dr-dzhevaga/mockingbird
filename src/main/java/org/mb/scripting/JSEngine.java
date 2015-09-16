package org.mb.scripting;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by Dmitriy Dzhevaga on 16.09.2015.
 */
public class JSEngine implements Engine {
    private final static ScriptEngineManager factory = new ScriptEngineManager();
    private final ScriptEngine engine;
    private final Writer writer;

    private JSEngine() {
        engine = factory.getEngineByName("JavaScript");
        writer = new StringWriter();
        engine.getContext().setWriter(writer);
    }

    public static Engine newInstance() {
        return new JSEngine();
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

    @Override
    public String getPrintOutput() {
        return writer.toString();
    }
}
