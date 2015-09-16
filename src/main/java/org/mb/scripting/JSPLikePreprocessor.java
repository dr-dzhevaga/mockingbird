package org.mb.scripting;

import com.google.common.collect.Queues;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Queue;

/**
 * Created by Dmitriy Dzhevaga on 16.09.2015.
 */

public class JSPLikePreprocessor extends Reader {
    private enum STATE {
        UNKNOWN,
        TEXT,
        MACRO,
        SCRIPT,
        MACRO_OR_SCRIPT
    }

    private final Reader reader;
    private STATE state = STATE.UNKNOWN;
    private int previous = -1;
    private StringBuilder processed = new StringBuilder();

    public JSPLikePreprocessor(Reader reader) {
        this.reader = reader;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        if(previous == -1) {
            previous = reader.read();
        }
        int current;
        while(processed.length() < len) {
            current = reader.read();
            if(current == -1 && previous != -1) {
                if(state == STATE.UNKNOWN) {
                    processed.append("print(\"");
                }
                processed.append(previous).append("\");");
                break;
            }
            switch (state) {
                case UNKNOWN:
                    if(previous == '<' && current == '%') {
                        state = STATE.MACRO_OR_SCRIPT;
                    } else {
                        state = STATE.TEXT;
                        processed.append("print(\"").append(previous);
                    }
                    break;
                case TEXT:
                    if(previous == '<' && current == '%') {
                        state = STATE.MACRO_OR_SCRIPT;
                        processed.append("\");");
                    } else {
                        processed.append(previous);
                    }
                    break;
                case MACRO_OR_SCRIPT:
                    if(current == '=') {
                        state = STATE.MACRO;
                        current = read();
                        processed.append("print(");
                    } else {
                        state = STATE.SCRIPT;
                    }
                    break;
                case MACRO:
                    if(previous == '%' && current == '>') {
                        state = STATE.UNKNOWN;
                        current = read();
                        processed.append(");");
                    } else {
                        processed.append(previous);
                    }
                    break;
                case SCRIPT:
                    if(previous == '%' && current == '>') {
                        state = STATE.UNKNOWN;
                        current = read();
                    } else {
                        processed.append(previous);
                    }
                    break;
            }
            previous = current;
        }
        if(processed.length() == 0) {
            return  -1;
        } else if(processed.length() > len) {
            System.arraycopy(processed.toString().toCharArray(), 0, cbuf, off, len);
            processed = new StringBuilder(processed.substring(len));
            return len;
        } else {
            int processedLength = processed.length();
            System.arraycopy(processed.toString().toCharArray(), 0, cbuf, off, processedLength);
            processed = new StringBuilder();
            return processedLength;
        }
    }

    @Override
    public void close() throws IOException {

    }
}