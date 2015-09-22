package org.mb.settings.parsing;

import org.mb.utils.EnumUtils;

/**
 * Created by Dmitriy Dzhevaga on 28.06.2015.
 */
public enum FileFormat {
    JSON,
    YAML;

    @Override
    public String toString() {
        return this.name();
    }

    public static FileFormat of(String name) throws IllegalArgumentException {
        return EnumUtils.valueOf(FileFormat.class, name);
    }
}
