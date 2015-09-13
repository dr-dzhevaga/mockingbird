package org.mb.http.basic;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 17.06.2015.
 */
public enum Method {
    GET("GET"),
    HEAD("HEAD"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE"),
    TRACE("TRACE"),
    CONNECT("CONNECT");

    private final String string;
    private static final Map<String, Method> stringToEnum = Maps.newHashMap();

    static {
        for(Method value : values()) {
            stringToEnum.put(value.toString().toLowerCase(), value);
        }
    }

    Method(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }

    public static Method of(String string) throws IllegalArgumentException {
        Method method = stringToEnum.get(string.toLowerCase());
        if(method == null) {
            // TODO: show expected input strings based on stringToEnum map
            throw new IllegalArgumentException("Unknown HTTP method: " + string);
        }
        return method;
    }
}
