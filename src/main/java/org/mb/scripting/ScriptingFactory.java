package org.mb.scripting;

import org.mb.scripting.js.JSEngine;
import org.mb.scripting.js.JSScriptPrinter;

/**
 * Created by Dmitriy Dzhevaga on 21.09.2015.
 */
public final class ScriptingFactory {
    private ScriptingFactory() {
    }

    public static Engine newEngine() {
        return new JSEngine();
    }

    public static ScriptPrinter newScriptPrinter(Appendable output) {
        return new JSScriptPrinter(output);
    }
}