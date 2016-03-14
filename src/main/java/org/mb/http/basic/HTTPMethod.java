package org.mb.http.basic;

import org.mb.utils.EnumUtils;

/**
 * Created by Dmitriy Dzhevaga on 17.06.2015.
 */
public enum HTTPMethod {
    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    TRACE,
    CONNECT;

    public static HTTPMethod of(String name) {
        return EnumUtils.valueOf(HTTPMethod.class, name);
    }

    @Override
    public String toString() {
        return this.name();
    }
}