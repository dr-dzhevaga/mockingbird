package org.mb.scripting.js;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import org.apache.log4j.Logger;
import org.mb.scripting.Engine;
import org.mb.scripting.ScriptingException;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.Reader;
import java.io.Writer;

/**
 * Created by Dmitriy Dzhevaga on 16.09.2015.
 */
public final class JSEngine implements Engine {
    private static final Logger log = Logger.getLogger(JSEngine.class);
    private static final NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
    private final ScriptEngine engine;

    public JSEngine() {
        this.engine = factory.getScriptEngine("--print-no-newline=true");
    }

    @Override
    public Engine setOutput(Writer writer) {
        engine.getContext().setWriter(writer);
        return this;
    }

    @Override
    public Engine putInContext(String key, Object value) {
        engine.put(key, value);
        return this;
    }

    @Override
    public void eval(Reader reader) throws ScriptingException {
        try {
            engine.eval(reader);
        } catch (ScriptException e) {
            log.error(e);
            throw new ScriptingException(e);
        }
    }
}