package org.mb.scripting;

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

        final String openMacro;
        final String openScript;
        final String closeScript;

        State(String openMacro, String openScript, String closeScript) {
            this.openMacro = openMacro;
            this.openScript = openScript;
            this.closeScript = closeScript;
        }
    }

    private final Reader reader;
    private State state = TEXT;
    private int previous = -1;
    private int current = -1;
    private StringBuilder processedBuff = new StringBuilder();
    private boolean textIsOpened;

    public JSPLikePreprocessor(Reader reader) {
        this.reader = reader;
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
                            processedBuff.append(state.openScript);
                        }
                        processedBuff.append(escapeJS((char) previous)).append(state.closeScript);
                    }
                    previous = -1;
                }
                break;
            }

            switch (state) {
                case TEXT:
                    if(startsWith(SCRIPT.openMacro)) {
                        if(textIsOpened) {
                            textIsOpened = false;
                            processedBuff.append(state.closeScript);
                        }
                        readNext();
                        if(startsWith(MACRO.openMacro)) {
                            state = MACRO;
                            readNext();
                        } else {
                            state = SCRIPT;
                        }
                        processedBuff.append(state.openScript);
                    } else {
                        if(!textIsOpened) {
                            textIsOpened = true;
                            processedBuff.append(state.openScript);
                        }
                        processedBuff.append(escapeJS((char) previous));
                    }
                    break;
                case MACRO:
                case SCRIPT:
                    if(startsWith(TEXT.openMacro)) {
                        processedBuff.append(state.closeScript);
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

    private char[] escapeJS(char ch) {
        if(ch < 40) {
            switch(ch) {
                case '\'':
                    return "\\'".toCharArray();
                case '"':
                    return "\\\"".toCharArray();
                default:
                    if(ch < 32) {
                        return toHex(ch);
                    }
            }
        }
        return new char[] {ch};
    }

    private static final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();

    private char[] toHex(char ch) {
        char[] r = new char[4];
        r[3] = HEX_DIGITS[ch & 0xF];
        ch >>>= 4;
        r[2] = HEX_DIGITS[ch & 0xF];
        r[1] = 'x';
        r[0] = '\\';
        return r;
    }
}