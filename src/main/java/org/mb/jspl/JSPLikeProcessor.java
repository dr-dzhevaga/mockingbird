package org.mb.jspl;

import org.apache.log4j.Logger;
import org.mb.scripting.Engine;
import org.mb.scripting.EngineFactory;
import org.mb.scripting.EngineType;

import java.io.*;

/**
 * Created by Dmitriy Dzhevaga on 17.09.2015.
 */
public class JSPLikeProcessor {
    private static final String LOG_OUTPUT = "Jsp-like template after processing:\n%s";
    private static final Logger Log = Logger.getLogger(JSPLikeProcessor.class);

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
            if(!Log.isDebugEnabled()) {
                engine.setWriter(output).eval(script);
            } else {
                Writer writer = new StringWriter();
                engine.setWriter(writer).eval(script);
                String result = writer.toString();
                Log.debug(String.format(LOG_OUTPUT, result));
                output.write(result);
                output.flush();
            }
        }
        return this;
    }
}