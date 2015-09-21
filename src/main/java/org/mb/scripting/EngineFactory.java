package org.mb.scripting;

import org.mb.scripting.js.JSEngine;

/**
 * Created by Dmitriy Dzhevaga on 21.09.2015.
 */
public class EngineFactory {
    public static Engine newInstance(EngineType engineType) {
        switch (engineType) {
            case JS:
                return JSEngine.newInstance();
            default:
                throw new IllegalArgumentException("Unsupported script engine type: " + engineType.toString());
        }
    }
}