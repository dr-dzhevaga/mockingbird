package org.mb.scripting;

import org.mb.scripting.js.JSEngine;

/**
 * Created by Dmitriy Dzhevaga on 21.09.2015.
 */
public final class EngineFactory {
    private EngineFactory() { }

    public static Engine newInstance(final EngineType engineType) {
        switch (engineType) {
            case JS:
                return JSEngine.newInstance();
            default:
                throw new IllegalArgumentException("Unsupported script engine type: " + engineType.toString());
        }
    }
}