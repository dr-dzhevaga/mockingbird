package org.mb.scripting;

import org.mb.utils.EnumUtils;

/**
 * Created by Dmitriy Dzhevaga on 21.09.2015.
 */
public enum EngineType {
    JS;

    @Override
    public String toString() {
        return this.name();
    }

    public static EngineType of(final String name) {
        return EnumUtils.valueOf(EngineType.class, name);
    }
}