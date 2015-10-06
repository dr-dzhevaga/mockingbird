package org.mb.jspl;

import org.apache.log4j.Logger;
import org.mb.scripting.Engine;
import org.mb.scripting.EngineFactory;
import org.mb.scripting.EngineType;
import org.mb.scripting.ScriptingException;

import java.io.Reader;
import java.io.Writer;
import java.io.StringWriter;
import java.io.IOException;

/**
 * Created by Dmitriy Dzhevaga on 17.09.2015.
 */
public final class JSPLikeProcessor {
    private static final String LOG_OUTPUT = "Jsp-like template after processing:%n%s";
    private static final Logger LOG = Logger.getLogger(JSPLikeProcessor.class);

    private final Engine engine;
    private final Reader jsp;

    private JSPLikeProcessor(final Reader jsp) {
        this.jsp = jsp;
        this.engine = EngineFactory.newInstance(EngineType.JS);
    }

    public static JSPLikeProcessor from(final Reader reader) {
        return new JSPLikeProcessor(reader);
    }

    public JSPLikeProcessor put(final String name, final Object value) {
        engine.put(name, value);
        return this;
    }

    public JSPLikeProcessor print(final Writer output) throws IOException, ScriptingException {
        try (Reader script = new JSPLikePreprocessor(jsp, engine.getScriptPrinter())) {
            if (!LOG.isDebugEnabled()) {
                engine.setWriter(output).eval(script);
            } else {
                Writer writer = new StringWriter();
                engine.setWriter(writer).eval(script);
                String result = writer.toString();
                LOG.debug(String.format(LOG_OUTPUT, result));
                output.write(result);
                output.flush();
            }
        }
        return this;
    }
}