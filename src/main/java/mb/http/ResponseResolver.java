package mb.http;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 19.06.2015.
 */
public class ResponseResolver {
    private Map<HTTPRequestPattern, HTTPResponse> requestToResponseMapping = new LinkedHashMap<HTTPRequestPattern, HTTPResponse>();
    private HTTPResponse defaultResponse = new HTTPResponse(404, Collections.<String, String>emptyMap(), "Not found");

    public HTTPResponse resolve(HTTPRequest request) {
        for(HTTPRequestPattern requestPattern : requestToResponseMapping.keySet()) {
            if(requestPattern.matches(request, Collections.<String, String>emptyMap())) {
                return requestToResponseMapping.get(requestPattern);
            }
        }
        return defaultResponse;
    }

    public void addMapping(HTTPRequestPattern requestPattern, HTTPResponse response) {
        requestToResponseMapping.put(requestPattern, response);
    }
}
