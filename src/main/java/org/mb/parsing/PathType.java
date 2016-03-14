package org.mb.parsing;

import org.mb.utils.EnumUtils;

/**
 * Created by Dmitriy Dzhevaga on 12.09.2015.
 */
public enum PathType {
    Xpath,
    RegExp,
    JSONPath;

    public static PathType of(String name) {
        return EnumUtils.valueOf(PathType.class, name);
    }

    @Override
    public String toString() {
        return this.name();
    }
}