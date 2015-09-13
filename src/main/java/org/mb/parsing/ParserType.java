package org.mb.parsing;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 12.09.2015.
 */
public enum ParserType {
    DUMMY("DUMMY");

    private final String string;
    private static final Map<String, ParserType> stringToEnum = Maps.newHashMap();

    static {
        for(ParserType value : values()) {
            stringToEnum.put(value.toString().toLowerCase(), value);
        }
    }

    ParserType(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }

    public static ParserType of(String string) throws IllegalArgumentException {
        ParserType enumFromString = stringToEnum.get(string.toLowerCase());
        if(enumFromString == null) {
            // TODO: show expected input strings based on stringToEnum map
            throw new IllegalArgumentException("Unsupported parser type: " + string);
        }
        return enumFromString;
    }
}