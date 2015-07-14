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
    private static int DEFAULT_STATUS_CODE = 404;
    private static String DEFAULT_CONTENT = "Not found";

    private static final String BINDING_IS_ADDED = "Binding is added%nRequest pattern:%n%s%nResponse:%n%s";
    private static final String REQUEST_IS_RECEIVED = "Request is received%n%s";
    private static final String RESPONSE_IS_FOUND = "Response is found in bindings%n%s";
    private static final String RESPONSE_IS_NOT_FOUND = "Response is not found in bindings, default response will be used%n%s";

    private HTTPResponse defaultResponse;
    private Map<HTTPRequestPattern, HTTPResponse> binding = Maps.newLinkedHashMap();
    final static private Logger Log = Logger.getLogger(HTTPBinding.class);

    public HTTPBinding() {
        defaultResponse = HTTPResponse.getBuilder().
                setStatusCode(DEFAULT_STATUS_CODE).
                setContent(DEFAULT_CONTENT).
                build();
    }

    public void addBinding(HTTPRequestPattern requestPattern, HTTPResponse response) {
        binding.put(requestPattern, response);
        Log.info(String.format(BINDING_IS_ADDED, requestPattern, response));
    }

    public void setDefaultResponse(HTTPResponse defaultResponse) {
        this.defaultResponse = defaultResponse;
    }

    public HTTPResponse resolve(HTTPRequest request) {
        Log.info(String.format(REQUEST_IS_RECEIVED, request));
        for(HTTPRequestPattern requestPattern : binding.keySet()) {
            if(requestPattern.matches(request)) {
                HTTPResponse response = binding.get(requestPattern);
                Log.info(String.format(RESPONSE_IS_FOUND, response));
                return response;
            }
        }
        Log.info(String.format(RESPONSE_IS_NOT_FOUND, defaultResponse));
        return defaultResponse;
    }
}