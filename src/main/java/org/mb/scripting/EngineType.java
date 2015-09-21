package org.mb.scripting;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 21.09.2015.
 */
public enum EngineType {
    JS("JS");

    private final String string;
    private static final Map<String, EngineType> stringToEnum = Maps.newHashMap();

    static {
        for(EngineType value : values()) {
            stringToEnum.put(value.toString().toLowerCase(), value);
        }
    }

    EngineType(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }

    public static EngineType of(String string) throws IllegalArgumentException {
        EngineType enumFromString = stringToEnum.get(string.toLowerCase());
        if(enumFromString == null) {
            // TODO: show expected input strings based on stringToEnum map
            throw new IllegalArgumentException("Unsupported script engine type: " + string);
        }
        return enumFromString;
    }
}