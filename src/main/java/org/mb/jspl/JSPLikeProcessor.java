package org.mb.jspl;

import org.mb.scripting.Engine;
import org.mb.scripting.EngineFactory;
import org.mb.scripting.EngineType;

import java.io.*;

/**
 * Created by Dmitriy Dzhevaga on 17.09.2015.
 */
public class JSPLikeProcessor {
    private final Engine engine;
    private final Reader jsp;

    private JSPLikeProcessor(Reader jsp) {
        this.jsp = jsp;
        this.engine = EngineFactory.newInstance(EngineType.JS);
    }

    public static JSPLikeProcessor from(Reader reader) {
        return new JSPLikeProcessor(reader);
    }

    public JSPLikeProcessor put(String name, Object value) {
        engine.put(name, value);
        return this;
    }

    public JSPLikeProcessor print(Writer output) throws IOException {
        try(Reader script = new JSPLikePreprocessor(jsp, engine.getScriptPrinter())) {
            engine.setWriter(output).eval(script);
        }
        return this;
    }
}