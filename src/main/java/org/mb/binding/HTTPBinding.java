package org.mb.binding;

import org.mb.http.HTTPRequest;
import org.mb.http.HTTPResponse;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 19.06.2015.
 */
public class HTTPBinding {
    public static int DEFAULT_STATUS_CODE = 404;
    public static String DEFAULT_CONTENT = "Not found";

    private HTTPResponse defaultResponse;
    private Map<HTTPRequestPattern, HTTPResponse> binding = new LinkedHashMap<HTTPRequestPattern, HTTPResponse>();

    public HTTPBinding() {
        defaultResponse = HTTPResponse.getBuilder().
                setStatusCode(DEFAULT_STATUS_CODE).
                setContent(DEFAULT_CONTENT).
                build();
    }

    public void addBinding(HTTPRequestPattern requestPattern, HTTPResponse response) {
        binding.put(requestPattern, response);
    }

    public void setDefaultResponse(HTTPResponse defaultResponse) {
        this.defaultResponse = defaultResponse;
    }

    public HTTPResponse resolve(HTTPRequest request) {
        for(HTTPRequestPattern requestPattern : binding.keySet()) {
            if(requestPattern.matches(request, Collections.<String, String>emptyMap())) {
                return binding.get(requestPattern);
            }
        }
        return defaultResponse;
    }
}
