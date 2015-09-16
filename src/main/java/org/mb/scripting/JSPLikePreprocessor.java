package org.mb.scripting;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by Dmitriy Dzhevaga on 16.09.2015.
 */

public class JSPLikePreprocessor extends Reader {
    private enum STATE {
        TEXT("<%"),
        MACRO("%>"),
        SCRIPT("%>");

        private final String end;

        STATE(String end) {
            this.end = end;
        }

        public String getEnd() {
            return end;
        }
    }

    private final Reader reader;
    private STATE state = STATE.TEXT;

    public JSPLikePreprocessor(Reader reader) {
        this.reader = reader;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        return 0;
    }

    @Override
    public void close() throws IOException {

    }
}