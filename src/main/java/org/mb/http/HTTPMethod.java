package org.mb.http;

import com.google.common.collect.Maps;

import java.util.HashMap;
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
        for(HTTPMethod method : values()) {
            stringToEnum.put(method.toString().toLowerCase(), method);
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
            throw new IllegalArgumentException("Unknown HTTP method: " + string);
        }
        return method;
    }
}
