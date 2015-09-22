package org.mb.parsing;

import org.mb.utils.EnumUtils;

/**
 * Created by Dmitriy Dzhevaga on 12.09.2015.
 */
public enum PathType {
    Xpath,
    RegExp,
    JSONPath;

    @Override
    public String toString() {
        return this.name();
    }

    public static PathType of(String name) throws IllegalArgumentException {
        return EnumUtils.valueOf(PathType.class, name);
    }
}