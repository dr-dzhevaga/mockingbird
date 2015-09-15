package org.mb.settings.parsing;

import com.google.common.collect.Maps;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 28.06.2015.
 */
public enum FileFormat {
    JSON("JSON"),
    YAML("YAML");

    private final String string;
    private static final Map<String, FileFormat> stringToEnum = Maps.newHashMap();

    static {
        for(FileFormat value : values()) {
            stringToEnum.put(value.toString().toLowerCase(), value);
        }
    }

    FileFormat(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }

    public static FileFormat of(String string) throws IllegalArgumentException {
        FileFormat enumFromString = stringToEnum.get(string.toLowerCase());
        if(enumFromString == null) {
            // TODO: show expected input strings based on stringToEnum map
            throw new IllegalArgumentException("Unsupported file format: " + string);
        }
        return enumFromString;
    }
}
