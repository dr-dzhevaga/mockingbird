package org.mb.jspl;

import org.mb.scripting.Syntax;

import java.io.IOException;
import java.io.Reader;

import static org.mb.jspl.JSPLikePreprocessor.State.*;

/**
 * Created by Dmitriy Dzhevaga on 16.09.2015.
 */
public class JSPLikePreprocessor extends Reader {
    protected enum State {
        TEXT("%>"),
        SCRIPT("<%"),
        MACRO("%=");

        final String start;

        State(String start) {
            this.start = start;
        }
    }

    private final Reader reader;
    private final Syntax syntax;
    private State state = TEXT;
    private StringBuilder processedBuff = new StringBuilder();
    private int previous = -1;
    private int current = -1;
    private boolean textIsOpened;

    public JSPLikePreprocessor(Reader reader, Syntax syntax) {
        this.reader = reader;
        this.syntax = syntax;
    }

    @Override
    public int read(char[] dest, int destOff, int length) throws IOException {
        initCurrent();

        while(processedBuff.length() < length) {
            readNext();
            if(current == -1) {
                if(previous != -1) {
                    if (state == TEXT) {
                        if(!textIsOpened) {
                            processedBuff.append(syntax.openPrint()).
                                    append(syntax.openLiteral());
                        }
                        processedBuff.append(syntax.escapeLiteral((char) previous)).
                                append(syntax.closeLiteral()).
                                append(syntax.closePrint());
                    }
                    previous = -1;
                }
                break;
            }

            switch (state) {
                case TEXT:
                    if(startsWith(SCRIPT.start)) {
                        if(textIsOpened) {
                            textIsOpened = false;
                            processedBuff.append(syntax.closeLiteral()).
                                    append(syntax.closePrint());
                        }
                        readNext();
                        if(startsWith(MACRO.start)) {
                            state = MACRO;
                            processedBuff.append(syntax.openPrint());
                            readNext();
                        } else {
                            state = SCRIPT;
                            processedBuff.append(syntax.openScript());
                        }
                    } else {
                        if(!textIsOpened) {
                            textIsOpened = true;
                            processedBuff.append(syntax.openPrint()).
                                    append(syntax.openLiteral());
                        }
                        processedBuff.append(syntax.escapeLiteral((char) previous));
                    }
                    break;

                case MACRO:
                    if(startsWith(TEXT.start)) {
                        processedBuff.append(syntax.closePrint());
                        state = TEXT;
                        readNext();
                    } else {
                        processedBuff.append((char) previous);
                    }
                    break;

                case SCRIPT:
                    if(startsWith(TEXT.start)) {
                        processedBuff.append(syntax.closeScript());
                        state = TEXT;
                        readNext();
                    } else {
                        processedBuff.append((char) previous);
                    }
                    break;
            }
        }

        if(processedBuff.length() == 0) {
            return  -1;
        } else if(processedBuff.length() > length) {
            System.arraycopy(processedBuff.toString().toCharArray(), 0, dest, destOff, length);
            processedBuff = new StringBuilder(processedBuff.substring(length));
            return length;
        } else {
            int processedLength = processedBuff.length();
            System.arraycopy(processedBuff.toString().toCharArray(), 0, dest, destOff, processedLength);
            processedBuff = new StringBuilder();
            return processedLength;
        }
    }

    @Override
    public void close() throws IOException {
        reader.close();
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

    private boolean startsWith(String pattern) {
        return previous == pattern.charAt(0) && current == pattern.charAt(1);
    }
}