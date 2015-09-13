package org.mb.settings.marshalling;

import com.google.common.collect.Table;
import org.mb.http.mapping.HandlerDataMapping;
import org.mb.http.mapping.RequestPattern;
import org.mb.http.basic.Response;
import org.mb.parsing.ParserType;
import org.mb.settings.Settings;

import java.util.List;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 27.06.2015.
 */
public class Marshaller {
    private final static String MAPPING         = "mapping";
    private final static String PARSING         = "parsing";
    private final static String REQUEST         = "request";
    private final static String RESPONSE        = "response";
    private final static String URI             = "url";
    private final static String METHOD          = "method";
    private final static String QUERY_PARAMETER = "queryParameter";
    private final static String HEADER          = "header";
    private final static String STATUS_CODE     = "statusCode";
    private final static String CONTENT         = "content";

    public static Settings toSettings(Object o) throws MarshallingException {
        Map settingsMap = BaseMarshaller.toType(o, Map.class, true);
        HandlerDataMapping mapping = toHTTPMapping(settingsMap.get(MAPPING));
        Table<ParserType, String, String> parsing = BaseMarshaller.ToTableOfType(settingsMap.get(PARSING), ParserType.class, String.class, String.class);
        return new Settings(mapping, parsing);
    }

    public static HandlerDataMapping toHTTPMapping(Object o) throws MarshallingException {
        HandlerDataMapping handlerDataMapping = new HandlerDataMapping();

        List httpMappingList = BaseMarshaller.toType(o, List.class, true);
        for(Object httpMappingObject : httpMappingList) {
            Map httpMappingMap = BaseMarshaller.toType(httpMappingObject, Map.class, true);
            RequestPattern requestPattern = toHTTPRequestPattern(httpMappingMap.get(REQUEST));
            Response response = toHTTPResponse(httpMappingMap.get(RESPONSE));
            Table<ParserType, String, String> parsing = BaseMarshaller.ToTableOfType(httpMappingMap.get(PARSING), ParserType.class, String.class, String.class);
            handlerDataMapping.addMapping(requestPattern, response, parsing);
        }
        return handlerDataMapping;
    }

    public static RequestPattern toHTTPRequestPattern(Object o) throws MarshallingException {
        Map httpRequestPatternMap = BaseMarshaller.toType(o, Map.class, true);

        RequestPattern.Builder builder = RequestPattern.newBuilder();

        Object uriObject = httpRequestPatternMap.get(URI);
        String uri = BaseMarshaller.toString(uriObject);
        if(!uri.isEmpty()) {
            builder.setUriPattern(uri);
        }

        Object methodObject = httpRequestPatternMap.get(METHOD);
        builder.addMethodsAsStrings(BaseMarshaller.toListOfType(methodObject, String.class));

        Object queryParameterObject = httpRequestPatternMap.get(QUERY_PARAMETER);
        builder.addQueryParameters(BaseMarshaller.toMultimapOfType(queryParameterObject, String.class, String.class));

        Object headerObject = httpRequestPatternMap.get(HEADER);
        builder.addHeaders(BaseMarshaller.toMultimapOfType(headerObject, String.class, String.class));

        return builder.build();
    }

    public static Response toHTTPResponse(Object o) throws MarshallingException {
        Map httpResponseMap = BaseMarshaller.toType(o, Map.class, true);

        Response.Builder builder = Response.newBuilder();

        Object statusCodeObject = httpResponseMap.get(STATUS_CODE);
        String statusCode = BaseMarshaller.toString(statusCodeObject);
        if(!statusCode.isEmpty()) {
            builder.setStatusCode(statusCode);
        }

        Object headerObject = httpResponseMap.get(HEADER);
        builder.addHeaders(BaseMarshaller.toMapOfType(headerObject, String.class, String.class));

        Object contentObject = httpResponseMap.get(CONTENT);
        String content = BaseMarshaller.toString(contentObject);
        if(!content.isEmpty()) {
            builder.setContent(content);
        }

        return builder.build();
    }
}