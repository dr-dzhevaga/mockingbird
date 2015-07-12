package org.mb.marshalling;

import org.mb.binding.HTTPBinding;
import org.mb.binding.HTTPRequestPattern;
import org.mb.http.HTTPResponse;
import java.util.List;
import java.util.Map;
import static org.mb.marshalling.Utils.*;

/**
 * Created by Dmitriy Dzhevaga on 27.06.2015.
 */
public class Marshaller {
    private final static String REQUEST = "request";
    private final static String RESPONSE = "response";
    private final static String URI = "url";
    private final static String METHOD = "method";
    private final static String QUERY_PARAMETER = "queryParameter";
    private final static String HEADER = "header";
    private final static String STATUS_CODE = "statusCode";
    private final static String CONTENT = "content";

    public static HTTPBinding GetAsHTTPBinding(Object o) throws MarshallingException {
        List httpBindingsList = getAs(o, List.class, true);
        HTTPBinding httpBinding = new HTTPBinding();
        for(Object httpBindingObject : httpBindingsList) {
            Map httpBindingMap = getAs(httpBindingObject, Map.class, true);
            HTTPRequestPattern httpRequestPattern = getAsHTTPRequestPattern(httpBindingMap.get(REQUEST));
            HTTPResponse httpResponse = getAsHTTPResponse(httpBindingMap.get(RESPONSE));
            httpBinding.addBinding(httpRequestPattern, httpResponse);
        }
        return  httpBinding;
    }

    public static HTTPRequestPattern getAsHTTPRequestPattern(Object o) throws MarshallingException {
        Map httpRequestPatternMap = getAs(o, Map.class, true);

        HTTPRequestPattern.RequestPatternBuilder builder = HTTPRequestPattern.getBuilder();

        Object uriObject = httpRequestPatternMap.get(URI);
        String uri = getAsString(uriObject);
        if(!uri.isEmpty()) {
            builder.setUriPattern(uri);
        }

        Object methodObject = httpRequestPatternMap.get(METHOD);
        builder.addMethodsAsStrings(getAsList(methodObject, String.class));

        Object queryParameterObject = httpRequestPatternMap.get(QUERY_PARAMETER);
        builder.addQueryParameters(getAsMultimap(queryParameterObject, String.class, String.class));

        Object headerObject = httpRequestPatternMap.get(HEADER);
        builder.addHeaders(getAsMultimap(headerObject, String.class, String.class));

        return builder.build();
    }

    public static HTTPResponse getAsHTTPResponse(Object o) throws MarshallingException {
        Map httpResponseMap = getAs(o, Map.class, true);

        HTTPResponse.HTTPResponseBuilder builder = HTTPResponse.getBuilder();

        Object statusCodeObject = httpResponseMap.get(STATUS_CODE);
        String statusCode = getAsString(statusCodeObject);
        if(!statusCode.isEmpty()) {
            builder.setStatusCode(statusCode);
        }

        Object headerObject = httpResponseMap.get(HEADER);
        builder.addHeaders(getAsMap(headerObject, String.class, String.class));

        Object contentObject = httpResponseMap.get(CONTENT);
        String content = getAsString(contentObject);
        if(!content.isEmpty()) {
            builder.setContent(content);
        }

        return builder.build();
    }
}