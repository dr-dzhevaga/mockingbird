package org.mb.loader.marshalling;

import org.mb.binding.HTTPBinding;
import org.mb.binding.HTTPRequestPattern;
import org.mb.http.HTTPResponse;
import java.util.List;
import java.util.Map;

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

    public static HTTPBinding toHTTPBinding(Object o) throws MarshallingException {
        List httpBindingsList = CommonMarshaller.toType(o, List.class, true);
        HTTPBinding httpBinding = new HTTPBinding();
        for(Object httpBindingObject : httpBindingsList) {
            Map httpBindingMap = CommonMarshaller.toType(httpBindingObject, Map.class, true);
            HTTPRequestPattern httpRequestPattern = toHTTPRequestPattern(httpBindingMap.get(REQUEST));
            HTTPResponse httpResponse = toHTTPResponse(httpBindingMap.get(RESPONSE));
            httpBinding.addBinding(httpRequestPattern, httpResponse);
        }
        return  httpBinding;
    }

    public static HTTPRequestPattern toHTTPRequestPattern(Object o) throws MarshallingException {
        Map httpRequestPatternMap = CommonMarshaller.toType(o, Map.class, true);

        HTTPRequestPattern.Builder builder = HTTPRequestPattern.getBuilder();

        Object uriObject = httpRequestPatternMap.get(URI);
        String uri = CommonMarshaller.toString(uriObject);
        if(!uri.isEmpty()) {
            builder.setUriPattern(uri);
        }

        Object methodObject = httpRequestPatternMap.get(METHOD);
        builder.addMethodsAsStrings(CommonMarshaller.toListOfType(methodObject, String.class));

        Object queryParameterObject = httpRequestPatternMap.get(QUERY_PARAMETER);
        builder.addQueryParameters(CommonMarshaller.toMultimapOfType(queryParameterObject, String.class, String.class));

        Object headerObject = httpRequestPatternMap.get(HEADER);
        builder.addHeaders(CommonMarshaller.toMultimapOfType(headerObject, String.class, String.class));

        return builder.build();
    }

    public static HTTPResponse toHTTPResponse(Object o) throws MarshallingException {
        Map httpResponseMap = CommonMarshaller.toType(o, Map.class, true);

        HTTPResponse.Builder builder = HTTPResponse.getBuilder();

        Object statusCodeObject = httpResponseMap.get(STATUS_CODE);
        String statusCode = CommonMarshaller.toString(statusCodeObject);
        if(!statusCode.isEmpty()) {
            builder.setStatusCode(statusCode);
        }

        Object headerObject = httpResponseMap.get(HEADER);
        builder.addHeaders(CommonMarshaller.toMapOfType(headerObject, String.class, String.class));

        Object contentObject = httpResponseMap.get(CONTENT);
        String content = CommonMarshaller.toString(contentObject);
        if(!content.isEmpty()) {
            builder.setContent(content);
        }

        return builder.build();
    }
}