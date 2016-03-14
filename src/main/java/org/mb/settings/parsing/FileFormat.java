package org.mb.settings.parsing;

import org.mb.utils.EnumUtils;

/**
 * Created by Dmitriy Dzhevaga on 28.06.2015.
 */
public enum FileFormat {
    JSON,
    YAML;

    public static FileFormat of(String name) {
        return EnumUtils.valueOf(FileFormat.class, name);
    }

    @Override
    public String toString() {
        return this.name();
    }
}
