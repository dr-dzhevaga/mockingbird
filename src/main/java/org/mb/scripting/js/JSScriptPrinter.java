package org.mb.scripting.js;

import org.mb.scripting.ScriptPrinter;

import java.io.IOException;
import java.io.Writer;
import java.nio.CharBuffer;

/**
 * Created by Dmitriy Dzhevaga on 21.09.2015.
 */
public class JSScriptPrinter implements ScriptPrinter {
    private static final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
    private static final String OPEN_PRINT = "print(";
    private static final String CLOSE_PRINT = ");\n";
    private static final String OPEN_LITERAL = "\"";
    private static final String CLOSE_LITERAL = "\"";
    private static final String OPEN_SCRIPT = "";
    private static final String CLOSE_SCRIPT = "\n";

    private Appendable output;

    @Override
    public ScriptPrinter setOutput(Appendable output) {
        this.output = output;
        return this;
    }

    @Override
    public Appendable getOutput() {
        return output;
    }

    @Override
    public ScriptPrinter openPrintFunction() throws IOException {
        output.append(OPEN_PRINT);
        return this;
    }

    @Override
    public ScriptPrinter closePrintFunction() throws IOException {
        output.append(CLOSE_PRINT);
        return this;
    }

    @Override
    public ScriptPrinter openLiteral() throws IOException {
        output.append(OPEN_LITERAL);
        return this;
    }

    @Override
    public ScriptPrinter appendLiteral(char ch) throws IOException {
        String escaped = null;
        if (ch <= '\\') {
            switch(ch) {
                case '\\':
                    escaped = "\\\\";
                    break;
                case '\'':
                    escaped= "\\'";
                    break;
                case '"':
                    escaped = "\\\"";
                    break;
                default:
                    if (ch < 32) {
                        escaped = toHex(ch);
                    }
            }
        }
        if (escaped == null) {
            output.append(ch);
        } else {
            output.append(escaped);
        }
        return this;
    }

    @Override
    public ScriptPrinter closeLiteral() throws IOException {
        output.append(CLOSE_LITERAL);
        return this;
    }
    @Override
    public ScriptPrinter openScript() throws IOException {
        output.append(OPEN_SCRIPT);
        return this;
    }

    @Override
    public ScriptPrinter appendScript(char ch) throws IOException {
        output.append(ch);
        return this;
    }

    @Override
    public ScriptPrinter closeScript() throws IOException {
        output.append(CLOSE_SCRIPT);
        return this;
    }

    private String toHex(char ch) {
        char[] r = new char[4];
        r[3] = HEX_DIGITS[ch & 0xF];
        ch >>>= 4;
        r[2] = HEX_DIGITS[ch & 0xF];
        r[1] = 'x';
        r[0] = '\\';
        return String.valueOf(r);
    }
}
