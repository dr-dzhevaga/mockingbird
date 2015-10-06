package org.mb.scripting;

import java.io.IOException;

/**
 * Created by Dmitriy Dzhevaga on 21.09.2015.
 */
public interface ScriptPrinter {
    ScriptPrinter setOutput(Appendable output);
    Appendable getOutput();
    ScriptPrinter openPrintFunction() throws IOException;
    ScriptPrinter closePrintFunction() throws IOException;
    ScriptPrinter openLiteral() throws IOException;
    ScriptPrinter appendLiteral(char ch) throws IOException;
    ScriptPrinter closeLiteral() throws IOException;
    ScriptPrinter openScript() throws IOException;
    ScriptPrinter appendScript(char ch) throws IOException;
    ScriptPrinter closeScript() throws IOException;
}