package org.mb.scripting.js;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import org.apache.log4j.Logger;
import org.mb.scripting.Engine;
import org.mb.scripting.ScriptPrinter;
import org.mb.scripting.ScriptingException;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.*;

/**
 * Created by Dmitriy Dzhevaga on 16.09.2015.
 */
public class JSEngine implements Engine {
    private static final Logger Log = Logger.getLogger(JSEngine.class);
    private final static NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
    private final ScriptEngine engine;

    private JSEngine() {
        engine = factory.getScriptEngine("--print-no-newline=true");
    }

    public static Engine newInstance() {
        return new JSEngine();
    }

    @Override
    public Engine eval(Reader reader) {
        try {
            engine.eval(reader);
        } catch (ScriptException e) {
            Log.error(e);
            throw new ScriptingException(e);
        }
        return this;
    }

    @Override
    public Engine put(String key, Object value) {
        engine.put(key, value);
        return this;
    }

    @Override
    public Engine setWriter(Writer writer) {
        engine.getContext().setWriter(writer);
        return this;
    }

    @Override
    public ScriptPrinter getScriptPrinter() {
        return new JSScriptPrinter();
    }
}