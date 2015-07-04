package org.mb.parsing;

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
        for(InputFormat method : values()) {
            stringToEnum.put(method.toString().toLowerCase(), method);
        }
    }

    InputFormat(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }

    public static InputFormat fromString(String string) throws IllegalArgumentException {
        InputFormat method = stringToEnum.get(string.toLowerCase());
        if(method == null) {
            throw new IllegalArgumentException("Unknown file format: " + string);
        }
        return method;
    }
}
