package org.mb.parsing;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 12.09.2015.
 */
public enum PathType {
    Xpath("Xpath"),
    RegExp("RegExp"),
    JSONPath("JSONPath");

    private final String string;
    private static final Map<String, PathType> stringToEnum = Maps.newHashMap();

    static {
        for(PathType value : values()) {
            stringToEnum.put(value.toString().toLowerCase(), value);
        }
    }

    PathType(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }

    public static PathType of(String string) throws IllegalArgumentException {
        PathType enumFromString = stringToEnum.get(string.toLowerCase());
        if(enumFromString == null) {
            // TODO: show expected input strings based on stringToEnum map
            throw new IllegalArgumentException("Unsupported parser path type: " + string);
        }
        return enumFromString;
    }
}