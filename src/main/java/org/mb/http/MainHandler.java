package org.mb.http;

import com.google.common.collect.Maps;
import org.mb.http.basic.HTTPRequest;
import org.mb.http.basic.HTTPResponse;
import org.mb.http.basic.Handler;
import org.mb.http.mapping.HTTPRequestResponseMapping;

import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 11.09.2015.
 */
public class MainHandler implements Handler {
    private final HTTPRequestResponseMapping mapping;

    public MainHandler(HTTPRequestResponseMapping mapping) {
        this.mapping = mapping;
    }

    @Override
    public HTTPResponse handle(HTTPRequest request) {
        Map<String, String> context = Maps.newHashMap();
        return mapping.findResponse(request);
    }
}