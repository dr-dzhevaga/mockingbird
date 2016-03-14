package org.mb.jspl;

import org.apache.log4j.Logger;
import org.mb.scripting.ScriptPrinter;
import org.mb.scripting.ScriptingFactory;

import java.io.IOException;
import java.io.Reader;

import static org.mb.jspl.JSPLikePreprocessor.State.*;

/**
 * Created by Dmitriy Dzhevaga on 16.09.2015.
 */
public final class JSPLikePreprocessor extends Reader {
    private static final Logger log = Logger.getLogger(JSPLikePreprocessor.class);
    private static final String LOG_OUTPUT = "Jsp-like template after preprocessing:%n%s";
    private static final int BUFF_INITIAL_CAPACITY = 1024;
    private final Reader reader;
    private final ScriptPrinter scriptPrinter;
    private State state;
    private StringBuilder output;
    private int previousChar;
    private int currentChar;
    private boolean textIsOpened;

    public JSPLikePreprocessor(Reader reader) {
        this.reader = reader;
        this.output = new StringBuilder(BUFF_INITIAL_CAPACITY);
        this.scriptPrinter = ScriptingFactory.newScriptPrinter(output);
        this.state = TEXT;
        this.previousChar = -1;
        this.currentChar = -1;
    }

    @Override
    public int read(char[] dest, int destOff, int length) throws IOException {
        if (currentChar == -1) {
            readNextChar();
        }
        while (output.length() < length) {
            readNextChar();
            if (currentChar == -1) {
                if (previousChar != -1) {
                    if (state == TEXT) {
                        if (!textIsOpened) {
                            scriptPrinter.openPrintFunction().openLiteral();
                        }
                        scriptPrinter.appendLiteral((char) previousChar).closeLiteral().closePrintFunction();
                    }
                    previousChar = -1;
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
                        readNextChar();
                        if (startsWith(MACRO.start)) {
                            state = MACRO;
                            scriptPrinter.openPrintFunction();
                            readNextChar();
                        } else {
                            state = SCRIPT;
                            scriptPrinter.openScript();
                        }
                    } else {
                        if (!textIsOpened) {
                            textIsOpened = true;
                            scriptPrinter.openPrintFunction().openLiteral();
                        }
                        scriptPrinter.appendLiteral((char) previousChar);
                    }
                    break;

                case MACRO:
                    if (startsWith(TEXT.start)) {
                        scriptPrinter.closePrintFunction();
                        state = TEXT;
                        readNextChar();
                    } else {
                        scriptPrinter.appendScript((char) previousChar);
                    }
                    break;

                case SCRIPT:
                    if (startsWith(TEXT.start)) {
                        scriptPrinter.closeScript();
                        state = TEXT;
                        readNextChar();
                    } else {
                        scriptPrinter.appendScript((char) previousChar);
                    }
                    break;
                default:
                    throw new IllegalStateException();
            }
        }

        if (output.length() == 0) {
            return -1;
        } else if (output.length() > length) {
            if (log.isDebugEnabled()) {
                log.debug(String.format(LOG_OUTPUT, output.substring(0, length)));
            }
            output.getChars(0, length, dest, destOff);
            output.delete(0, length);
            return length;
        } else {
            int processedLength = output.length();
            if (log.isDebugEnabled()) {
                log.debug(String.format(LOG_OUTPUT, output.substring(0, processedLength)));
            }
            output.getChars(0, output.length(), dest, destOff);
            output.setLength(0);
            return processedLength;
        }
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    private void readNextChar() throws IOException {
        previousChar = currentChar;
        currentChar = reader.read();
    }

    private boolean startsWith(final String pattern) {
        return previousChar == pattern.charAt(0) && currentChar == pattern.charAt(1);
    }

    protected enum State {
        TEXT("%>"),
        SCRIPT("<%"),
        MACRO("%=");

        private final String start;

        State(final String start) {
            this.start = start;
        }
    }
}