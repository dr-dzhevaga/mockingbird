package org.mb.settings.parsing;

import com.google.common.collect.Maps;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 28.06.2015.
 */
public enum InputFormat {
    JSON("JSON"),
    YAML("YAML");

    private final String string;
    private static final Map<String, InputFormat> stringToEnum = Maps.newHashMap();

    static {
        for(InputFormat value : values()) {
            stringToEnum.put(value.toString().toLowerCase(), value);
        }
    }

    InputFormat(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }

    public static InputFormat of(String string) throws IllegalArgumentException {
        InputFormat enumFromString = stringToEnum.get(string.toLowerCase());
        if(enumFromString == null) {
            // TODO: show expected input strings based on stringToEnum map
            throw new IllegalArgumentException("Unsupported file format: " + string);
        }
        return enumFromString;
    }
}
