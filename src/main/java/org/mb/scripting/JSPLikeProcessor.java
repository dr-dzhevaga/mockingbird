package org.mb.scripting;

import org.eclipse.jetty.io.WriterOutputStream;

import java.io.*;

/**
 * Created by Dmitriy Dzhevaga on 17.09.2015.
 */
public class JSPLikeProcessor {
    private final Reader reader;

    private JSPLikeProcessor(Reader reader) {
        this.reader = reader;
    }

    public static JSPLikeProcessor from(Reader reader) {
        return new JSPLikeProcessor(reader);
    }

    public static JSPLikeProcessor from(InputStream inputStream) {
        return from(new InputStreamReader(inputStream));
    }

    public void to(Writer writer) {
        JSEngine.newInstance(writer).eval(new JSPLikePreprocessor(reader));
    }

    public void to(OutputStream outputStream) {
        to(new OutputStreamWriter(outputStream));
    }
}