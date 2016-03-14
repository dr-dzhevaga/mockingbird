package org.mb.scripting.js;

import org.mb.scripting.ScriptPrinter;

import java.io.IOException;

/**
 * Created by Dmitriy Dzhevaga on 21.09.2015.
 */
public final class JSScriptPrinter implements ScriptPrinter {
    private final Appendable output;

    public JSScriptPrinter(Appendable output) {
        this.output = output;
    }

    @Override
    public ScriptPrinter openPrintFunction() throws IOException {
        output.append("print(");
        return this;
    }

    @Override
    public ScriptPrinter closePrintFunction() throws IOException {
        output.append(");\n");
        return this;
    }

    @Override
    public ScriptPrinter openLiteral() throws IOException {
        output.append("\"");
        return this;
    }

    @Override
    public ScriptPrinter appendLiteral(char ch) throws IOException {
        String escaped = null;
        if (ch <= '\\') {
            switch (ch) {
                case '\\':
                    escaped = "\\\\";
                    break;
                case '\'':
                    escaped = "\\'";
                    break;
                case '"':
                    escaped = "\\\"";
                    break;
                default:
                    if (ch < ' ') {
                        escaped = String.format("\\x%02X", (int) ch);
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
        output.append("\"");
        return this;
    }
    @Override
    public ScriptPrinter openScript() throws IOException {
        return this;
    }

    @Override
    public ScriptPrinter appendScript(char ch) throws IOException {
        output.append(ch);
        return this;
    }

    @Override
    public ScriptPrinter closeScript() throws IOException {
        output.append("\n");
        return this;
    }
}
