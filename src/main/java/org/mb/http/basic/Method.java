package org.mb.http.basic;

import org.mb.utils.EnumUtils;

/**
 * Created by Dmitriy Dzhevaga on 17.06.2015.
 */
public enum Method {
    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    TRACE,
    CONNECT;

    @Override
    public String toString() {
        return this.name();
    }

    public static Method of(String name) {
        return EnumUtils.valueOf(Method.class, name);
    }
}