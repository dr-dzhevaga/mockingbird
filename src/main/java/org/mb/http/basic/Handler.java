package org.mb.http.basic;

/**
 * Created by Dmitriy Dzhevaga on 17.06.2015.
 */
public interface Handler {
    HTTPResponse handle(HTTPRequest request) throws Exception;
}
