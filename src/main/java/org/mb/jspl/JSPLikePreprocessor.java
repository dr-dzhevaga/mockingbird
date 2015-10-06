package org.mb.jspl;

import org.apache.log4j.Logger;
import org.mb.scripting.ScriptPrinter;

import java.io.IOException;
import java.io.Reader;

import static org.mb.jspl.JSPLikePreprocessor.State.MACRO;
import static org.mb.jspl.JSPLikePreprocessor.State.SCRIPT;
import static org.mb.jspl.JSPLikePreprocessor.State.TEXT;

/**
 * Created by Dmitriy Dzhevaga on 16.09.2015.
 */
public final class JSPLikePreprocessor extends Reader {
    protected enum State {
        TEXT("%>"),
        SCRIPT("<%"),
        MACRO("%=");

        private final String start;

        State(final String start) {
            this.start = start;
        }
    }
    private static final String LOG_OUTPUT = "Jsp-like template after preprocessing:%n%s";
    private static final Logger LOG = Logger.getLogger(JSPLikePreprocessor.class);

    private static final int BUFF_INITIAL_CAPACITY = 1024;

    private final Reader jsp;
    private final ScriptPrinter scriptPrinter;
    private State state = TEXT;
    private StringBuilder processedBuff = new StringBuilder(BUFF_INITIAL_CAPACITY);
    private int previous = -1;
    private int current = -1;
    private boolean textIsOpened;

    public JSPLikePreprocessor(final Reader jsp, final ScriptPrinter scriptPrinter) {
        this.jsp = jsp;
        this.scriptPrinter = scriptPrinter;
        this.scriptPrinter.setOutput(processedBuff);
    }

    @Override
    public int read(final char[] dest, final int destOff, final int length) throws IOException {
        initCurrent();

        while (processedBuff.length() < length) {
            readNext();
            if (current == -1) {
                if (previous != -1) {
                    if (state == TEXT) {
                        if (!textIsOpened) {
                            scriptPrinter.openPrintFunction().openLiteral();
                        }
                        scriptPrinter.appendLiteral((char) previous).closeLiteral().closePrintFunction();
                    }
                    previous = -1;
                }
                break;
            }

            switch (state) {
                case TEXT:
                    if (startsWith(SCRIPT.start)) {
                        if (textIsOpened) {
                            textIsOpened = false;
                            scriptPrinter.closeLiteral().closePrintFunction();
                        }
                        readNext();
                        if (startsWith(MACRO.start)) {
                            state = MACRO;
                            scriptPrinter.openPrintFunction();
                            readNext();
                        } else {
                            state = SCRIPT;
                            scriptPrinter.openScript();
                        }
                    } else {
                        if (!textIsOpened) {
                            textIsOpened = true;
                            scriptPrinter.openPrintFunction().openLiteral();
                        }
                        scriptPrinter.appendLiteral((char) previous);
                    }
                    break;

                case MACRO:
                    if (startsWith(TEXT.start)) {
                        scriptPrinter.closePrintFunction();
                        state = TEXT;
                        readNext();
                    } else {
                        scriptPrinter.appendScript((char) previous);
                    }
                    break;

                case SCRIPT:
                    if (startsWith(TEXT.start)) {
                        scriptPrinter.closeScript();
                        state = TEXT;
                        readNext();
                    } else {
                        scriptPrinter.appendScript((char) previous);
                    }
                    break;
                default:
                    throw new IllegalStateException();
            }
        }

        if (processedBuff.length() == 0) {
            return  -1;
        } else if (processedBuff.length() > length) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format(LOG_OUTPUT, processedBuff.substring(0, length)));
            }
            processedBuff.getChars(0, length, dest, destOff);
            processedBuff.delete(0, length);
            return length;
        } else {
            int processedLength = processedBuff.length();
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format(LOG_OUTPUT, processedBuff.substring(0, processedLength)));
            }
            processedBuff.getChars(0, processedBuff.length(), dest, destOff);
            processedBuff.setLength(0);
            return processedLength;
        }
    }

    @Override
    public void close() throws IOException {
        jsp.close();
    }

    private void initCurrent() throws IOException {
        if (current == -1) {
            current = jsp.read();
        }
    }

    private void readNext() throws IOException {
        previous = current;
        current = jsp.read();
    }

    private boolean startsWith(final String pattern) {
        return previous == pattern.charAt(0) && current == pattern.charAt(1);
    }
}