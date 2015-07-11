package org.mb.binding;

import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.mb.http.HTTPRequest;
import org.mb.http.HTTPResponse;
import java.util.Collections;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 19.06.2015.
 */
public class HTTPBinding {
    public static int DEFAULT_STATUS_CODE = 404;
    public static String DEFAULT_CONTENT = "Not found";

    private HTTPResponse defaultResponse;
    private Map<HTTPRequestPattern, HTTPResponse> binding = Maps.newHashMap();
    final static private Logger Log = Logger.getLogger(HTTPBinding.class);

    public HTTPBinding() {
        defaultResponse = HTTPResponse.getBuilder().
                setStatusCode(DEFAULT_STATUS_CODE).
                setContent(DEFAULT_CONTENT).
                build();
    }

    public void addBinding(HTTPRequestPattern requestPattern, HTTPResponse response) {
        binding.put(requestPattern, response);
        Log.info(String.format("Binding is added%nRequest pattern:%n%s%nResponse:%n%s", requestPattern, response));
    }

    public void setDefaultResponse(HTTPResponse defaultResponse) {
        this.defaultResponse = defaultResponse;
    }

    public HTTPResponse resolve(HTTPRequest request) {
        Log.info(String.format("Request is received%n%s", request));
        for(HTTPRequestPattern requestPattern : binding.keySet()) {
            if(requestPattern.matches(request, Collections.<String, String>emptyMap())) {
                HTTPResponse response = binding.get(requestPattern);
                Log.info(String.format("Response is found in bindings%n%s", response));
                return response;
            }
        }
        Log.info(String.format("Response is not found in bindings, default response will be used%n%s", defaultResponse));
        return defaultResponse;
    }
}