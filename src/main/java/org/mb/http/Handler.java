package org.mb.http;

/**
 * Created by Dmitriy Dzhevaga on 17.06.2015.
 */
public interface Handler {
    public HTTPResponse handle(HTTPRequest request);
}
