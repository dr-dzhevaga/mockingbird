package org.mb.scripting.js;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import org.apache.log4j.Logger;
import org.mb.scripting.Engine;
import org.mb.scripting.ScriptPrinter;
import org.mb.scripting.ScriptingException;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.Reader;
import java.io.Writer;

/**
 * Created by Dmitriy Dzhevaga on 16.09.2015.
 */
public final class JSEngine implements Engine {
    private static final Logger LOG = Logger.getLogger(JSEngine.class);
    private static final NashornScriptEngineFactory FACTORY = new NashornScriptEngineFactory();
    private final ScriptEngine engine;

    private JSEngine() {
        engine = FACTORY.getScriptEngine("--print-no-newline=true");
    }

    public static Engine newInstance() {
        return new JSEngine();
    }

    @Override
    public Engine eval(final Reader reader) throws ScriptingException {
        try {
            engine.eval(reader);
        } catch (ScriptException e) {
            LOG.error(e);
            throw new ScriptingException(e);
        }
        return this;
    }

    @Override
    public Engine put(final String key, final Object value) {
        engine.put(key, value);
        return this;
    }

    @Override
    public Engine setWriter(final Writer writer) {
        engine.getContext().setWriter(writer);
        return this;
    }

    @Override
    public ScriptPrinter getScriptPrinter() {
        return new JSScriptPrinter();
    }
}