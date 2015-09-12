package org.mb.http.basic;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 17.06.2015.
 */
public enum HTTPMethod {
    GET("GET"),
    HEAD("HEAD"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE"),
    TRACE("TRACE"),
    CONNECT("CONNECT");

    private final String string;
    private static final Map<String, HTTPMethod> stringToEnum = Maps.newHashMap();

    static {
        for(HTTPMethod value : values()) {
            stringToEnum.put(value.toString().toLowerCase(), value);
        }
    }

    HTTPMethod(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }

    public static HTTPMethod of(String string) throws IllegalArgumentException {
        HTTPMethod method = stringToEnum.get(string.toLowerCase());
        if(method == null) {
            // TODO: show expected input strings based on stringToEnum map
            throw new IllegalArgumentException("Unknown HTTP method: " + string);
        }
        return method;
    }
}
