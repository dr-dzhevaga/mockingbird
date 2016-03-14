package org.mb.jspl;

import com.google.common.base.Charsets;
import org.apache.log4j.Logger;
import org.mb.scripting.Engine;
import org.mb.scripting.ScriptingException;
import org.mb.scripting.ScriptingFactory;

import java.io.*;

/**
 * Created by Dmitriy Dzhevaga on 17.09.2015.
 */
public final class JSPLikeProcessor {
    private static final Logger log = Logger.getLogger(JSPLikeProcessor.class);

    private final Reader input;
    private final Writer output;
    private final Engine engine;

    public JSPLikeProcessor(Reader input, Writer output) {
        this.input = input;
        this.output = output;
        this.engine = ScriptingFactory.newEngine();
    }

    public JSPLikeProcessor(InputStream input, OutputStream output) {
        this(new InputStreamReader(input, Charsets.UTF_8), new OutputStreamWriter(output, Charsets.UTF_8));
    }

    public JSPLikeProcessor putInContext(String name, Object value) {
        engine.putInContext(name, value);
        return this;
    }

    public JSPLikeProcessor print() throws IOException, ScriptingException {
        try (Reader script = new JSPLikePreprocessor(input)) {
            if (!log.isDebugEnabled()) {
                engine.setOutput(output).eval(script);
            } else {
                Writer writer = new StringWriter();
                engine.setOutput(writer).eval(script);
                String result = writer.toString();
                log.debug(String.format("Jsp-like template after processing:%n%s", result));
                output.write(result);
                output.flush();
            }
        }
        return this;
    }
}