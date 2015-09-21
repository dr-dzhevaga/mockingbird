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
    private final Reader jspReader;

    private JSPLikeProcessor(Reader jspReader) {
        this.jspReader = jspReader;
        this.engine = EngineFactory.newInstance(EngineType.JS);
    }

    public static JSPLikeProcessor from(Reader reader) {
        return new JSPLikeProcessor(reader);
    }

    public JSPLikeProcessor put(String name, Object value) {
        engine.put(name, value);
        return this;
    }

    public JSPLikeProcessor process(Writer writer) throws IOException {
        try(Reader scriptReader = new JSPLikePreprocessor(jspReader, engine.getRules())) {
            engine.setWriter(writer).eval(scriptReader);
        }
        return this;
    }
}