package org.mb.scripting.js;

import org.mb.scripting.Syntax;

import java.util.List;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 21.09.2015.
 */
public class JSSyntax implements Syntax {
    private static final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
    private static final String OPEN_PRINT = "print(";
    private static final String CLOSE_PRINT = ");\n";
    private static final String OPEN_LITERAL = "\"";
    private static final String CLOSE_LITERAL = "\"";
    private static final String OPEN_SCRIPT = "";
    private static final String CLOSE_SCRIPT = "\n";

    @Override
    public char[] openPrint() {
        return OPEN_PRINT.toCharArray();
    }

    @Override
    public char[] closePrint() {
        return CLOSE_PRINT.toCharArray();
    }

    @Override
    public char[] openLiteral() {
        return OPEN_LITERAL.toCharArray();
    }

    @Override
    public char[] escapeLiteral(char ch) {
        char[] r;
        if(ch >'\\') {
            r = new char[] {ch};
        } else {
            switch(ch) {
                case '\\':
                    r = "\\\\".toCharArray();
                    break;
                case '\'':
                    r = "\\'".toCharArray();
                    break;
                case '"':
                    r = "\\\"".toCharArray();
                    break;
                default:
                    if(ch < 32) {
                        r = toHex(ch);
                    } else {
                        r = new char[] {ch};
                    }
            }
        }
        return r;
    }

    @Override
    public char[] closeLiteral() {
        return CLOSE_LITERAL.toCharArray();
    }
    @Override
    public char[] openScript() {
        return OPEN_SCRIPT.toCharArray();
    }

    @Override
    public char[] closeScript() {
        return CLOSE_SCRIPT.toCharArray();
    }

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
