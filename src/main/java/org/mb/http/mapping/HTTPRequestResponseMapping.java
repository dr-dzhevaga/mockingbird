package org.mb.http.mapping;

import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.mb.http.basic.HTTPRequest;
import org.mb.http.basic.HTTPResponse;

import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 19.06.2015.
 */
public class HTTPRequestResponseMapping {
    private static final int    DEFAULT_RESPONSE_STATUS_CODE  = 404;
    private static final String DEFAULT_RESPONSE_CONTENT      = "Not found";

    private static final String LOG_MAPPING_IS_ADDED        = "Mapping is added%nRequest pattern:%n%s%nResponse:%n%s";
    private static final String LOG_REQUEST_IS_RECEIVED     = "Request is received%n%s";
    private static final String LOG_RESPONSE_IS_FOUND       = "Response is found in mapping%n%s";
    private static final String LOG_RESPONSE_IS_NOT_FOUND   = "Response is not found in mapping, default response will be used%n%s";

    final static private Logger Log = Logger.getLogger(HTTPRequestResponseMapping.class);
    private Map<HTTPRequestPattern, HTTPResponse> mapping = Maps.newLinkedHashMap();
    private HTTPResponse defaultResponse;

    public HTTPRequestResponseMapping() {
        defaultResponse = HTTPResponse.newBuilder().
                setStatusCode(DEFAULT_RESPONSE_STATUS_CODE).
                setContent(DEFAULT_RESPONSE_CONTENT).
                build();
    }

    public void addMapping(HTTPRequestPattern requestPattern, HTTPResponse response) {
        mapping.put(requestPattern, response);
        Log.info(String.format(LOG_MAPPING_IS_ADDED, requestPattern, response));
    }

    public void setDefaultResponse(HTTPResponse defaultResponse) {
        this.defaultResponse = defaultResponse;
    }

    public HTTPResponse findResponse(HTTPRequest request) {
        Log.info(String.format(LOG_REQUEST_IS_RECEIVED, request));
        for(Map.Entry<HTTPRequestPattern, HTTPResponse> entry : mapping.entrySet()) {
            HTTPRequestPattern requestPattern = entry.getKey();
            if(requestPattern.matches(request)) {
                HTTPResponse response = entry.getValue();
                Log.info(String.format(LOG_RESPONSE_IS_FOUND, response));
                return response;
            }
        }
        Log.info(String.format(LOG_RESPONSE_IS_NOT_FOUND, defaultResponse));
        return defaultResponse;
    }
}