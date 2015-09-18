package org.mb.scripting;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.IOException;
import java.io.Reader;

import static org.mb.scripting.JSPLikePreprocessor.State.*;

/**
 * Created by Dmitriy Dzhevaga on 16.09.2015.
 */
public class JSPLikePreprocessor extends Reader {
    protected enum State {
        TEXT(  "%>", "print(\"", "\");\n"),
        MACRO( "%=", "print(",   ");\n"),
        SCRIPT("<%", "",         "\n");

        final String startMacro;
        final String startScript;
        final String endScript;

        State(String startMacro, String startScript, String endScript) {
            this.startMacro = startMacro;
            this.startScript = startScript;
            this.endScript = endScript;
        }
    }

    private final Reader reader;
    private State state = TEXT;
    private int previous = -1;
    private int current = -1;
    private StringBuilder processed = new StringBuilder();
    private boolean textIsOpen;

    public JSPLikePreprocessor(Reader reader) {
        this.reader = reader;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        initCurrent();

        while(processed.length() < len) {
            readNext();
            if(current == -1) {
                if(previous != -1) {
                    if (state == TEXT) {
                        if(!textIsOpen) {
                            processed.append(state.startScript);
                        }
                        processed.append((char) previous).append(state.endScript);
                    }
                    previous = -1;
                }
                break;
            }

            switch (state) {
                case TEXT:
                    if(startWith(SCRIPT.startMacro)) {
                        if(textIsOpen) {
                            textIsOpen = false;
                            processed.append(state.endScript);
                        }
                        readNext();
                        if(startWith(MACRO.startMacro)) {
                            state = MACRO;
                            readNext();
                        } else {
                            state = SCRIPT;
                        }
                        processed.append(state.startScript);
                    } else {
                        if(!textIsOpen) {
                            textIsOpen = true;
                            processed.append(state.startScript);
                        }
                        // TODO: not effective way to escape chars
                        processed.append(StringEscapeUtils.escapeJavaScript(String.valueOf((char)previous)));
                    }
                    break;
                case MACRO:
                case SCRIPT:
                    if(startWith(TEXT.startMacro)) {
                        processed.append(state.endScript);
                        state = TEXT;
                        readNext();
                    } else {
                        processed.append((char) previous);
                    }
                    break;
            }
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

    private void initCurrent() throws IOException {
        if(current == -1) {
            current = reader.read();
        }
    }

    private void readNext() throws IOException {
        previous = current;
        current = reader.read();
    }

    private boolean startWith(String pattern) {
        return previous == pattern.charAt(0) && current == pattern.charAt(1);
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}