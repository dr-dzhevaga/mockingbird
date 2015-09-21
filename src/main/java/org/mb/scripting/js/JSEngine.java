package org.mb.scripting.js;

import org.mb.scripting.Engine;
import org.mb.scripting.Syntax;
import org.mb.scripting.ScriptingException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;

/**
 * Created by Dmitriy Dzhevaga on 16.09.2015.
 */
public class JSEngine implements Engine {
    private final static String ENGINE_NAME = "nashorn";
    private final static Syntax syntax = new JSSyntax();
    private final static ScriptEngineManager factory = new ScriptEngineManager();
    private final ScriptEngine engine;

    private JSEngine() {
        engine = factory.getEngineByName(ENGINE_NAME);
    }

    public static Engine newInstance() {
        return new JSEngine();
    }

    @Override
    public Engine eval(Reader reader) {
        try {
            engine.eval(reader);
        } catch (ScriptException e) {
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
    public Syntax getRules() {
        return syntax;
    }
}