package org.mb.scripting;

import java.io.*;

/**
 * Created by Dmitriy Dzhevaga on 17.09.2015.
 */
public class JSPLikeProcessor {
    private final Reader reader;
    private final Engine engine;

    private JSPLikeProcessor(Reader reader) {
        this.reader = reader;
        engine = JSEngine.newInstance();
    }

    public static JSPLikeProcessor from(Reader reader) {
        return new JSPLikeProcessor(reader);
    }

    public JSPLikeProcessor put(String name, Object value) {
        engine.put(name, value);
        return this;
    }

    public void compile(Writer writer) {
        engine.setWriter(writer).eval(new JSPLikePreprocessor(reader));
    }
}